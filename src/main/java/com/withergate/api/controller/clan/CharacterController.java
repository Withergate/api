package com.withergate.api.controller.clan;

import com.withergate.api.model.request.CharacterRestRequest;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.exception.InvalidActionException;

import java.security.Principal;
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
@RestController
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    /**
     * Marks character as resting.
     *
     * @param request the request with character id
     * @throws InvalidActionException
     */
    @PostMapping("/characters/rest")
    public ResponseEntity<Void> submitCharacterRestAction(Principal principal, @RequestBody CharacterRestRequest request) throws InvalidActionException {
        characterService.markCharacterAsResting(request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
