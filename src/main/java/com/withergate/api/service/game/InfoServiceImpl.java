package com.withergate.api.service.game;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.dto.FactionMiniDTO;
import com.withergate.api.game.model.dto.InfoDTO;
import com.withergate.api.game.model.faction.Faction;
import com.withergate.api.game.repository.clan.ClanRepository;
import com.withergate.api.profile.repository.ProfileRepository;
import com.withergate.api.service.faction.FactionService;
import com.withergate.api.service.turn.TurnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Info service implementation.
 *
 * @author Martin Myslik
 */
@RequiredArgsConstructor
@Service
public class InfoServiceImpl implements InfoService {

    private final ProfileRepository profileRepository;
    private final ClanRepository clanRepository;
    private final TurnService turnService;
    private final FactionService factionService;
    private final GameProperties properties;

    @Override
    public InfoDTO getGameInfo() {
        InfoDTO info = new InfoDTO();
        info.setCurrentTurn(turnService.getCurrentTurn());
        info.setMaxTurns(properties.getMaxTurns());
        info.setClanNum(clanRepository.count());
        info.setProfileNum(profileRepository.count());
        info.setTopClans(clanRepository.findTop3ByOrderByFameDesc());

        Faction faction = factionService.getBestFaction();
        info.setFaction(new FactionMiniDTO(faction.getIconUrl(), faction.getName(), faction.getPoints()));

        return info;
    }

}
