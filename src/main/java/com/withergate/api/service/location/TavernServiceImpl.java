package com.withergate.api.service.location;

import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.TavernAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.repository.action.TavernActionRepository;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.notification.NotificationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Tavern service implementation.
 *
 * @author Martin Myslik
 */
@AllArgsConstructor
@Slf4j
@Service
public class TavernServiceImpl implements TavernService {

    private final TavernActionRepository tavernActionRepository;
    private final ClanService clanService;
    private final NotificationService notificationService;

    @Override
    public void saveTavernAction(TavernAction action) {
        tavernActionRepository.save(action);
    }

    @Override
    public void processTavernActions(int turnId) {
        log.debug("Processing tavern actions.");

        for (TavernAction action : tavernActionRepository.findAllByState(ActionState.PENDING)) {
            Character character = action.getCharacter();

            // prepare notification
            ClanNotification notification = new ClanNotification(turnId, character.getClan().getId());
            notification.setHeader(character.getName());

            // hire character
            Character hired = clanService.hireCharacter(character.getClan());

            notificationService.addLocalizedTexts(notification.getText(), "location.tavern.hired", new String[] {hired.getName()});
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.character.joined", new String[] {hired.getName()});
            notification.getDetails().add(detail);

            // save notification
            notificationService.save(notification);

            // mark action as completed
            action.setState(ActionState.COMPLETED);
        }
    }
}
