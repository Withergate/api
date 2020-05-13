package com.withergate.api.game.model.action;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.quest.Quest;
import com.withergate.api.game.model.quest.QuestDetails;
import lombok.Getter;
import lombok.Setter;

/**
 * Quest action. Used for sending a character to participate in a quest.
 *
 * @author Martin Myslik
 */
@Entity
@Getter
@Setter
public class QuestAction extends BaseAction {

    @JsonIgnore
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "quest_id", nullable = false, updatable = false)
    private Quest quest;

    @Override
    public String getDescriptor() {
        return ActionDescriptor.QUEST.name();
    }

    @Override
    public boolean isCancellable() {
        return true;
    }

    @Override
    public void cancel() {
        Clan clan = getCharacter().getClan();
        QuestDetails details = quest.getDetails();

        clan.changeCaps(details.getCapsCost());
        clan.changeFood(details.getFoodCost());
        clan.changeJunk(details.getJunkCost());
    }

}
