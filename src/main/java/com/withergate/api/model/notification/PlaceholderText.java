package com.withergate.api.model.notification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Placeholder text. Used as a template for generating localized text messages.
 *
 * @author Martin Myslik
 */
@Entity
@Table(name = "placeholder_texts")
@Getter
@Setter
public class PlaceholderText {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "text_id", updatable = false, nullable = false)
    private int id;

    @Column(name = "code",nullable = false)
    private String code;

    @Column(name = "lang", updatable = false, nullable = false)
    private String lang;

    @Column(name = "text", nullable = false)
    private String text;
}
