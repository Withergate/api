package com.withergate.api.game.model.notification;

import java.util.HashMap;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.withergate.api.game.model.encounter.SolutionType;
import lombok.Getter;
import lombok.Setter;

/**
 * Notification detail. Used only in scope of clan notifications.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "notification_details")
@Getter
@Setter
public class NotificationDetail {

    @Id
    @Column(name = "detail_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "notification_detail_id")
    private Map<String, LocalizedText> text;

    @Column(name = "solution_type", updatable = false)
    @Enumerated(EnumType.STRING)
    private SolutionType solutionType;

    @OneToOne(mappedBy = "detail", cascade = CascadeType.ALL)
    private NotificationCombatRound combatRound;

    /**
     * Default constructor.
     */
    public NotificationDetail() {
        if (text == null) text = new HashMap<>();
    }

    /**
     * Constructor for duplicating details. Needed to save entity properly with Hibernate mappings.
     *
     * @param detail the detail to be duplicated
     */
    public NotificationDetail(NotificationDetail detail) {
        text = new HashMap<>();

        // copying notification detail into multiple notifications requires de-referencing all values explicitly
        for (Map.Entry<String, LocalizedText> entry : detail.getText().entrySet()) {
            LocalizedText loc = new LocalizedText();
            loc.setText(entry.getValue().getText());
            loc.setLang(entry.getValue().getLang());
            text.put(entry.getKey(), loc);
        }

        if (detail.getCombatRound() != null) {
            combatRound = new NotificationCombatRound(detail.getCombatRound(), this);
        }
    }

}
