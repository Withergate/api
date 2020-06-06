package com.withergate.api.service.clan;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.RestingAction;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.notification.NotificationDetail;
import com.withergate.api.game.model.request.RestingRequest;
import com.withergate.api.game.model.research.Research;
import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.model.type.ResearchBonusType;
import com.withergate.api.game.repository.action.RestingActionRepository;
import com.withergate.api.game.repository.clan.CharacterRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.action.ActionOrder;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import com.withergate.api.service.utils.BonusUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Resting service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RestingServiceImpl implements RestingService {

    private final CharacterRepository characterRepository;
    private final RestingActionRepository repository;
    private final NotificationService notificationService;
    private final RandomService randomService;
    private final GameProperties properties;

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    @Retryable
    @Override
    public void runActions(int turn) {
        for (RestingAction action : repository.findAllByState(ActionState.PENDING)) {
            Character character = action.getCharacter();

            // prepare notification
            ClanNotification notification = new ClanNotification(turn, character.getClan().getId());
            notification.setHeader(character.getName());
            notification.setImageUrl(character.getImageUrl());
            notificationService.addLocalizedTexts(notification.getText(), "character.resting", new String[] {});

            // handle resting bonuses
            handleRestingBonuses(character, notification);

            handleHealing(character, notification);

            // save notification
            notificationService.save(notification);

            // mark action as done
            action.setState(ActionState.COMPLETED);
        }
    }

    @Transactional
    @Override
    public void saveAction(RestingRequest request, int clanId) throws InvalidActionException {
        Character character = characterRepository.getOne(request.getCharacterId());
        if (character.getState() != CharacterState.READY) {
            throw new InvalidActionException("Character is not READY.");
        }

        RestingAction action = new RestingAction();
        action.setCharacter(character);
        action.setState(ActionState.PENDING);

        repository.save(action);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
    }

    @Override
    public int getOrder() {
        return ActionOrder.RESTING_ORDER;
    }

    private void handleRestingBonuses(Character character, ClanNotification notification) {
        int exp = BonusUtils.getBonus(character, BonusType.TRAINING, notification, notificationService);
        if (exp > 0) {
            notification.changeExperience(exp);
            character.changeExperience(exp);
        }

        Research research = character.getClan().getResearch(ResearchBonusType.REST_FOOD);
        if (research != null && research.isCompleted()) {
            int food = randomService.getRandomInt(1, RandomServiceImpl.K4);
            character.getClan().changeFood(food);

            notification.changeFood(food);
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), research.getDetails().getBonusText(), new String[]{});
            notification.getDetails().add(detail);
        }
    }

    private void handleHealing(Character character, ClanNotification notification) {
        int hitpointsMissing = character.getMaxHitpoints() - character.getHitpoints();

        if (hitpointsMissing == 0 || character.getHitpoints() <= 0) {
            return;
        }

        // each character that is ready heals
        int points = properties.getHealing();

        // trait
        points += BonusUtils.getBonus(character, BonusType.HEALING, notification, notificationService);

        int healing = Math.min(points, hitpointsMissing);
        character.changeHitpoints(healing);
        notification.changeHealing(healing);
    }

}
