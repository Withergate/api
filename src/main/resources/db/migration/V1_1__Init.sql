DROP TABLE IF EXISTS turns;
CREATE TABLE turns (
    turn_id INT,
    start_date DATE DEFAULT NULL,
    PRIMARY KEY (turn_id)
);

DROP TABLE IF EXISTS avatars;
CREATE TABLE avatars (
    avatar_id INT AUTO_INCREMENT,
    image_url VARCHAR(256) NOT NULL,
    gender VARCHAR(8) NOT NULL,
    PRIMARY KEY (avatar_id)
);

-- Factions
DROP TABLE IF EXISTS factions;
CREATE TABLE factions (
    identifier VARCHAR(16),
    faction_points INT DEFAULT 0,
    image_url VARCHAR(256) NOT NULL,
    icon_url VARCHAR(256) NOT NULL,
    PRIMARY KEY (identifier)
);

DROP TABLE IF EXISTS faction_aids;
CREATE TABLE faction_aids (
    identifier VARCHAR(16) NOT NULL,
    aid_type VARCHAR(32) NOT NULL,
    fame INT NOT NULL DEFAULT 0,
    faction_points INT NOT NULL DEFAULT 0,
    cost INT NOT NULL DEFAULT 0,
    aid INT NOT NULL DEFAULT 0,
    num_aid INT NOT NULL DEFAULT 0,
    health_cost BIT DEFAULT 0,
    item_cost BIT DEFAULT 0,
    faction VARCHAR(16) NOT NULL,
    PRIMARY KEY (identifier),
    CONSTRAINT faction_faction_solution_fk FOREIGN KEY (faction) REFERENCES factions (identifier)
);

-- Clans
DROP TABLE IF EXISTS clans;
CREATE TABLE clans (
    clan_id INT,
    clan_name VARCHAR(32) UNIQUE NOT NULL,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fame INT NOT NULL,
    caps INT NOT NULL,
    junk INT NOT NULL,
    food INT NOT NULL,
    information INT NOT NULL,
    information_level INT NOT NULL,
    disaster_progress INT NOT NULL DEFAULT 0,
    default_action VARCHAR(32) NOT NULL,
    prefer_disaster BIT DEFAULT 1,
    faction VARCHAR(16),
    faction_points INT DEFAULT 0,
    CONSTRAINT clan_faction_fk FOREIGN KEY (faction) REFERENCES factions (identifier),
    PRIMARY KEY (clan_id)
);

-- Buildings
DROP TABLE IF EXISTS building_details;
CREATE TABLE building_details (
    identifier VARCHAR(16) UNIQUE NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    cost INT NOT NULL,
    visitable BIT NOT NULL,
    visit_junk_cost INT DEFAULT 0,
    bonus INT DEFAULT 0,
    bonus_type VARCHAR(32),
    bonus_text VARCHAR(32),
    item_type VARCHAR(32),
    end_bonus INT DEFAULT 0,
    end_bonus_type VARCHAR(32),
    end_bonus_text VARCHAR(32),
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

-- Research
DROP TABLE IF EXISTS research_details;
CREATE TABLE research_details (
    identifier VARCHAR(16) UNIQUE NOT NULL,
    bonus_type VARCHAR(32),
    bonus_text VARCHAR(32),
    image_url VARCHAR(256) NOT NULL,
    value INT NOT NULL,
    information_level INT NOT NULL,
    cost INT NOT NULL,
    fame INT NOT NULL,
    PRIMARY KEY (identifier)
);

DROP TABLE IF EXISTS research;
CREATE TABLE research (
    research_id INT AUTO_INCREMENT,
    identifier VARCHAR(16),
    progress INT NOT NULL DEFAULT 0,
    completed BIT NOT NULL,
    clan_id INT NOT NULL,
    PRIMARY KEY (research_id),
    CONSTRAINT research_details_fk FOREIGN KEY (identifier) REFERENCES research_details (identifier),
    CONSTRAINT research_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id)
);

-- Characters
DROP TABLE IF EXISTS characters;
CREATE TABLE characters (
    character_id INT AUTO_INCREMENT,
    clan_id INT,
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
    skill_points INT NOT NULL,
    PRIMARY KEY (character_id),
    CONSTRAINT character_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id)
);

-- Items
DROP TABLE IF EXISTS item_details;
CREATE TABLE item_details (
    identifier VARCHAR(16) NOT NULL,
    item_type VARCHAR(16) NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    rarity VARCHAR(8) NOT NULL,
    price INT NOT NULL,
    prereq INT DEFAULT 0,
    combat INT DEFAULT 0,
    bonus INT DEFAULT 0,
    bonus_type VARCHAR(32),
    bonus_text VARCHAR(32),
    effect_type VARCHAR(32),
    weapon_type VARCHAR(16),
    PRIMARY KEY (identifier)
);

DROP TABLE IF EXISTS items;
CREATE TABLE items (
    item_id INT AUTO_INCREMENT,
    identifier VARCHAR(16),
    clan_id INT DEFAULT NULL,
    character_id INT DEFAULT NULL,
    PRIMARY KEY (item_id),
    CONSTRAINT item_item_details_fk FOREIGN KEY (identifier) REFERENCES item_details (identifier),
    CONSTRAINT item_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id),
    CONSTRAINT item_character_fk FOREIGN KEY (character_id) REFERENCES characters (character_id)
);

-- Traits
DROP TABLE IF EXISTS trait_details;
CREATE TABLE trait_details (
    identifier VARCHAR(16) UNIQUE NOT NULL,
    bonus INT,
    bonus_type VARCHAR(32),
    bonus_text VARCHAR(32),
    optional BIT DEFAULT 0,
    image_url VARCHAR(256) NOT NULL,
    PRIMARY KEY (identifier)
);

DROP TABLE IF EXISTS traits;
CREATE TABLE traits (
    trait_id INT AUTO_INCREMENT,
    trait_order INT,
    active BIT,
    identifier VARCHAR(16),
    character_id INT,
    PRIMARY KEY (trait_id),
    CONSTRAINT trait_trait_details_fk FOREIGN KEY (identifier) REFERENCES trait_details (identifier),
    CONSTRAINT trait_character_fk FOREIGN KEY (character_id) REFERENCES characters (character_id)
);

-- Locations
DROP TABLE IF EXISTS location_descriptions;
CREATE TABLE location_descriptions (
    location VARCHAR(16) NOT NULL,
    scouting BIT NOT NULL DEFAULT FALSE,
    food_bonus INT DEFAULT 0,
    junk_bonus INT DEFAULT 0,
    information_bonus INT DEFAULT 0,
    encounter_chance INT DEFAULT 0,
    item_chance INT DEFAULT 0,
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
    text VARCHAR(1024) NOT NULL,
    PRIMARY KEY (text_id)
);

-- Notifications
DROP TABLE IF EXISTS clan_notifications;
CREATE TABLE clan_notifications (
    notification_id BIGINT AUTO_INCREMENT,
    clan_id INT NOT NULL,
    turn_id INT NOT NULL,
    header VARCHAR(64),
    junk_income INT,
    food_income INT,
    caps_income INT,
    fame_income INT,
    injury INT,
    healing INT,
    experience INT,
    information INT,
    item BIT DEFAULT 0,
    faction_points INT,
    death BIT DEFAULT 0,
    image_url VARCHAR(256) DEFAULT NULL,
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

-- Encounters
DROP TABLE IF EXISTS encounters;
CREATE TABLE encounters (
    encounter_id INT AUTO_INCREMENT,
    location VARCHAR(16) NOT NULL,
    encounter_type VARCHAR(16) NOT NULL,
    reward_type VARCHAR(16) NOT NULL,
    penalty_type VARCHAR(16) NOT NULL,
    difficulty INT NOT NULL,
    description_text VARCHAR(64) NOT NULL,
    success_text VARCHAR(64) NOT NULL,
    failure_text VARCHAR(64) NOT NULL,
    PRIMARY KEY (encounter_id)
);

-- Quests
DROP TABLE IF EXISTS quest_details;
CREATE TABLE quest_details (
    identifier VARCHAR(16),
    quest_type VARCHAR(32) NOT NULL,
    quest_condition VARCHAR(32),
    difficulty INT NOT NULL,
    completion INT NOT NULL,
    caps_reward INT DEFAULT 0,
    fame_reward INT NOT NULL,
    faction_reward INT DEFAULT 0,
    food_cost INT DEFAULT 0,
    junk_cost INT DEFAULT 0,
    item_cost BIT DEFAULT 0,
    health_cost BIT DEFAULT 0,
    follow_up VARCHAR(32),
    faction VARCHAR(16),
    faction_specific BIT DEFAULT 0,
    image_url VARCHAR(256) NOT NULL,
    PRIMARY KEY (identifier)
);

DROP TABLE IF EXISTS quests;
CREATE TABLE quests (
    quest_id INT AUTO_INCREMENT,
    identifier VARCHAR(16),
    progress INT NOT NULL DEFAULT 0,
    completed BIT NOT NULL DEFAULT 0,
    clan_id INT NOT NULL,
    PRIMARY KEY (quest_id),
    CONSTRAINT quest_quest_details_fk FOREIGN KEY (identifier) REFERENCES quest_details (identifier),
    CONSTRAINT quest_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id)
);

-- Disasters
DROP TABLE IF EXISTS disaster_details;
CREATE TABLE disaster_details (
    identifier VARCHAR(32),
    final_disaster BIT DEFAULT 0,
    fame_reward INT NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    success_text VARCHAR(32),
    partial_success_text VARCHAR(32),
    failure_text VARCHAR(32),
    PRIMARY KEY (identifier)
);

DROP TABLE IF EXISTS disaster_penalties;
CREATE TABLE disaster_penalties (
    identifier VARCHAR(32),
    penalty_type VARCHAR(32) NOT NULL,
    disaster VARCHAR(32) NOT NULL,
    PRIMARY KEY (identifier),
    CONSTRAINT disaster_disaster_penalty_fk FOREIGN KEY (disaster) REFERENCES disaster_details (identifier)
);

DROP TABLE IF EXISTS disaster_solutions;
CREATE TABLE disaster_solutions (
    identifier VARCHAR(32),
    basic BIT DEFAULT 0,
    solution_type VARCHAR(32) NOT NULL,
    disaster_condition VARCHAR(32),
    difficulty INT NOT NULL DEFAULT 0,
    bonus INT NOT NULL DEFAULT 0,
    junk_cost INT NOT NULL DEFAULT 0,
    caps_cost INT NOT NULL DEFAULT 0,
    food_cost INT NOT NULL DEFAULT 0,
    item_cost BIT DEFAULT 0,
    disaster VARCHAR(32) NOT NULL,
    PRIMARY KEY (identifier),
    CONSTRAINT disaster_disaster_solution_fk FOREIGN KEY (disaster) REFERENCES disaster_details (identifier)
);

DROP TABLE IF EXISTS disasters;
CREATE TABLE disasters (
    disaster_id INT AUTO_INCREMENT,
    identifier VARCHAR(32),
    completed BIT NOT NULL DEFAULT 0,
    turn INT NOT NULL,
    PRIMARY KEY (disaster_id),
    CONSTRAINT disaster_disaster_details_fk FOREIGN KEY (identifier) REFERENCES disaster_details (identifier)
);

-- Global notification
DROP TABLE IF EXISTS global_notification;
CREATE TABLE global_notification (
    single_id VARCHAR(8) NOT NULL,
    active BIT DEFAULT 0,
    PRIMARY KEY (single_id)
);

-- Localized texts
DROP TABLE IF EXISTS localized_texts;
CREATE TABLE localized_texts (
    text_id BIGINT AUTO_INCREMENT,
    lang VARCHAR(4) NOT NULL,
    text VARCHAR(2048) NOT NULL,
    notification_id BIGINT,
    notification_detail_id BIGINT,
    global_notification VARCHAR(16),
    location_name VARCHAR(16),
    location_description VARCHAR(16),
    location_info VARCHAR(16),
    trait_name VARCHAR(16),
    trait_description VARCHAR(16),
    building_name VARCHAR(16),
    building_description VARCHAR(16),
    building_info VARCHAR(16),
    research_name VARCHAR(16),
    research_description VARCHAR(16),
    research_info VARCHAR(16),
    item_name VARCHAR(16),
    item_description VARCHAR(16),
    quest_name VARCHAR(16),
    quest_description VARCHAR(16),
    disaster_name VARCHAR(16),
    disaster_description VARCHAR(16),
    disaster_solution_name VARCHAR(16),
    disaster_solution_description VARCHAR(16),
    faction_name VARCHAR(16),
    faction_description VARCHAR(16),
    faction_aid VARCHAR(16),
    PRIMARY KEY (text_id),
    CONSTRAINT localized_text_notification_fk FOREIGN KEY (notification_id) REFERENCES clan_notifications (notification_id),
    CONSTRAINT localized_text_notification_detail_fk FOREIGN KEY (notification_detail_id) REFERENCES notification_details (detail_id),
    CONSTRAINT localized_text_global_notification_fk FOREIGN KEY (global_notification) REFERENCES global_notification (single_id),
    CONSTRAINT localized_text_location_name_fk FOREIGN KEY (location_name) REFERENCES location_descriptions (location),
    CONSTRAINT localized_text_location_description_fk FOREIGN KEY (location_description) REFERENCES location_descriptions (location),
    CONSTRAINT localized_text_location_info_fk FOREIGN KEY (location_info) REFERENCES location_descriptions (location),
    CONSTRAINT localized_text_trait_name_fk FOREIGN KEY (trait_name) REFERENCES trait_details (identifier),
    CONSTRAINT localized_text_trait_description_fk FOREIGN KEY (trait_description) REFERENCES trait_details (identifier),
    CONSTRAINT localized_text_building_name_fk FOREIGN KEY (building_name) REFERENCES building_details (identifier),
    CONSTRAINT localized_text_building_description_fk FOREIGN KEY (building_description) REFERENCES building_details (identifier),
    CONSTRAINT localized_text_building_info_fk FOREIGN KEY (building_info) REFERENCES building_details (identifier),
    CONSTRAINT localized_text_research_name_fk FOREIGN KEY (research_name) REFERENCES research_details (identifier),
    CONSTRAINT localized_text_research_description_fk FOREIGN KEY (research_description) REFERENCES research_details (identifier),
    CONSTRAINT localized_text_research_info_fk FOREIGN KEY (research_info) REFERENCES research_details (identifier),
    CONSTRAINT localized_text_item_name_fk FOREIGN KEY (item_name) REFERENCES item_details (identifier),
    CONSTRAINT localized_text_item_description_fk FOREIGN KEY (item_description) REFERENCES item_details (identifier),
    CONSTRAINT localized_text_quest_name_fk FOREIGN KEY (quest_name) REFERENCES quest_details (identifier),
    CONSTRAINT localized_text_quest_description_fk FOREIGN KEY (quest_description) REFERENCES quest_details (identifier),
    CONSTRAINT localized_text_disaster_name_fk FOREIGN KEY (disaster_name) REFERENCES disaster_details (identifier),
    CONSTRAINT localized_text_disaster_description_fk FOREIGN KEY (disaster_description) REFERENCES disaster_details (identifier),
    CONSTRAINT localized_text_disaster_solution_name_fk FOREIGN KEY (disaster_solution_name) REFERENCES disaster_solutions (identifier),
    CONSTRAINT localized_text_disaster_solution_description_fk FOREIGN KEY (disaster_solution_description) REFERENCES disaster_solutions (identifier),
    CONSTRAINT localized_text_faction_name_fk FOREIGN KEY (faction_name) REFERENCES factions (identifier),
    CONSTRAINT localized_text_faction_description_fk FOREIGN KEY (faction_description) REFERENCES factions (identifier),
    CONSTRAINT localized_text_faction_aid_fk FOREIGN KEY (faction_aid) REFERENCES faction_aids (identifier)
);

-- Notification bootstrap
INSERT INTO global_notification VALUES ('SINGLE', false);
INSERT INTO localized_texts (global_notification, lang, text) VALUES
    ('SINGLE', 'en', ''),
    ('SINGLE', 'cs', '');

-- Market offers
DROP TABLE IF EXISTS market_offers;
CREATE TABLE market_offers (
    offer_id INT AUTO_INCREMENT,
    state VARCHAR(16) NOT NULL,
    seller_id INT,
    buyer_id INT,
    item_id INT NOT NULL,
    price INT NOT NULL,
    identifier VARCHAR(16) NOT NULL,
    CONSTRAINT offer_seller_fk FOREIGN KEY (seller_id) REFERENCES clans (clan_id),
    CONSTRAINT offer_buyer_fk FOREIGN KEY (buyer_id) REFERENCES clans (clan_id),
    CONSTRAINT offer_identifier_fk FOREIGN KEY (identifier) REFERENCES item_details (identifier),
    PRIMARY KEY (offer_id)
);

-- Tavern offers
DROP TABLE IF EXISTS tavern_offers;
CREATE TABLE tavern_offers (
    offer_id INT AUTO_INCREMENT,
    state VARCHAR(16) NOT NULL,
    character_id INT NOT NULL,
    price INT NOT NULL,
    clan_id INT NOT NULL,
    CONSTRAINT tavern_offer_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id),
    CONSTRAINT tavern_character_identifier_fk FOREIGN KEY (character_id) REFERENCES characters (character_id),
    PRIMARY KEY (offer_id)
);

-- Actions
DROP TABLE IF EXISTS actions;
CREATE TABLE actions (
    dtype VARCHAR(256),
    action_id INT AUTO_INCREMENT,
    state VARCHAR(16) NOT NULL,
    character_id INT NOT NULL,
    location VARCHAR(16),
    action_type VARCHAR(16),
    offer_id INT,
    building VARCHAR(16),
    research VARCHAR(16),
    quest_id INT,
    trade_type VARCHAR(16),
    food INT DEFAULT 0,
    junk INT DEFAULT 0,
    disaster_solution VARCHAR(32),
    faction VARCHAR(16),
    faction_aid VARCHAR(16),
    clan_id INT,
    PRIMARY KEY (action_id)
);

-- Names
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

-- Arena stats
DROP TABLE IF EXISTS arena_stats;
CREATE TABLE arena_stats (
    stats_id INT NOT NULL,
    stats INT NOT NULL,
    character_name VARCHAR(32) NOT NULL,
    clan_name VARCHAR(32) NOT NULL,
    PRIMARY KEY (stats_id)
);

-- Statistics
DROP TABLE IF EXISTS clan_turn_statistics;
CREATE TABLE clan_turn_statistics (
    statistics_id INT AUTO_INCREMENT,
    clan_id INT NOT NULL,
    turn_id INT NOT NULL,
    fame INT,
    food INT,
    caps INT,
    junk INT,
    buildings INT,
    research INT,
    quests INT,
    PRIMARY KEY (statistics_id)
);
