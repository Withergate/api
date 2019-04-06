package com.withergate.api.model.quest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.model.Clan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Quest entity.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "quests")
@Getter
@Setter
public class Quest {

    @Id
    @Column(name = "quest_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "progress", nullable = false)
    private int progress;

    @Column(name = "completed", nullable = false)
    private boolean completed;

    @ManyToOne(optional = false)
    @JoinColumn(name = "identifier")
    private QuestDetails details;

    @ManyToOne
    @JoinColumn(name = "clan_id")
    @JsonIgnore
    private Clan clan;

}
