package com.withergate.api.game.model.item;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

import com.withergate.api.game.model.type.BonusType;
import com.withergate.api.game.model.notification.LocalizedText;
import lombok.Getter;
import lombok.Setter;

/**
 * ItemDetails entity. Base class for all item details types.
 *
 * @author Martin Myslik
 */
@Entity(name = "item_details")
@DiscriminatorColumn(name = "item_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
public class ItemDetails {

    @Id
    @Column(name = "identifier", updatable = false, nullable = false)
    private String identifier;

    @Column(name = "price", updatable = false, nullable = false)
    private int price;

    @Column(name = "rarity", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private Rarity rarity;

    @Column(name = "item_type", updatable = false, nullable = false, insertable = false)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "item_name")
    private Map<String, LocalizedText> name;

    @OneToMany(cascade = CascadeType.ALL)
    @MapKeyColumn(name = "lang")
    @JoinColumn(name = "item_description")
    private Map<String, LocalizedText> description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "combat", updatable = false)
    private int combat;

    @Column(name = "bonus", updatable = false)
    private int bonus;

    @Column(name = "bonus_type", updatable = false)
    @Enumerated(EnumType.STRING)
    private BonusType bonusType;

    @Column(name = "bonus_text", updatable = false)
    private String bonusText;

    @Column(name = "prereq", updatable = false)
    private int prereq;

    @Column(name = "effect_type", updatable = false)
    @Enumerated(EnumType.STRING)
    private EffectType effectType;

    @Column(name = "weapon_type", updatable = false)
    @Enumerated(EnumType.STRING)
    private WeaponType weaponType;

    @Column(name = "crafting_cost", updatable = false)
    private int craftingCost;

    @Column(name = "crafting_level", updatable = false)
    private int craftingLevel;

    /**
     * Weapon type.
     */
    public enum WeaponType {
        MELEE, RANGED
    }

    /**
     * Item rarity.
     */
    public enum Rarity {
        COMMON, RARE, EPIC
    }

}
