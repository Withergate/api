package com.withergate.api.game.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.turn.Turn;
import com.withergate.api.game.model.view.Views;
import lombok.Getter;
import lombok.Setter;

/**
 * Game info DTO.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class InfoDTO {

    private long clanNum;
    private long profileNum;
    private Turn currentTurn;

    @JsonView(Views.Public.class)
    private List<Clan> topClans;

}
