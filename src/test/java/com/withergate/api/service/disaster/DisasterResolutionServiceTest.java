package com.withergate.api.service.disaster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.building.BuildingDetails.BuildingName;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.disaster.Disaster;
import com.withergate.api.model.disaster.DisasterDetails;
import com.withergate.api.model.disaster.DisasterPenalty;
import com.withergate.api.model.disaster.DisasterPenalty.Type;
import com.withergate.api.model.item.ItemType;
import com.withergate.api.model.item.Weapon;
import com.withergate.api.model.item.WeaponDetails;
import com.withergate.api.model.notification.ClanNotification;
import com.withergate.api.service.RandomService;
import com.withergate.api.service.RandomServiceImpl;
import com.withergate.api.service.item.ItemService;
import com.withergate.api.service.notification.NotificationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DisasterResolutionServiceTest {

    private DisasterResolutionService disasterResolutionService;

    @Mock
    private ItemService itemService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private RandomService randomService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties properties = new GameProperties();
        properties.setDisasterFailureThreshold(50);
        properties.setDisasterPartialSuccessThreshold(75);
        properties.setDisasterResourceLoss(20);
        properties.setDisasterFameLoss(20);
        properties.setDisasterBuildingProgressLoss(8);

        disasterResolutionService = new DisasterResolutionServiceImpl(itemService, notificationService, randomService, properties);
    }

    @Test
    public void testGivenClanWithDisasterSuccessWhenResolvingDisasterThenVerifySuccessNotificationCreated() {
        // given clan
        Clan clan = new Clan();
        clan.setName("Stalkers");
        clan.setId(1);
        clan.setDisasterProgress(100);

        DisasterDetails details = new DisasterDetails();
        details.setIdentifier("disaster");
        details.setName(new HashMap<>());
        details.setSuccessText("disaster.success");
        Disaster disaster = new Disaster();
        disaster.setDetails(details);

        // when resolving disaster
        ClanNotification notification = new ClanNotification();
        disasterResolutionService.handleDisasterResolution(clan, notification, disaster);

        // then verify notification prepared
        Mockito.verify(notificationService).addLocalizedTexts(notification.getText(), "disaster.success", new String[]{},
                disaster.getDetails().getName());
    }

    @Test
    public void testGivenClanWithPartialSuccessWhenResolvingDisasterThenVerifyOnePenaltyApplied() {
        // given clan
        Clan clan = new Clan();
        clan.setName("Stalkers");
        clan.setId(1);
        clan.setDisasterProgress(80);
        clan.setJunk(50);
        clan.setFood(50);
        clan.setFame(50);

        DisasterDetails details = new DisasterDetails();
        details.setIdentifier("disaster");
        details.setName(new HashMap<>());
        details.setSuccessText("disaster.success");
        details.setPenalties(new ArrayList<>());
        Disaster disaster = new Disaster();
        disaster.setDetails(details);

        DisasterPenalty penalty1 = new DisasterPenalty();
        penalty1.setPenaltyType(Type.RESOURCES_LOSS);
        details.getPenalties().add(penalty1);
        DisasterPenalty penalty2 = new DisasterPenalty();
        penalty2.setPenaltyType(Type.FAME_LOSS);
        details.getPenalties().add(penalty2);

        // when resolving disaster
        ClanNotification notification = new ClanNotification();
        disasterResolutionService.handleDisasterResolution(clan, notification, disaster);

        // then verify penalty applied
        Assert.assertEquals(30, clan.getFood());
        Assert.assertEquals(30, clan.getJunk());
        Assert.assertEquals(50, clan.getFame());
    }

    @Test
    public void testGivenClanWithPartialSuccessWhenResolvingDisasterThenVerifyTwoPenaltiesApplied() {
        // given clan
        Clan clan = new Clan();
        clan.setName("Stalkers");
        clan.setId(1);
        clan.setDisasterProgress(60);
        clan.setJunk(50);
        clan.setFood(50);
        clan.setFame(50);
        clan.setWeapons(new HashSet<>());
        clan.setOutfits(new HashSet<>());
        clan.setGear(new HashSet<>());
        clan.setConsumables(new HashSet<>());

        WeaponDetails weaponDetails = new WeaponDetails();
        weaponDetails.setItemType(ItemType.WEAPON);
        weaponDetails.setIdentifier("Weapon");
        Weapon weapon = new Weapon();
        weapon.setDetails(weaponDetails);
        weapon.setClan(clan);
        clan.getWeapons().add(weapon);

        DisasterDetails details = new DisasterDetails();
        details.setIdentifier("disaster");
        details.setName(new HashMap<>());
        details.setSuccessText("disaster.success");
        details.setPenalties(new ArrayList<>());
        Disaster disaster = new Disaster();
        disaster.setDetails(details);

        DisasterPenalty penalty1 = new DisasterPenalty();
        penalty1.setPenaltyType(Type.ITEM_LOSS);
        details.getPenalties().add(penalty1);
        DisasterPenalty penalty2 = new DisasterPenalty();
        penalty2.setPenaltyType(Type.FAME_LOSS);
        details.getPenalties().add(penalty2);

        Mockito.when(randomService.getRandomInt(0, 0)).thenReturn(0); // item roll

        // when resolving disaster
        ClanNotification notification = new ClanNotification();
        disasterResolutionService.handleDisasterResolution(clan, notification, disaster);

        // then verify penalties applied
        Assert.assertEquals(30, clan.getFame());
        Assert.assertEquals(null, weapon.getClan());
        Mockito.verify(itemService).deleteItem(weapon);
    }

    @Test
    public void testGivenClanWithFailureWhenResolvingDisasterThenVerifyTwoPenaltiesApplied() {
        // given clan
        Clan clan = new Clan();
        clan.setName("Stalkers");
        clan.setId(1);
        clan.setDisasterProgress(10);
        clan.setJunk(50);
        clan.setFood(50);
        clan.setFame(50);
        clan.setCharacters(new HashSet<>());
        Character character = new Character();
        character.setName("John");
        character.setHitpoints(10);
        clan.getCharacters().add(character);

        DisasterDetails details = new DisasterDetails();
        details.setIdentifier("disaster");
        details.setName(new HashMap<>());
        details.setSuccessText("disaster.success");
        details.setPenalties(new ArrayList<>());
        Disaster disaster = new Disaster();
        disaster.setDetails(details);

        DisasterPenalty penalty1 = new DisasterPenalty();
        penalty1.setPenaltyType(Type.FAME_LOSS);
        details.getPenalties().add(penalty1);
        DisasterPenalty penalty2 = new DisasterPenalty();
        penalty2.setPenaltyType(Type.FAME_LOSS);
        details.getPenalties().add(penalty2);
        DisasterPenalty penalty3 = new DisasterPenalty();
        penalty3.setPenaltyType(Type.CHARACTER_INJURY);
        details.getPenalties().add(penalty3);

        Mockito.when(randomService.getRandomInt(1, RandomServiceImpl.K6)).thenReturn(5); // injury roll

        // when resolving disaster
        ClanNotification notification = new ClanNotification();
        disasterResolutionService.handleDisasterResolution(clan, notification, disaster);

        // then verify penalties applied
        Assert.assertEquals(10, clan.getFame());
        Assert.assertEquals(5, character.getHitpoints());
    }

    @Test
    public void testGivenClanWithPartialSuccessWhenResolvingDisasterThenVerifyBuildingPenaltyApplied() {
        // given clan
        Clan clan = new Clan();
        clan.setName("Stalkers");
        clan.setId(1);
        clan.setDisasterProgress(80);
        clan.setBuildings(new HashMap<>());

        BuildingDetails buildingDetails1 = new BuildingDetails();
        buildingDetails1.setIdentifier(BuildingName.MONUMENT);
        buildingDetails1.setCost(15);
        Building building1 = new Building();
        building1.setLevel(2);
        building1.setProgress(5);
        building1.setDetails(buildingDetails1);
        clan.getBuildings().put(BuildingName.MONUMENT, building1);

        BuildingDetails buildingDetails2 = new BuildingDetails();
        buildingDetails2.setIdentifier(BuildingName.FORGE);
        buildingDetails2.setCost(10);
        Building building2 = new Building();
        building2.setLevel(0);
        building2.setProgress(4);
        building2.setDetails(buildingDetails2);
        clan.getBuildings().put(BuildingName.FORGE, building2);

        DisasterDetails details = new DisasterDetails();
        details.setIdentifier("disaster");
        details.setName(new HashMap<>());
        details.setSuccessText("disaster.success");
        details.setPenalties(new ArrayList<>());
        Disaster disaster = new Disaster();
        disaster.setDetails(details);

        DisasterPenalty penalty1 = new DisasterPenalty();
        penalty1.setPenaltyType(Type.BUILDING_DESTRUCTION);
        details.getPenalties().add(penalty1);

        // when resolving disaster
        ClanNotification notification = new ClanNotification();
        disasterResolutionService.handleDisasterResolution(clan, notification, disaster);

        // then verify penalty applied
        Assert.assertEquals(1, clan.getBuildings().get(BuildingName.MONUMENT).getLevel());
        Assert.assertEquals(19, clan.getBuildings().get(BuildingName.MONUMENT).getProgress());
        Assert.assertEquals(0, clan.getBuildings().get(BuildingName.FORGE).getLevel());
        Assert.assertEquals(0, clan.getBuildings().get(BuildingName.FORGE).getProgress());
    }

}
