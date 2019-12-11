package com.withergate.api.controller.clan;

import java.security.Principal;

import com.withergate.api.model.request.CharacterRestRequest;
import com.withergate.api.model.request.TraitRequest;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.TraitService;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Character controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@RestController
public class CharacterController {

    private final CharacterService characterService;
    private final TraitService traitService;

    /**
     * Marks character as resting.
     *
     * @param request the request with character id
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/characters/rest")
    public ResponseEntity<Void> submitCharacterRestAction(Principal principal, @RequestBody CharacterRestRequest request)
            throws InvalidActionException {

        characterService.markCharacterAsResting(request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Activates trait for character.
     *
     * @param request the request with character id
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/characters/trait")
    public ResponseEntity<Void> activateTrait(Principal principal, @RequestBody TraitRequest request)
            throws InvalidActionException {

        traitService.activateTrait(request, Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
