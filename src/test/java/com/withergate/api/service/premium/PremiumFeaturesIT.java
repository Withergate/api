package com.withergate.api.service.premium;

import java.util.Optional;

import com.withergate.api.profile.model.PremiumType;
import com.withergate.api.profile.model.Profile;
import com.withergate.api.profile.repository.ProfileRepository;
import com.withergate.api.service.profile.ProfileService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PremiumFeaturesIT {

    @Autowired
    private ProfileService profileService;

    @MockBean
    private ProfileRepository profileRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @WithMockUser(username = "1")
    public void testGivenPremiumUserWhenSwitchingThemeThenVerifyThemeSwitched() {
        // given premium user
        Profile profile = new Profile();
        profile.setId(1);
        profile.setTheme("light");
        profile.setPremiumType(PremiumType.SILVER);
        Mockito.when(profileRepository.findById(1)).thenReturn(Optional.of(profile));

        // when changing theme
        profileService.changeTheme(1, "dark");

        // then verify theme changed
        Assert.assertEquals("dark", profile.getTheme());
    }

    @Test(expected = Throwable.class)
    @WithMockUser(username = "1")
    public void testGivenNonPremiumUserWhenSwitchingThemeThenExpectException() {
        // given premium user
        Profile profile = new Profile();
        profile.setId(1);
        profile.setTheme("light");
        profile.setPremiumType(null);
        Mockito.when(profileRepository.findById(1)).thenReturn(Optional.of(profile));

        // when changing theme
        profileService.changeTheme(1, "dark");

        // then expect exception
    }


}
