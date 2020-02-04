package com.withergate.api.game.model.quest;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.withergate.api.game.model.encounter.SolutionCondition;
import com.withergate.api.game.model.encounter.SolutionType;
import com.withergate.api.game.model.item.ItemCost;
import com.withergate.api.game.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

/**
 * Quest details.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "quest_details")
@Getter
@Setter
public class QuestDetails {

    @Id
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "quest_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "quest_description")
    private Map<String, LocalizedText> description;

    @Column(name = "quest_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private SolutionType type;

    @Column(name = "quest_condition", updatable = false)
    @Enumerated(EnumType.STRING)
    private SolutionCondition condition;

    @Column(name = "completion", nullable = false)
    private int completion;

    @Column(name = "caps_reward", nullable = false)
    private int capsReward;

    @Column(name = "fame_reward", nullable = false)
    private int fameReward;

    @Column(name = "faction_reward", nullable = false)
    private int factionReward;

    @Column(name = "difficulty", nullable = false)
    private int difficulty;

    @Column(name = "food_cost", nullable = false)
    private int foodCost;

    @Column(name = "junk_cost", nullable = false)
    private int junkCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_cost", updatable = false, nullable = false)
    private ItemCost itemCost;

    @Column(name = "health_cost", nullable = false)
    private boolean healthCost;

    @Column(name = "follow_up", updatable = false, nullable = false)
    private String followUp;

    @Column(name = "faction_specific", nullable = false)
    private boolean factionSpecific;

    @Column(name = "faction", updatable = false, nullable = false)
    private String faction;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

}
