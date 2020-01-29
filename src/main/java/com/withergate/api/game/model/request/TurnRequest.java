package com.withergate.api.game.model.request;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * Turn request. Used by admins.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class TurnRequest {

    private LocalDate startDate;

}
