package com.withergate.api.controller.quest;

import java.security.Principal;

import com.withergate.api.model.request.QuestRequest;
import com.withergate.api.service.action.ActionService;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Quest controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@RestController
public class QuestController {

    private final ActionService actionService;

    /**
     * Submits a new quest action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the trade action request
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/quests/action")
    public ResponseEntity<Void> submitQuestAction(Principal principal, @RequestBody QuestRequest request)
            throws InvalidActionException {
        actionService.createQuestAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
