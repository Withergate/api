package com.withergate.api.service.item;

import java.util.ArrayList;
import java.util.List;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.action.ActionState;
import com.withergate.api.game.model.action.CraftingAction;
import com.withergate.api.game.model.building.Building;
import com.withergate.api.game.model.building.BuildingDetails;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.item.Item;
import com.withergate.api.game.model.item.ItemDetails;
import com.withergate.api.game.model.item.ItemDetails.Rarity;
import com.withergate.api.game.model.item.ItemType;
import com.withergate.api.game.model.notification.ClanNotification;
import com.withergate.api.game.model.request.CraftingRequest;
import com.withergate.api.game.model.type.PassiveBonusType;
import com.withergate.api.game.repository.action.CraftingActionRepository;
import com.withergate.api.game.repository.item.ItemDetailsRepository;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.clan.CharacterService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CraftingServiceTest {

    private CraftingServiceImpl craftingService;

    @Mock
    private ClanService clanService;

    @Mock
    private CharacterService characterService;

    @Mock
    private ItemDetailsRepository detailsRepository;

    @Mock
    private CraftingActionRepository actionRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private RandomService randomService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        craftingService = new CraftingServiceImpl(clanService, characterService, detailsRepository, actionRepository, itemService,
                notificationService, randomService);
    }

    @Test
    public void testGivenClanWhenGettingAvailableItemsThenVerifyItemsFiltered() {
        // given clan
        Clan clan = new Clan();
        clan.setId(1);
        Mockito.when(clanService.getClan(1)).thenReturn(clan);

        clan.getBuildings().add(mockBuilding(PassiveBonusType.CRAFTING_GEAR, 1));

        List<ItemDetails> detailsList = new ArrayList<>();
        detailsList.add(mockItem(ItemType.WEAPON, Rarity.COMMON, 1, "weapon", 10));
        detailsList.add(mockItem(ItemType.GEAR, Rarity.COMMON, 1, "gear1", 10));
        detailsList.add(mockItem(ItemType.GEAR, Rarity.COMMON, 2, "gear2", 10));
        detailsList.add(mockItem(ItemType.GEAR, Rarity.EPIC, 1, "gear3", 10));
        Mockito.when(detailsRepository.findAll()).thenReturn(detailsList);

        // when getting item details
        List<ItemDetails> result = craftingService.getAvailableItems(1);

        // verify details filteres
        Assert.assertEquals(1, result.size());
        Assert.assertEquals("gear1", result.get(0).getIdentifier());
    }

    @Test
    public void testGivenCharacterWhenCraftingThenVerifyActionCreated() throws Exception {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setState(CharacterState.READY);
        character.setCraftsmanship(5);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setJunk(10);
        clan.getBuildings().add(mockBuilding(PassiveBonusType.CRAFTING_WEAPON, 2));
        character.setClan(clan);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        ItemDetails details = mockItem(ItemType.WEAPON, Rarity.COMMON, 1, "WEAPON", 8);
        Mockito.when(detailsRepository.getOne("WEAPON")).thenReturn(details);

        // when crafting
        CraftingRequest request = new CraftingRequest();
        request.setCharacterId(1);
        request.setItem("WEAPON");
        craftingService.saveCraftingAction(request, 1);

        // then verify item crafted
        Assert.assertEquals(7, clan.getJunk());
        Assert.assertEquals(CharacterState.BUSY, character.getState());
        Mockito.verify(actionRepository).save(Mockito.any(CraftingAction.class));
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenCharacterWhenCraftingWithoutJunkThenVerifyExceptionThrown() throws Exception {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setState(CharacterState.READY);
        character.setCraftsmanship(5);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setJunk(0);
        clan.getBuildings().add(mockBuilding(PassiveBonusType.CRAFTING_WEAPON, 2));
        character.setClan(clan);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        ItemDetails details = mockItem(ItemType.WEAPON, Rarity.COMMON, 1, "WEAPON", 8);
        Mockito.when(detailsRepository.getOne("WEAPON")).thenReturn(details);

        // when crafting
        CraftingRequest request = new CraftingRequest();
        request.setCharacterId(1);
        request.setItem("WEAPON");
        craftingService.saveCraftingAction(request, 1);

        // then verify exception thrown
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenCharacterWhenCraftingWithoutBuildingThenVerifyExceptionThrown() throws Exception {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setState(CharacterState.READY);
        character.setCraftsmanship(5);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setJunk(0);
        character.setClan(clan);
        Mockito.when(characterService.loadReadyCharacter(1, 1)).thenReturn(character);

        ItemDetails details = mockItem(ItemType.WEAPON, Rarity.COMMON, 1, "WEAPON", 8);
        Mockito.when(detailsRepository.getOne("WEAPON")).thenReturn(details);

        // when crafting
        CraftingRequest request = new CraftingRequest();
        request.setCharacterId(1);
        request.setItem("WEAPON");
        craftingService.saveCraftingAction(request, 1);

        // then verify exception thrown
    }

    @Test
    public void testGivenCraftingActionWhenProcessingActionsThenVerifyItemCrafted() {
        // given action
        Character character = new Character();
        character.setId(1);
        character.setState(CharacterState.BUSY);

        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);

        CraftingAction action = new CraftingAction();
        action.setState(ActionState.PENDING);
        action.setCharacter(character);
        action.setCraftingItem("OUTFIT");
        Mockito.when(actionRepository.findAllByState(ActionState.PENDING)).thenReturn(List.of(action));

        ItemDetails details = mockItem(ItemType.OUTFIT, Rarity.COMMON, 1, "Outfit", 5);
        Mockito.when(detailsRepository.getOne("OUTFIT")).thenReturn(details);

        // when processing actions
        craftingService.processCraftingActions(1);

        // then verify item crafted
        Mockito.verify(itemService).generateCraftableItem(Mockito.eq(character), Mockito.any(ClanNotification.class), Mockito.eq(details));
    }

    private ItemDetails mockItem(ItemType type, Rarity rarity, int craftingLevel, String identifier, int craftingCost) {
        ItemDetails details = new ItemDetails();
        details.setIdentifier(identifier);
        details.setItemType(type);
        details.setRarity(rarity);
        details.setCraftingLevel(craftingLevel);
        details.setCraftingCost(craftingCost);
        return details;
    }

    private Building mockBuilding(PassiveBonusType type, int level) {
        BuildingDetails details = new BuildingDetails();
        details.setPassiveBonusType(type);
        Building building = new Building();
        building.setDetails(details);
        building.setLevel(level);
        return building;
    }

}
