package com.withergate.api.model.disaster;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.model.notification.LocalizedText;
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

    // Notification text codes

    @JsonIgnore
    @Column(name = "success_text", updatable = false, nullable = false)
    private String successText;

    @JsonIgnore
    @Column(name = "partial_success_text", updatable = false, nullable = false)
    private String partialSuccessText;

    @JsonIgnore
    @Column(name = "failure_text", updatable = false, nullable = false)
    private String failureText;

    @Column(name = "fame_reward", nullable = false)
    private int fameReward;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "disaster", cascade = CascadeType.ALL)
    private List<DisasterPenalty> penalties;

    @OneToMany(mappedBy = "disaster", cascade = CascadeType.ALL)
    private List<DisasterSolution> solutions;

}
