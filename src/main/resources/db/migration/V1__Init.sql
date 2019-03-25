DROP TABLE IF EXISTS turns;
CREATE TABLE turns (
    turn_id INT,
    PRIMARY KEY (turn_id)
);

DROP TABLE IF EXISTS avatars;
CREATE TABLE avatars (
    avatar_id INT AUTO_INCREMENT,
    image_url VARCHAR(256) NOT NULL,
    gender VARCHAR(8) NOT NULL,
    PRIMARY KEY (avatar_id)
);

DROP TABLE IF EXISTS clans;
CREATE TABLE clans (
    clan_id INT,
    clan_name VARCHAR(16) UNIQUE NOT NULL,
    fame INT NOT NULL,
    caps INT NOT NULL,
    junk INT NOT NULL,
    food INT NOT NULL,
    arena BIT NOT NULL DEFAULT FALSE,
    PRIMARY KEY (clan_id)
);

DROP TABLE IF EXISTS building_details;
CREATE TABLE building_details (
    identifier VARCHAR(16) UNIQUE NOT NULL,
    building_name VARCHAR(64) NOT NULL,
    description VARCHAR(64) NOT NULL,
    info VARCHAR(64) NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    cost INT NOT NULL,
    visitable BIT NOT NULL,
    PRIMARY KEY (identifier)
);

DROP TABLE IF EXISTS buildings;
CREATE TABLE buildings (
    building_id INT AUTO_INCREMENT,
    identifier VARCHAR(16),
    level INT NOT NULL DEFAULT 0,
    progress INT NOT NULL DEFAULT 0,
    clan_id INT NOT NULL,
    PRIMARY KEY (building_id),
    CONSTRAINT building_building_details_fk FOREIGN KEY (identifier) REFERENCES building_details (identifier),
    CONSTRAINT building_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id)
);

DROP TABLE IF EXISTS weapon_details;
CREATE TABLE weapon_details (
    item_identifier VARCHAR(16) NOT NULL,
    item_name VARCHAR(64) NOT NULL,
    description VARCHAR(256) NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    rarity VARCHAR(8) NOT NULL,
    combat INT NOT NULL,
    weapon_type VARCHAR(8) NOT NULL,
    PRIMARY KEY (item_identifier)
);

DROP TABLE IF EXISTS weapons;
CREATE TABLE weapons (
    weapon_id INT AUTO_INCREMENT,
    item_identifier VARCHAR(16),
    clan_id INT DEFAULT NULL,
    PRIMARY KEY (weapon_id),
    CONSTRAINT weapon_weapon_details_fk FOREIGN KEY (item_identifier) REFERENCES weapon_details (item_identifier),
    CONSTRAINT weapon_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id)
);

DROP TABLE IF EXISTS consumable_details;
CREATE TABLE consumable_details (
    item_identifier VARCHAR(16) NOT NULL,
    item_name VARCHAR(64) NOT NULL,
    description VARCHAR(256) NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    rarity VARCHAR(8) NOT NULL,
    effect_type VARCHAR(8) NOT NULL,
    effect INT NOT NULL,
    PRIMARY KEY (item_identifier)
);

DROP TABLE IF EXISTS consumables;
CREATE TABLE consumables (
    consumable_id INT AUTO_INCREMENT,
    item_identifier VARCHAR(16),
    clan_id INT DEFAULT NULL,
    PRIMARY KEY (consumable_id),
    CONSTRAINT consumable_consumable_details_fk FOREIGN KEY (item_identifier) REFERENCES consumable_details (item_identifier),
    CONSTRAINT consumable_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id)
);

DROP TABLE IF EXISTS characters;
CREATE TABLE characters (
    character_id INT AUTO_INCREMENT,
    clan_id INT NOT NULL,
    character_name VARCHAR(32) NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    gender VARCHAR(8) NOT NULL,
    state VARCHAR(8) NOT NULL,
    level INT NOT NULL DEFaULT 1,
    experience INT NOT NULL,
    hitpoints INT NOT NULL,
    max_hitpoints INT NOT NULL,
    combat INT NOT NULL,
    scavenge INT NOT NULL,
    craftsmanship INT NOT NULL,
    intellect INT NOT NULL,
    weapon_id INT DEFAULT NULL,
    PRIMARY KEY (character_id),
    CONSTRAINT character_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id),
    CONSTRAINT character_weapon_fk FOREIGN KEY (weapon_id) REFERENCES weapons (weapon_id)
);

DROP TABLE IF EXISTS trait_details;
CREATE TABLE trait_details (
    identifier VARCHAR(16) UNIQUE NOT NULL,
    trait_name VARCHAR(64) NOT NULL,
    description VARCHAR(64) NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    PRIMARY KEY (identifier)
);

DROP TABLE IF EXISTS traits;
CREATE TABLE traits (
    trait_id INT AUTO_INCREMENT,
    identifier VARCHAR(16),
    character_id INT,
    PRIMARY KEY (trait_id),
    CONSTRAINT trait_trait_details_fk FOREIGN KEY (identifier) REFERENCES trait_details (identifier),
    CONSTRAINT trait_character_fk FOREIGN KEY (character_id) REFERENCES characters (character_id)
);

DROP TABLE IF EXISTS location_descriptions;
CREATE TABLE location_descriptions (
    location VARCHAR(16) NOT NULL,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(64) NOT NULL,
    info VARCHAR(64) NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    PRIMARY KEY (location)
);

DROP TABLE IF EXISTS placeholder_texts;
CREATE TABLE placeholder_texts (
    text_id INT AUTO_INCREMENT,
    code VARCHAR(64) NOT NULL,
    lang VARCHAR(4) NOT NULL,
    text VARCHAR(512) NOT NULL,
    PRIMARY KEY (text_id)
);

DROP TABLE IF EXISTS clan_notifications;
CREATE TABLE clan_notifications (
    notification_id BIGINT AUTO_INCREMENT,
    clan_id INT NOT NULL,
    turn_id INT NOT NULL,
    junk_income INT,
    food_income INT,
    caps_income INT,
    fame_income INT,
    injury INT,
    healing INT,
    experience INT,
    PRIMARY KEY (notification_id),
    CONSTRAINT p_notification_turn_fk FOREIGN KEY (turn_id) REFERENCES turns (turn_id),
    CONSTRAINT p_notification_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id)
);

DROP TABLE IF EXISTS notification_details;
CREATE TABLE notification_details (
    detail_id BIGINT AUTO_INCREMENT,
    notification_id BIGINT,
    PRIMARY KEY (detail_id),
    CONSTRAINT detail_notification_fk FOREIGN KEY (notification_id) REFERENCES clan_notifications (notification_id)
);

DROP TABLE IF EXISTS localized_texts;
CREATE TABLE localized_texts (
    text_id BIGINT AUTO_INCREMENT,
    lang VARCHAR(4) NOT NULL,
    text VARCHAR(1024) NOT NULL,
    notification_id BIGINT,
    notification_detail_id BIGINT,
    PRIMARY KEY (text_id),
    CONSTRAINT localized_text_notification_fk FOREIGN KEY (notification_id) REFERENCES clan_notifications (notification_id),
    CONSTRAINT localized_text_notification_detail_fk FOREIGN KEY (notification_detail_id) REFERENCES notification_details (detail_id)
);

DROP TABLE IF EXISTS encounters;
CREATE TABLE encounters (
    encounter_id INT AUTO_INCREMENT,
    location VARCHAR(16) NOT NULL,
    encounter_type VARCHAR(16) NOT NULL,
    reward_type VARCHAR(16) NOT NULL,
    penalty_type VARCHAR(16) NOT NULL,
    difficulty INT NOT NULL,
    description_text VARCHAR(512) NOT NULL,
    success_text VARCHAR(512) NOT NULL,
    failure_text VARCHAR(512) NOT NULL,
    PRIMARY KEY (encounter_id)
);

-- Actions
DROP TABLE IF EXISTS location_actions;
CREATE TABLE location_actions (
    action_id INT AUTO_INCREMENT,
    state VARCHAR(16) NOT NULL,
    character_id INT NOT NULL,
    location VARCHAR(16) NOT NULL,
    PRIMARY KEY (action_id)
);

DROP TABLE IF EXISTS building_actions;
CREATE TABLE building_actions (
    action_id INT AUTO_INCREMENT,
    state VARCHAR(16) NOT NULL,
    character_id INT NOT NULL,
    building VARCHAR(16) NOT NULL,
    action_type VARCHAR(16) NOT NULL,
    PRIMARY KEY (action_id)
);

DROP TABLE IF EXISTS name_prefixes;
CREATE TABLE name_prefixes (
    value VARCHAR(16) UNIQUE NOT NULL,
    PRIMARY KEY (value)
);

DROP TABLE IF EXISTS names;
CREATE TABLE names (
    gender VARCHAR(8) NOT NULL,
    value VARCHAR(16) UNIQUE NOT NULL,
    PRIMARY KEY (value)
);
