package com.withergate.api.controller.item;

import com.withergate.api.model.request.ConsumableRequest;
import com.withergate.api.model.request.EquipRequest;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;

import java.security.Principal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Item controller.
 *
 * @author Martin Myslik
 */
@Slf4j
@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @RequestMapping(value = "/items/weapons/equip", method = RequestMethod.POST)
    public ResponseEntity<Void> equipWeapon(Principal principal, @RequestBody EquipRequest request) throws InvalidActionException {
        itemService.equipWeapon(request.getItemId(), request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/items/weapons/unequip", method = RequestMethod.POST)
    public ResponseEntity<Void> unequipWeapon(Principal principal, @RequestBody EquipRequest request) throws InvalidActionException {
        itemService.unequipWeapon(request.getItemId(), request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/items/gear/equip", method = RequestMethod.POST)
    public ResponseEntity<Void> equipGear(Principal principal, @RequestBody EquipRequest request) throws InvalidActionException {
        itemService.equipGear(request.getItemId(), request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/items/gear/unequip", method = RequestMethod.POST)
    public ResponseEntity<Void> unequipGear(Principal principal, @RequestBody EquipRequest request) throws InvalidActionException {
        itemService.unequipGear(request.getItemId(), request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/items/consumables/use", method = RequestMethod.POST)
    public ResponseEntity<Void> useConsumable(Principal principal, @RequestBody ConsumableRequest request) throws InvalidActionException {
        itemService.useConsumable(request.getConsumableId(), request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
