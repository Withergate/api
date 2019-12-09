package com.withergate.api.service.clan;

import com.withergate.api.model.BonusType;
import com.withergate.api.model.Clan;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.building.BuildingDetails;
import com.withergate.api.model.building.BuildingDetails.BuildingName;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.repository.clan.CharacterRepository;
import com.withergate.api.repository.clan.TraitDetailsRepository;
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        traitService = new TraitServiceImpl(traitDetailsRepository, characterRepository);
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
        buildingDetails.setIdentifier(BuildingName.TRAINING_GROUNDS);
        Building building = new Building();
        building.setDetails(buildingDetails);
        building.setLevel(0);
        clan.getBuildings().put(BuildingName.TRAINING_GROUNDS, building);
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
        traitService.activateTrait(1, 1, "BUILDER");

        // then verify trait activated
        Assert.assertTrue(trait.isActive());
        Assert.assertEquals(CharacterState.RESTING, character.getState());
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
        buildingDetails.setIdentifier(BuildingName.TRAINING_GROUNDS);
        Building building = new Building();
        building.setDetails(buildingDetails);
        building.setLevel(1);
        clan.getBuildings().put(BuildingName.TRAINING_GROUNDS, building);
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
        traitService.activateTrait(1, 1, "BUILDER");

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
        buildingDetails.setIdentifier(BuildingName.TRAINING_GROUNDS);
        Building building = new Building();
        building.setDetails(buildingDetails);
        building.setLevel(1);
        clan.getBuildings().put(BuildingName.TRAINING_GROUNDS, building);
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
        traitService.activateTrait(1, 1, "BUILDER");

        // then expect exception
    }

}
