package com.withergate.api.service.research;

import com.withergate.api.game.model.BonusType;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.ResearchAction;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.request.ResearchRequest;
import com.withergate.api.game.model.research.Research;
import com.withergate.api.game.model.research.ResearchDetails;
import com.withergate.api.game.repository.action.ResearchActionRepository;
import com.withergate.api.game.repository.research.ResearchDetailsRepository;
import com.withergate.api.service.BonusUtils;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final CharacterService characterService;

    @Transactional
    @Override
    public void saveResearchAction(ResearchRequest request, int clanId) throws InvalidActionException {
        log.debug("Submitting research action for request {}.", request);
        Character character = characterService.loadReadyCharacter(request.getCharacterId(), clanId);
        Clan clan = character.getClan();
        Research research = clan.getResearch(request.getResearch());

        // check if research is available
        if (research == null) {
            throw new InvalidActionException("The specified research is not available to your clan.");
        }

        // check requirements
        if (research.getDetails().getInformationLevel() > clan.getInformationLevel()) {
            throw new InvalidActionException("You need higher information level to perform this action.");
        }

        if (research.isCompleted()) {
            throw new InvalidActionException("This research has been completed already.");
        }

        ResearchAction action = new ResearchAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setResearch(research.getDetails().getIdentifier());

        actionRepository.save(action);

        // character needs to be marked as busy
        character.setState(CharacterState.BUSY);
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

            clan.getResearch().add(research);
        }
    }

    private void processResearchAction(ResearchAction action, ClanNotification notification) {
        Character character = action.getCharacter();
        Research research = character.getClan().getResearch(action.getResearch());
        ResearchDetails details = research.getDetails();

        // compute progress
        int progress = character.getIntellect() + BonusUtils.getBonus(character, BonusType.RESEARCH, notification, notificationService);

        research.setProgress(research.getProgress() + progress);

        notificationService.addLocalizedTexts(notification.getText(), "research.work", new String[] {}, details.getName());

        if (!research.isCompleted() && research.getProgress() >= details.getCost()) {
            research.setProgress(details.getCost());
            research.setCompleted(true);

            notificationService.addLocalizedTexts(notification.getText(), "research.complete", new String[] {}, details.getName());

            // award fame
            notification.changeFame(research.getDetails().getFame());
            character.getClan().changeFame(research.getDetails().getFame());
        }
    }

}
