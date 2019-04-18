package com.withergate.api.model.quest;

import com.withergate.api.model.notification.LocalizedText;

import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    private Type type;

    @Column(name = "information_level", nullable = false)
    private int informationLevel;

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

    public enum Type {
        COMBAT, INTELLECT, CRAFTSMANSHIP
    }

}