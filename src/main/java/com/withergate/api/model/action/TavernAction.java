package com.withergate.api.model.action;

import com.withergate.api.model.character.Character;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Tavern action.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "tavern_actions")
@Getter
@Setter
public class TavernAction extends BaseAction {

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "character_id", nullable = false, updatable = false)
    private Character character;

}
