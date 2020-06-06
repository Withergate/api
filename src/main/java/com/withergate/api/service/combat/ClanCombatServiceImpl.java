package com.withergate.api.service.combat;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionDescriptor;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.ClanCombatAction;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.dto.ClanIntelDTO;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.request.ClanCombatRequest;
import com.withergate.api.game.repository.action.ClanCombatActionRepository;
import com.withergate.api.profile.model.achievement.AchievementType;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.action.ActionOrder;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.profile.AchievementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Clan combat service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ClanCombatServiceImpl implements ClanCombatService {

    private final ClanCombatActionRepository actionRepository;
    private final ClanService clanService;
    private final NotificationService notificationService;
    private final CombatService combatService;
    private final CharacterService characterService;
    private final RandomService randomService;
    private final AchievementService achievementService;

    @Transactional
    @Override
    public void saveAction(ClanCombatRequest request, int clanId) throws InvalidActionException {
        log.debug("Receive clan combat request: {}.", request.toString());

        Character character = characterService.loadReadyCharacter(request.getCharacterId(), clanId);

        Clan target = clanService.getClan(request.getTargetId());
        if (target == null) {
            throw new InvalidActionException("Target clan does not exist!");
        }

        // check conditions
        if (character.getClan().getFaction() == null || target.getFaction() == null) {
            throw new InvalidActionException("Both clans need to be in a faction.");
        }
        if (character.getClan().getFaction().getIdentifier().equals(target.getFaction().getIdentifier())) {
            throw new InvalidActionException("Your target must be in a different faction than you.");
        }

        // only one attack action per clan is permitted
        for (Character ch : character.getClan().getCharacters()) {
            if (ch.getCurrentAction().isPresent()
                    && ch.getCurrentAction().get().getDescriptor().equals(ActionDescriptor.CLAN_COMBAT.name())) {
                throw new InvalidActionException("Only one attack action per turn is permitted!");
            }
        }

        // save action
        ClanCombatAction action = new ClanCombatAction();
        action.setTargetId(request.getTargetId());
        action.setCharacter(character);
        action.setState(ActionState.PENDING);
        actionRepository.save(action);

        // mark character as busy
        character.setState(CharacterState.BUSY);
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void runActions(int turn) {
        List<ClanCombatAction> actions = actionRepository.findAllByState(ActionState.PENDING);

        for (ClanCombatAction action : actions) {
            Clan attacker = action.getCharacter().getClan();
            Clan target = clanService.getClan(action.getTargetId());

            // prepare notification
            ClanNotification attackerNotification = new ClanNotification(turn, attacker.getId());
            attackerNotification.setHeader(action.getCharacter().getName());
            attackerNotification.setImageUrl(action.getCharacter().getImageUrl());
            notificationService.addLocalizedTexts(attackerNotification.getText(), "clan.combat.attack",
                    new String[]{target.getName()});

            ClanNotification defenderNotification = new ClanNotification(turn, target.getId());
            defenderNotification.setHeader(attacker.getName());
            notificationService.addLocalizedTexts(defenderNotification.getText(), "clan.combat.defense",
                    new String[]{attacker.getName()});

            // process action
            processAction(attackerNotification, defenderNotification, action.getCharacter(), target.getDefender());

            // mark action as completed
            action.setState(ActionState.COMPLETED);

            // save notifications
            notificationService.save(attackerNotification);
            notificationService.save(defenderNotification);
        }
    }

    @Override
    public int getOrder() {
        return ActionOrder.CLAN_COMBAT_ORDER;
    }

    private void processAction(ClanNotification attackerNotification, ClanNotification defenderNotification,
                               Character attacker, Character defender) {
        log.debug("Character {} from clan {} is attacking {}.", attacker, attacker.getClan().getName(), defender.getClan().getName());

        attacker.getClan().getStatistics().setOutcomingAttacks(attacker.getClan().getStatistics().getOutcomingAttacks() + 1);
        defender.getClan().getStatistics().setIncomingAttacks(defender.getClan().getStatistics().getIncomingAttacks() + 1);

        // handle combat
        boolean attackerSuccess = combatService.handleClanCombat(attackerNotification, defenderNotification, attacker, defender);

        if (attackerSuccess) { // ATTACKER SUCCESS
            notificationService.addLocalizedTexts(attackerNotification.getText(), "clan.combat.victory", new String[]{});
            notificationService.addLocalizedTexts(defenderNotification.getText(), "clan.combat.failure", new String[]{});

            // increase experience
            attacker.changeExperience(2);
            attackerNotification.changeExperience(2);

            // statistics
            attacker.getClan().getStatistics()
                    .setOutcomingAttacksSuccess(attacker.getClan().getStatistics().getOutcomingAttacksSuccess() + 1);
            achievementService.checkAchievementAward(attacker.getClan().getId(), AchievementType.ATTACK_SUCCESS_COUNT,
                    attacker.getClan().getStatistics().getOutcomingAttacksSuccess());

            // add reward
            try {
                ClanIntelDTO intel = clanService.getClanIntel(defender.getClan().getId(), attacker.getClan().getId());

                int fame = intel.getFameReward();
                int factionPoints = intel.getFactionReward();
                attacker.getClan().changeFame(fame);
                attackerNotification.changeFame(fame);
                attacker.getClan().changeFactionPoints(factionPoints);
                attackerNotification.changeFactionPoints(factionPoints);

                int food = Math.min(defender.getClan().getFood(), randomService.getRandomInt(1, RandomServiceImpl.K4));
                attackerNotification.changeFood(food);
                attacker.getClan().changeFood(food);
                defenderNotification.changeFood(- food);
                defender.getClan().changeFood(- food);
                int junk = Math.min(defender.getClan().getJunk(), randomService.getRandomInt(1, RandomServiceImpl.K4));
                attackerNotification.changeJunk(junk);
                attacker.getClan().changeJunk(junk);
                defenderNotification.changeJunk(- junk);
                defender.getClan().changeJunk(- junk);
                int caps = Math.min(defender.getClan().getCaps(), randomService.getRandomInt(1, RandomServiceImpl.K4));
                attackerNotification.changeCaps(caps);
                attacker.getClan().changeCaps(caps);
                defenderNotification.changeCaps(- caps);
                defender.getClan().changeCaps(- caps);
            } catch (InvalidActionException e) {
                log.error("Cannot compute attacker reward.", e);
            }

        } else { // DEFENDER SUCCESS
            notificationService.addLocalizedTexts(attackerNotification.getText(), "clan.combat.failure", new String[]{});
            notificationService.addLocalizedTexts(defenderNotification.getText(), "clan.combat.victory", new String[]{});

            // increase experience
            attacker.changeExperience(1);
            attackerNotification.changeExperience(1);

            // statistics
            defender.getClan().getStatistics()
                    .setIncomingAttacksSuccess(defender.getClan().getStatistics().getIncomingAttacksSuccess() + 1);
            achievementService.checkAchievementAward(defender.getClan().getId(), AchievementType.DEFENSE_SUCCESS_COUNT,
                    defender.getClan().getStatistics().getIncomingAttacksSuccess());
        }

        // remove injury from defender notification - not needed for generated defender
        defenderNotification.setInjury(0);
    }

}
