package com.withergate.api.model.notification;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "localized_texts")
@Getter
@Setter
public class LocalizedText {

    @Id
    @Column(name = "text_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "lang", nullable = false)
    private String lang;

    @Column(name = "text", nullable = false)
    private String text;

}
