package com.withergate.api.controller.item;

import com.withergate.api.model.request.ConsumableRequest;
import com.withergate.api.model.request.EquipRequest;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.item.ItemService;

import java.security.Principal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/items/equip")
    public ResponseEntity<Void> equipWeapon(Principal principal, @RequestBody EquipRequest request) throws InvalidActionException {
        itemService.equipItem(request.getItemId(), request.getItemType(), request.getCharacterId(),
                Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/items/unequip")
    public ResponseEntity<Void> unequipWeapon(Principal principal, @RequestBody EquipRequest request) throws InvalidActionException {
        itemService.unequipItem(request.getItemId(), request.getItemType(), request.getCharacterId(),
                Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/items/consumables/use", method = RequestMethod.POST)
    public ResponseEntity<Void> useConsumable(Principal principal, @RequestBody ConsumableRequest request) throws InvalidActionException {
        itemService.useConsumable(request.getConsumableId(), request.getCharacterId(), Integer.parseInt(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
