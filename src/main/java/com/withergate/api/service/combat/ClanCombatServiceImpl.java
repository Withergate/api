package com.withergate.api.service.combat;

import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.ClanCombatAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.dto.ClanIntelDTO;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.request.ClanCombatRequest;
import com.withergate.api.repository.action.ClanCombatActionRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import jnr.ffi.annotations.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public void saveClanCombatAction(ClanCombatRequest request, int clanId) throws InvalidActionException {
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

        // save action
        ClanCombatAction action = new ClanCombatAction();
        action.setTargetId(request.getTargetId());
        action.setCharacter(character);
        action.setState(ActionState.PENDING);
        actionRepository.save(action);

        // mark character as busy
        character.setState(CharacterState.BUSY);
    }

    @Override
    public void processClanCombatActions(int turnId) {
        log.info("Executing clan combat actions...");
        List<ClanCombatAction> actions = actionRepository.findAllByState(ActionState.PENDING);

        for (ClanCombatAction action : actions) {
            Clan attacker = action.getCharacter().getClan();
            Clan target = clanService.getClan(action.getTargetId());

            // prepare notification
            ClanNotification attackerNotification = new ClanNotification(turnId, attacker.getId());
            attackerNotification.setHeader(action.getCharacter().getName());
            attackerNotification.setImageUrl(action.getCharacter().getImageUrl());
            notificationService.addLocalizedTexts(attackerNotification.getText(), "clan.combat.attack",
                    new String[]{target.getName()});

            ClanNotification defenderNotification = new ClanNotification(turnId, target.getId());
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

    private void processAction(ClanNotification attackerNotification, ClanNotification defenderNotification,
                               Character attacker, Character defender) {
        log.debug("Character {} from clan {} is attacking {}.", attacker, attacker.getClan().getName(), defender.getClan().getName());

        // handle combat
        boolean attackerSuccess = combatService.handleClanCombat(attackerNotification, defenderNotification, attacker, defender);

        if (attackerSuccess) {
            notificationService.addLocalizedTexts(attackerNotification.getText(), "clan.combat.victory", new String[]{});
            notificationService.addLocalizedTexts(defenderNotification.getText(), "clan.combat.failure", new String[]{});

            // increase experience
            attacker.changeExperience(2);

            // add reward
            try {
                ClanIntelDTO intel = clanService.getClanIntel(defender.getClan().getId(), attacker.getClan().getId());

                int fame = intel.getFameReward();
                int factionPoints = intel.getFactionReward();

                int food = Math.min(defender.getClan().getFood(), randomService.getRandomInt(1, RandomServiceImpl.K4));
                int junk = Math.min(defender.getClan().getJunk(), randomService.getRandomInt(1, RandomServiceImpl.K4));
                int caps = Math.min(defender.getClan().getCaps(), randomService.getRandomInt(1, RandomServiceImpl.K4));
                attackerNotification.changeFame(fame);
                attackerNotification.changeFactionPoints(factionPoints);

                attackerNotification.changeFood(food);
                defenderNotification.changeFood(- food);
                attackerNotification.changeJunk(junk);
                defenderNotification.changeJunk(- junk);
                attackerNotification.changeCaps(caps);
                defenderNotification.changeCaps(- caps);
            } catch (InvalidActionException e) {
                log.error("Cannot compute attacker reward.", e);
            }

        } else {
            notificationService.addLocalizedTexts(attackerNotification.getText(), "clan.combat.failure", new String[]{});
            notificationService.addLocalizedTexts(defenderNotification.getText(), "clan.combat.victory", new String[]{});

            // increase experience
            attacker.changeExperience(1);
        }
    }
}
