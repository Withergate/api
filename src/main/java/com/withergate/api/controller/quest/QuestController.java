package com.withergate.api.controller.quest;

import com.withergate.api.game.model.request.QuestRequest;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.quest.QuestService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Quest controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@RestController
public class QuestController {

    private final QuestService questService;

    /**
     * Submits a new quest action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the quest action request
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/quests/action")
    public ResponseEntity<Void> submitQuestAction(Principal principal, @RequestBody QuestRequest request)
            throws InvalidActionException {
        questService.saveAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
