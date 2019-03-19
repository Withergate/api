package com.withergate.api.controller;

import com.withergate.api.model.request.ConsumableRequest;
import com.withergate.api.model.request.WeaponEquipRequest;
import com.withergate.api.service.item.IItemService;
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

    public ItemController(IItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(value = "/items/weapons/equip", method = RequestMethod.POST)
    public ResponseEntity<Void> equipWeapon(Principal principal, @RequestBody WeaponEquipRequest request) throws InvalidActionException {
        itemService.equipWeapon(request.getWeaponId(), request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/items/weapons/unequip", method = RequestMethod.POST)
    public ResponseEntity<Void> unequipWeapon(Principal principal, @RequestBody WeaponEquipRequest request) throws InvalidActionException {
        itemService.unequipWeapon(request.getWeaponId(), request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/items/consumables/use", method = RequestMethod.POST)
    public ResponseEntity<Void> useConsumable(Principal principal, @RequestBody ConsumableRequest request) throws InvalidActionException {
        itemService.useConsumable(request.getConsumableId(), request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
