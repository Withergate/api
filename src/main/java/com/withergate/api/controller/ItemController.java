package com.withergate.api.controller;

import com.withergate.api.model.request.WeaponEquipRequest;
import com.withergate.api.service.clan.IItemService;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Item controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@RestController
public class ItemController {

    private final IItemService itemService;

    /**
     * Constructor.
     *
     * @param itemService item service
     */
    public ItemController(IItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(value = "/items/weapons/equip", method = RequestMethod.POST)
    public ResponseEntity<Void> equipWeapon(Principal principal, @RequestBody WeaponEquipRequest request) {
        try {
            itemService.equipWeapon(request.getWeaponId(), request.getCharacterId(), Integer.parseInt(principal.getName()));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidActionException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/items/weapons/unequip", method = RequestMethod.POST)
    public ResponseEntity<Void> unequipWeapon(Principal principal, @RequestBody WeaponEquipRequest request) {
        try {
            itemService.unequipWeapon(request.getWeaponId(), request.getCharacterId(), Integer.parseInt(principal.getName()));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (InvalidActionException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
