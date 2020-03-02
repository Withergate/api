package com.withergate.api.service.game;

import com.withergate.api.game.model.dto.InfoDTO;

/**
 * Info service. Providing basic information about the game.
 *
 * @author Martin Myslik
 */
public interface InfoService {

    /**
     * Collects and returns game info.
     *
     * @return game info
     */
    InfoDTO getGameInfo();

}
