package com.withergate.api.model.action;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.quest.Quest;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Quest action. Used for sending a character to participate in a quest.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "quest_actions")
@Getter
@Setter
public class QuestAction extends BaseAction {

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "character_id", unique = true, nullable = false, updatable = false)
    private Character character;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "quest_id", unique = true, nullable = false, updatable = false)
    private Quest quest;

}
