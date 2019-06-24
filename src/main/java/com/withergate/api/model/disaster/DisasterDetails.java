package com.withergate.api.model.disaster;

import com.withergate.api.model.notification.LocalizedText;

import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Disaster details.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "disaster_details")
@Getter
@Setter
public class DisasterDetails {

    @Id
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "disaster_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "disaster_description")
    private Map<String, LocalizedText> description;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "disaster_success_text")
    private Map<String, LocalizedText> successText;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "disaster_partial_success_text")
    private Map<String, LocalizedText> partialSuccessText;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "disaster_failure_text")
    private Map<String, LocalizedText> failureText;

    @Column(name = "fame_reward", nullable = false)
    private int fameReward;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "disaster", cascade = CascadeType.ALL)
    private Set<DisasterPenalty> penalties;

}
