package com.withergate.api.controller.item;

import com.withergate.api.game.model.request.EquipRequest;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Item controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@RestController
public class ItemController {

    private final ItemService itemService;

    /**
     * Handles item equip action.
     *
     * @param principal the principal
     * @param request the equip request
     * @return empty response body
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/items/equip")
    public ResponseEntity<Void> equipWeapon(Principal principal, @RequestBody EquipRequest request)
            throws InvalidActionException {

        itemService.equipItem(request.getItemId(), request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Handles item unequip action.
     *
     * @param principal the principal
     * @param request the unequip request
     * @return empty response body
     * @throws InvalidActionException invalid action
     */
    @PostMapping("/items/unequip")
    public ResponseEntity<Void> unequipWeapon(Principal principal, @RequestBody EquipRequest request)
            throws InvalidActionException {

        itemService.unequipItem(request.getItemId(), request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
