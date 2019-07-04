package com.withergate.api.model.item;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.withergate.api.model.Clan;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.withergate.api.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

/**
 * Item entity. Base class for all item types.
 *
 * @author Martin Myslik
 */
@MappedSuperclass
@Getter
@Setter
public abstract class Item {

    @ManyToOne
    @JoinColumn(name = "clan_id")
    @JsonIgnore
    private Clan clan;

    public abstract Map<String, LocalizedText> getName();

}
