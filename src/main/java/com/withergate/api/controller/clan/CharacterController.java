package com.withergate.api.controller.clan;

import java.security.Principal;

import com.withergate.api.game.model.request.CharacterRestRequest;
import com.withergate.api.game.model.request.RenameRequest;
import com.withergate.api.game.model.request.TraitRequest;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.TraitService;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    /**
     * Renames character. Premium feature.
     *
     * @param principal the principal
     * @param characterId character ID
     * @param request the request with new name
     */
    @PutMapping("/characters/{characterId}")
    public ResponseEntity<Void> renameDefender(Principal principal, @PathVariable(name = "characterId") int characterId,
                                               @RequestBody RenameRequest request) throws InvalidActionException {
        log.debug("Renaming character {}", characterId);

        characterService.renameCharacter(characterId, Integer.parseInt(principal.getName()), request.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
