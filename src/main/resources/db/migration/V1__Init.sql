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
    arena BIT NOT NULL DEFAULT FALSE,
    PRIMARY KEY (clan_id)
);

DROP TABLE IF EXISTS weapon_details;
CREATE TABLE weapon_details (
    item_name VARCHAR(16) NOT NULL,
    description VARCHAR(256) NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    rarity VARCHAR(8) NOT NULL,
    combat INT NOT NULL,
    weapon_type VARCHAR(8) NOT NULL,
    PRIMARY KEY (item_name)
);

DROP TABLE IF EXISTS weapons;
CREATE TABLE weapons (
    weapon_id INT AUTO_INCREMENT,
    item_name VARCHAR(16),
    clan_id INT DEFAULT NULL,
    PRIMARY KEY (weapon_id),
    CONSTRAINT weapon_weapon_details_fk FOREIGN KEY (item_name) REFERENCES weapon_details (item_name),
    CONSTRAINT weapon_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id)
);

DROP TABLE IF EXISTS consumable_details;
CREATE TABLE consumable_details (
    item_name VARCHAR(16) NOT NULL,
    description VARCHAR(256) NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    rarity VARCHAR(8) NOT NULL,
    effect_type VARCHAR(8) NOT NULL,
    effect INT NOT NULL,
    PRIMARY KEY (item_name)
);

DROP TABLE IF EXISTS consumables;
CREATE TABLE consumables (
    consumable_id INT AUTO_INCREMENT,
    item_name VARCHAR(16),
    clan_id INT DEFAULT NULL,
    PRIMARY KEY (consumable_id),
    CONSTRAINT consumable_consumable_details_fk FOREIGN KEY (item_name) REFERENCES consumable_details (item_name),
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

DROP TABLE IF EXISTS location_descriptions;
CREATE TABLE location_descriptions (
    location VARCHAR(16) NOT NULL,
    description VARCHAR(1024) NOT NULL,
    info VARCHAR(256) NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    PRIMARY KEY (location)
);

DROP TABLE IF EXISTS clan_notifications;
CREATE TABLE clan_notifications (
    notification_id BIGINT AUTO_INCREMENT,
    clan_id INT NOT NULL,
    turn_id INT NOT NULL,
    text VARCHAR(256) NOT NULL,
    result VARCHAR(256),
    details VARCHAR(128),
    item_income VARCHAR(128),
    character_income VARCHAR(128),
    junk_income INT,
    caps_income INT,
    fame_income INT,
    injury INT,
    healing INT,
    PRIMARY KEY (notification_id),
    CONSTRAINT p_notification_turn_fk FOREIGN KEY (turn_id) REFERENCES turns (turn_id),
    CONSTRAINT p_notification_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id)
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
