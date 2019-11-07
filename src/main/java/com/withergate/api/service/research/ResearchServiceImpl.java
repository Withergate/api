package com.withergate.api.service.research;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.ResearchAction;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails.TraitName;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.model.notification.NotificationDetail;
import com.withergate.api.model.research.Research;
import com.withergate.api.model.research.ResearchDetails;
import com.withergate.api.repository.action.ResearchActionRepository;
import com.withergate.api.repository.research.ResearchDetailsRepository;
import com.withergate.api.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Research service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ResearchServiceImpl implements ResearchService {

    private final ResearchDetailsRepository detailsRepository;
    private final ResearchActionRepository actionRepository;
    private final NotificationService notificationService;
    private final GameProperties gameProperties;

    @Override
    public void saveResearchAction(ResearchAction action) {
        actionRepository.save(action);
    }

    @Override
    public void processResearchActions(int turnId) {
        log.debug("Processing research actions...");

        for (ResearchAction action : actionRepository.findAllByState(ActionState.PENDING)) {
            ClanNotification notification = new ClanNotification(turnId, action.getCharacter().getClan().getId());
            notification.setHeader(action.getCharacter().getName());
            notification.setImageUrl(action.getCharacter().getImageUrl());

            // logic
            processResearchAction(action, notification);

            // award experience
            action.getCharacter().changeExperience(1);
            notification.changeExperience(1);

            // dismiss action
            action.setState(ActionState.COMPLETED);

            // save notification
            notificationService.save(notification);
        }
    }

    @Override
    public void assignResearch(Clan clan) {
        log.debug("Assigning initial research to clan");

        for (ResearchDetails details : detailsRepository.findAll()) {
            Research research = new Research();
            research.setDetails(details);
            research.setClan(clan);
            research.setProgress(0);
            research.setCompleted(false);

            clan.getResearch().put(details.getIdentifier(), research);
        }
    }

    private void processResearchAction(ResearchAction action, ClanNotification notification) {
        Character character = action.getCharacter();
        Research research = character.getClan().getResearch().get(action.getResearch());
        ResearchDetails details = research.getDetails();

        // compute progress
        int progress = character.getIntellect() + getResearchBonus(character, notification);

        research.setProgress(research.getProgress() + progress);

        notificationService.addLocalizedTexts(notification.getText(), "research.work", new String[] {}, details.getName());

        if (!research.isCompleted() && research.getProgress() >= details.getCost()) {
            research.setProgress(details.getCost());
            research.setCompleted(true);

            notificationService.addLocalizedTexts(notification.getText(), "research.complete", new String[] {}, details.getName());

            // award fame
            notification.changeFame(gameProperties.getResearchFame());
            character.getClan().changeFame(gameProperties.getResearchFame());
        }
    }

    private int getResearchBonus(Character character, ClanNotification notification) {
        int bonus = 0;

        if (character.getTraits().containsKey(TraitName.BOFFIN)) {
            Trait trait = character.getTraits().get(TraitName.BOFFIN);
            bonus += trait.getDetails().getBonus();
            NotificationDetail detail = new NotificationDetail();
            notificationService.addLocalizedTexts(detail.getText(), "detail.trait.boffin", new String[] {character.getName()},
                    trait.getDetails().getName());
            notification.getDetails().add(detail);
        }

        return bonus;
    }
}
