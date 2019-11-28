package com.withergate.api.controller.research;

import java.security.Principal;

import com.withergate.api.model.request.ResearchRequest;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.research.ResearchService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ResearchAction controller. Enables the execution of research actions.
 *
 * @author Martin Myslik
 */
@AllArgsConstructor
@RestController
public class ResearchController {

    private final ResearchService researchService;

    /**
     * Submits a new research action and checks if this action is applicable. Throws an exception if not.
     *
     * @param principal the principal
     * @param request   the research action request
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/research/action")
    public ResponseEntity<Void> submitResearchAction(Principal principal, @RequestBody ResearchRequest request)
            throws InvalidActionException {

        researchService.saveResearchAction(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
