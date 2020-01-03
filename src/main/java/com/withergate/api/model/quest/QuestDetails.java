package com.withergate.api.model.quest;

import com.withergate.api.model.encounter.SolutionCondition;
import com.withergate.api.model.encounter.SolutionType;
import com.withergate.api.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

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
import java.util.Map;

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

    @Column(name = "difficulty", nullable = false)
    private int difficulty;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

}
