package com.withergate.api.service.clan;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.request.ClanRequest;
import com.withergate.api.game.model.type.AttributeTemplate.Type;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClanPresetIT {

    @Autowired
    private ClanService clanService;

    @Test
    public void testGivenPresetsWhenCreatingClansThenVerifyAttributeSumCorrect() throws Exception {
        // given presets
        Type[] presets = new Type[]{Type.BALANCED, Type.ECONOMY, Type.SMART, Type.COMBAT, Type.RANDOM};

        // when creating clans
        int clanId = 10;
        for (Type type : presets) {
            ClanRequest request = new ClanRequest("Clan " + type.name(), type);
            Clan clan = clanService.createClan(clanId, request, 1);
            clanId++;

            // verify sum
            Assert.assertEquals(65, getAttributeSum(clan));
        }
    }

    private int getAttributeSum(Clan clan) {
        int sum = 0;

        for (Character character : clan.getCharacters()) {
            sum += character.getCombat();
            sum += character.getScavenge();
            sum += character.getCraftsmanship();
            sum += character.getIntellect();
        }

        return sum;
    }

}

