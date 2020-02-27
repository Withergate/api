package com.withergate.api;

import java.util.List;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.request.ClanRequest;
import com.withergate.api.profile.model.HistoricalResult;
import com.withergate.api.profile.model.Profile;
import com.withergate.api.service.AdminService;
import com.withergate.api.service.clan.ClanService;
import com.withergate.api.service.profile.HistoricalResultsService;
import com.withergate.api.service.profile.ProfileService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameRestartIT {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ClanService clanService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private HistoricalResultsService resultsService;

    @Test
    public void testGivenClanWhenCreatingNewThenVerifyStatisticsCreated() throws Exception {
        // given clan
        ClanRequest request = new ClanRequest();
        request.setName("Warbarons");

        // when creating clan
        Clan clan = clanService.createClan(1, request, 1);

        // verify statistics created
        Assert.assertEquals(clan.getName(), clanService.getClan(1).getName());
        Assert.assertNotNull(clan.getStatistics());
    }

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    public void testGivenClanWhenRestartingGameThenVerifyClanDeleted() throws Exception {
        // given clan
        ClanRequest request = new ClanRequest();
        request.setName("Warbarons");
        Clan clan = clanService.createClan(1, request, 1);
        Assert.assertEquals(clan.getName(), clanService.getClan(1).getName());
        Assert.assertNotNull(clan.getStatistics());

        adminService.restartGame();

        // then verify clan does not exist
        Assert.assertNull(clanService.getClan(1));
    }

    @Test
    @WithMockUser(username = "1", roles = {"ADMIN"})
    public void testGivenHistoricalResultWhenRestartingGameThenVerifyResultNotDeleted() {
        Clan clan = new Clan();
        clan.setId(1);
        clan.setFame(10);
        clan.setName("Warbarons");

        Profile profile = new Profile();
        profile.setId(1);
        profile.setName("Stalker");
        profileService.saveProfile(profile);

        // given result
        HistoricalResult result = new HistoricalResult();
        resultsService.saveResults(List.of(clan));
        Assert.assertEquals(1, resultsService.loadResults(1).size());

        // when restarting game
        adminService.restartGame();

        // then verify results persisted
        Assert.assertEquals("Warbarons", resultsService.loadResults(1).get(0).getClanName());
    }

}
