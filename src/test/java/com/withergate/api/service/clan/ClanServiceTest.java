package com.withergate.api.service.clan;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.request.ClanRequest;
import com.withergate.api.repository.clan.ClanRepository;
import com.withergate.api.service.exception.EntityConflictException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class ClanServiceTest {

    private ClanServiceImpl clanService;

    @Mock
    private ClanRepository clanRepository;

    @Mock
    private CharacterService characterService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        GameProperties gameProperties = new GameProperties();
        gameProperties.setInitialClanSize(5);

        clanService = new ClanServiceImpl(clanRepository, characterService, gameProperties);
    }

    @Test(expected = EntityConflictException.class)
    public void testGivenExistingClanNameWhenCreatingClanThenExpectException() throws EntityConflictException {
        // given existing clan name
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");

        Mockito.when(clanRepository.findOneByName("Stalkers")).thenReturn(clan);

        // when creating new clan
        ClanRequest clanRequest = new ClanRequest("Stalkers");
        clanService.createClan(2, clanRequest);

        // then expect exception
    }

    @Test(expected = EntityConflictException.class)
    public void testGivenExistingClanIdWhenCreatingClanThenExpectException() throws EntityConflictException {
        // given existing clan name
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");

        Mockito.when(clanRepository.findById(1)).thenReturn(Optional.of(clan));

        // when creating new clan
        ClanRequest clanRequest = new ClanRequest("Dragons");
        clanService.createClan(1, clanRequest);

        // then expect exception
    }

    @Test
    public void testGivenUniqueClanWhenCreatingClanThenVerifyClanSaved() throws EntityConflictException {
        // given existing clan name
        Clan clan = new Clan();
        clan.setId(1);
        clan.setName("Stalkers");

        Mockito.when(clanRepository.findById(1)).thenReturn(Optional.of(clan));

        Character character = new Character();
        Mockito.when(characterService.generateRandomCharacter()).thenReturn(character);

        // when creating new clan
        ClanRequest clanRequest = new ClanRequest("Dragons");
        clanService.createClan(2, clanRequest);

        // then verify clan saved
        ArgumentCaptor<Clan> captor = ArgumentCaptor.forClass(Clan.class);
        Mockito.verify(clanRepository).save(captor.capture());

        assertEquals("Dragons", captor.getValue().getName());
        assertEquals(5, captor.getValue().getCharacters().size());
        assertEquals(0, captor.getValue().getFame());
    }
}
