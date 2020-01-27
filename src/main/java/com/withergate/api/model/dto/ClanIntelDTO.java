package com.withergate.api.model.dto;

import com.withergate.api.model.Clan;
import com.withergate.api.model.building.Building;
import com.withergate.api.model.research.Research;
import lombok.Getter;
import lombok.Setter;

/**
 * Clan intel DTO. Used for providing information about clan based on information level.
 *
 * @author Martin Myslik
 */
@Getter
@Setter
public class ClanIntelDTO {

    private String name;
    private String faction;
    private Integer fame;
    private Integer characters;
    private Integer defense;
    private Integer caps;
    private Integer buildings;
    private Integer research;
    private Integer disasterProgress;
    private Integer food;
    private Integer junk;

    /**
     * Constructor. Assigns information about the target based on the spy's information level.
     *
     * @param target target clan
     * @param spy spy clan
     */
    public ClanIntelDTO(Clan target, Clan spy) {
        name = target.getName();
        faction = target.getFaction() != null ? target.getFaction().getIdentifier() : null;
        fame = target.getFame();

        if (spy.getInformationLevel() >= 1) {
            characters = target.getCharacters().size();
        }
        if (spy.getInformationLevel() >= 2) {
            defense = target.getDefender().getCombat();
        }
        if (spy.getInformationLevel() >= 3) {
            caps = target.getCaps();
        }
        if (spy.getInformationLevel() >= 4) {
            buildings = target.getBuildings().stream().mapToInt(Building::getLevel).sum();
        }
        if (spy.getInformationLevel() >= 5) {
            research = (int) target.getResearch().stream().filter(Research::isCompleted).count();
        }
        if (spy.getInformationLevel() >= 6) {
            disasterProgress = target.getDisasterProgress();
        }
        if (spy.getInformationLevel() >= 7) {
            food = target.getFood();
            junk = target.getJunk();
        }
    }

}
