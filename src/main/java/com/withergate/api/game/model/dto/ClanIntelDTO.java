package com.withergate.api.game.model.dto;

import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.building.Building;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.research.Research;
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

    private static final int FAME_COEFFICIENT = 3;
    private static final int MAX_FAME = 5;
    private static final int FACTION_COEFFICIENT = 5;
    private static final int MAX_FACTION_POINTS = 10;

    private int clanId;
    private String name;
    private String faction;
    private Integer fame;

    private Integer characters;;
    private Integer caps;
    private Integer buildings;
    private Integer research;
    private Integer disasterProgress;
    private Integer food;
    private Integer junk;
    private Character defender;

    // dynamic statistics
    private int fameReward;
    private int factionReward;

    /**
     * Constructor. Assigns information about the target based on the spy's information level.
     *
     * @param target target clan
     * @param spy spy clan
     */
    public ClanIntelDTO(Clan target, Clan spy) {
        clanId = target.getId();
        name = target.getName();
        faction = target.getFaction() != null ? target.getFaction().getIdentifier() : null;
        fame = target.getFame();
        fameReward = computeReward(target, spy,FAME_COEFFICIENT, MAX_FAME);
        factionReward = computeReward(target, spy, FACTION_COEFFICIENT, MAX_FACTION_POINTS);

        if (spy.getInformationLevel() >= 1) {
            characters = target.getCharacters().size();
        }
        if (spy.getInformationLevel() >= 2) {
            defender = target.getDefender();
        }
        if (spy.getInformationLevel() >= 3) {
            caps = target.getCaps();
        }
        if (spy.getInformationLevel() >= 4) {
            food = target.getFood();
            junk = target.getJunk();
        }
        if (spy.getInformationLevel() >= 5) {
            buildings = target.getBuildings().stream().mapToInt(Building::getLevel).sum();
            research = (int) target.getResearch().stream().filter(Research::isCompleted).count();
        }
        if (spy.getInformationLevel() >= 6) {
            disasterProgress = target.getDisasterProgress();
        }
    }

    private int computeReward(Clan target, Clan spy, int coefficient, int max) {
        // set min fame to 1
        int spyFame = spy.getFame() < 1 ? 1 : spy.getFame();

        double ratio = (double) (target.getFame()) / spyFame;
        int result =  (int) (coefficient * ratio);
        if (result > max) result = max;
        return result;
    }

}
