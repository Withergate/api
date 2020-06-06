package com.withergate.api.service.clan;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.building.Building;
import com.withergate.api.game.model.building.BuildingDetails;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.CharacterState;
import com.withergate.api.game.model.character.Trait;
import com.withergate.api.game.model.character.TraitDetails;
import com.withergate.api.game.model.request.RestingRequest;
import com.withergate.api.game.model.request.TraitRequest;
import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.repository.clan.CharacterRepository;
import com.withergate.api.game.repository.clan.TraitDetailsRepository;
import com.withergate.api.service.exception.InvalidActionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class TraitServiceTest {

    private TraitService traitService;

    @Mock
    private TraitDetailsRepository traitDetailsRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private RestingService restingService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties properties = new GameProperties();
        properties.setTrainingPrice(5);

        traitService = new TraitServiceImpl(traitDetailsRepository, characterRepository, restingService, properties);
    }

    @Test
    public void testGivenCharacterWhenActivatingTraitThenVerifyTraitActivated() throws Exception {
        // given character
        Character character = new Character();
        character.setState(CharacterState.READY);
        character.setId(1);
        character.setSkillPoints(1);

        Clan clan = new Clan();
        clan.setId(1);
        BuildingDetails buildingDetails = new BuildingDetails();
        buildingDetails.setIdentifier("TRAINING_GROUNDS");
        buildingDetails.setBonusType(BonusType.TRAINING);
        buildingDetails.setBonus(1);
        Building building = new Building();
        building.setDetails(buildingDetails);
        building.setLevel(0);
        clan.getBuildings().add(building);
        character.setClan(clan);

        TraitDetails details = new TraitDetails();
        details.setIdentifier("BUILDER");
        details.setBonusType(BonusType.CONSTRUCT);
        Trait trait = new Trait();
        trait.setActive(false);
        trait.setOrder(0);
        trait.setDetails(details);
        character.getTraits().add(trait);

        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        // when activating trait
        TraitRequest request = new TraitRequest();
        request.setCharacterId(1);
        request.setImmediate(false);
        request.setTraitName("BUILDER");
        traitService.activateTrait(request, 1);

        // then verify trait activated
        Assert.assertTrue(trait.isActive());
        Mockito.verify(restingService).saveAction(Mockito.any(RestingRequest.class), Mockito.eq(1));
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenCharacterWhenActivatingInvalidTraitThenVerifyExceptionThrown() throws Exception {
        // given character
        Character character = new Character();
        character.setState(CharacterState.READY);
        character.setId(1);
        character.setSkillPoints(1);

        Clan clan = new Clan();
        clan.setId(1);
        BuildingDetails buildingDetails = new BuildingDetails();
        buildingDetails.setIdentifier("TRAINING_GROUNDS");
        buildingDetails.setBonusType(BonusType.TRAINING);
        buildingDetails.setBonus(1);
        Building building = new Building();
        building.setDetails(buildingDetails);
        building.setLevel(1);
        clan.getBuildings().add(building);
        character.setClan(clan);

        TraitDetails details = new TraitDetails();
        details.setIdentifier("BUILDER");
        Trait trait = new Trait();
        trait.setActive(false);
        trait.setOrder(3);
        trait.setDetails(details);
        character.getTraits().add(trait);

        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        // when activating trait
        TraitRequest request = new TraitRequest();
        request.setCharacterId(1);
        request.setImmediate(false);
        request.setTraitName("BUILDER");
        traitService.activateTrait(request, 1);

        // then expect exception
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenCharacterWithoutSkillpointsWhenActivatingTraitThenVerifyExceptionThrown() throws Exception {
        // given character
        Character character = new Character();
        character.setState(CharacterState.READY);
        character.setId(1);
        character.setSkillPoints(0);

        Clan clan = new Clan();
        clan.setId(1);
        BuildingDetails buildingDetails = new BuildingDetails();
        buildingDetails.setIdentifier("Forge");
        Building building = new Building();
        building.setDetails(buildingDetails);
        building.setLevel(1);
        clan.getBuildings().add(building);
        character.setClan(clan);

        TraitDetails details = new TraitDetails();
        details.setIdentifier("BUILDER");
        Trait trait = new Trait();
        trait.setActive(false);
        trait.setOrder(0);
        trait.setDetails(details);
        character.getTraits().add(trait);

        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        // when activating trait
        TraitRequest request = new TraitRequest();
        request.setCharacterId(1);
        request.setImmediate(false);
        request.setTraitName("BUILDER");
        traitService.activateTrait(request, 1);

        // then expect exception
    }

    @Test
    public void testGivenCharacterWhenActivatingTraitImmediatelyThenVerifyCharacterReady() throws Exception {
        // given character
        Character character = new Character();
        character.setState(CharacterState.READY);
        character.setId(1);
        character.setSkillPoints(1);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setCaps(5);
        BuildingDetails buildingDetails = new BuildingDetails();
        buildingDetails.setIdentifier("TRAINING_GROUNDS");
        buildingDetails.setBonusType(BonusType.TRAINING);
        buildingDetails.setBonus(1);
        Building building = new Building();
        building.setDetails(buildingDetails);
        building.setLevel(0);
        clan.getBuildings().add(building);
        character.setClan(clan);

        TraitDetails details = new TraitDetails();
        details.setIdentifier("BUILDER");
        details.setBonusType(BonusType.CONSTRUCT);
        Trait trait = new Trait();
        trait.setActive(false);
        trait.setOrder(0);
        trait.setDetails(details);
        character.getTraits().add(trait);

        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        // when activating trait
        TraitRequest request = new TraitRequest();
        request.setCharacterId(1);
        request.setImmediate(true);
        request.setTraitName("BUILDER");
        traitService.activateTrait(request, 1);

        // then verify trait activated and character ready
        Assert.assertTrue(trait.isActive());
        Assert.assertEquals(CharacterState.READY, character.getState());
    }

    @Test(expected = InvalidActionException.class)
    public void testGivenClanWithoutCapsWhenActivatingTraitImmediatelyThenVerifyExceptionThrown() throws Exception {
        // given character
        Character character = new Character();
        character.setState(CharacterState.READY);
        character.setId(1);
        character.setSkillPoints(1);

        Clan clan = new Clan();
        clan.setId(1);
        clan.setCaps(1);
        BuildingDetails buildingDetails = new BuildingDetails();
        buildingDetails.setIdentifier("TRAINING_GROUNDS");
        buildingDetails.setBonusType(BonusType.TRAINING);
        buildingDetails.setBonus(1);
        Building building = new Building();
        building.setDetails(buildingDetails);
        building.setLevel(1);
        clan.getBuildings().add(building);
        character.setClan(clan);

        TraitDetails details = new TraitDetails();
        details.setIdentifier("BUILDER");
        Trait trait = new Trait();
        trait.setActive(false);
        trait.setOrder(0);
        trait.setDetails(details);
        character.getTraits().add(trait);

        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        // when activating trait
        TraitRequest request = new TraitRequest();
        request.setCharacterId(1);
        request.setImmediate(true);
        request.setTraitName("BUILDER");
        traitService.activateTrait(request, 1);

        // then expect exception
    }

    @Test
    public void testGivenCharacterWhenForgettingTraitThenVerifyOrderChanged() throws Exception {
        // given character
        Character character = new Character();
        character.setId(1);
        character.setSkillPoints(1);
        Clan clan = new Clan();
        clan.setId(1);
        character.setClan(clan);
        Mockito.when(characterRepository.getOne(1)).thenReturn(character);

        TraitDetails d1 = new TraitDetails();
        d1.setIdentifier("BUILDER");
        Trait t1 = new Trait();
        t1.setOrder(0);
        t1.setDetails(d1);
        character.getTraits().add(t1);

        TraitDetails d2 = new TraitDetails();
        d2.setIdentifier("ROBBER");
        Trait t2 = new Trait();
        t2.setOrder(1);
        t2.setDetails(d2);
        character.getTraits().add(t2);

        TraitDetails d3 = new TraitDetails();
        d3.setIdentifier("SCAVENGER");
        Trait t3 = new Trait();
        t3.setOrder(2);
        t3.setDetails(d3);
        character.getTraits().add(t3);

        // when forgetting trait
        TraitRequest request = new TraitRequest();
        request.setCharacterId(1);
        request.setTraitName("ROBBER");
        traitService.forgetTrait(request, 1);

        // then verify order changed
        Assert.assertEquals(0, t1.getOrder());
        Assert.assertEquals(9, t2.getOrder());
        Assert.assertEquals(1, t3.getOrder());
    }

}
