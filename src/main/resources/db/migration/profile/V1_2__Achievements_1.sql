-- Achievements
DROP TABLE IF EXISTS achievement_details;
CREATE TABLE achievement_details (
    identifier VARCHAR(16) UNIQUE NOT NULL,
    achievement_type VARCHAR(32) NOT NULL,
    rarity VARCHAR(16) NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    value_number INT,
    value_string VARCHAR(32),
    PRIMARY KEY (identifier)
);

DROP TABLE IF EXISTS achievements;
CREATE TABLE achievements (
    achievement_id BIGINT AUTO_INCREMENT,
    identifier VARCHAR(16),
    profile_id INT NOT NULL,
    award_date TIMESTAMP NOT NULL,
    PRIMARY KEY (achievement_id),
    CONSTRAINT achievement_details_fk FOREIGN KEY (identifier) REFERENCES achievement_details (identifier),
    CONSTRAINT achievement_profile_fk FOREIGN KEY (profile_id) REFERENCES profiles (profile_id),
    CONSTRAINT achievement_unique UNIQUE (profile_id, identifier)
);

-- Localized texts
DROP TABLE IF EXISTS localized_texts;
CREATE TABLE localized_texts (
    text_id BIGINT AUTO_INCREMENT,
    lang VARCHAR(4) NOT NULL,
    text VARCHAR(2048) NOT NULL,
    achievement_name VARCHAR(16),
    achievement_description VARCHAR(16),
    PRIMARY KEY (text_id),
    CONSTRAINT localized_text_achievement_name_fk FOREIGN KEY (achievement_name) REFERENCES achievement_details (identifier),
    CONSTRAINT localized_text_achievement_description_fk FOREIGN KEY (achievement_description) REFERENCES achievement_details (identifier)
);

-- Achievements data
INSERT INTO achievement_details (identifier, achievement_type, rarity, value_number, value_string, image_url) VALUES
    ('DEDICATED', 'CONSECUTIVE_LOGINS', 'COMMON', 5, null, 'https://storage.googleapis.com/withergate-images/achievements/dedicated.png'),
    ('ON_FIRE', 'CONSECUTIVE_LOGINS', 'RARE', 21, null, 'https://storage.googleapis.com/withergate-images/achievements/on-fire.png'),
    ('SUPPORTER', 'PREMIUM', 'RARE', 0, 'SILVER', 'https://storage.googleapis.com/withergate-images/achievements/supporter.png'),
    ('PATRON', 'PREMIUM', 'EPIC', 0, 'GOLD', 'https://storage.googleapis.com/withergate-images/achievements/patron.png'),
    ('ARENA_NEWBIE', 'ARENA_WINS', 'COMMON', 1, null, 'https://storage.googleapis.com/withergate-images/achievements/arena-newbie.png'),
    ('ARENA_BADASS', 'ARENA_WINS', 'RARE', 7, null, 'https://storage.googleapis.com/withergate-images/achievements/arena-badass.png'),
    ('ARENA_PHANTOM', 'ARENA_WINS', 'EPIC', 20, null, 'https://storage.googleapis.com/withergate-images/achievements/arena-phantom.png'),
    ('BOB_BUILDER', 'BUILDING_ALL', 'COMMON', 0, null, 'https://storage.googleapis.com/withergate-images/achievements/bob-builder.png'),
    ('CAMPER', 'BUILDING_DEFENSE', 'COMMON', 4, null, 'https://storage.googleapis.com/withergate-images/achievements/camper.png'),
    ('BRICK_WALL', 'BUILDING_TOP', 'RARE', 6, null, 'https://storage.googleapis.com/withergate-images/achievements/brick-wall.png');

INSERT INTO localized_texts (achievement_name, lang, text) VALUES
    ('DEDICATED', 'en', 'Dedicated'),
    ('DEDICATED', 'cs', 'Odhodlaný'),
    ('ON_FIRE', 'en', 'On fire'),
    ('ON_FIRE', 'cs', 'Zapálený'),
    ('SUPPORTER', 'en', 'Supporter'),
    ('SUPPORTER', 'cs', 'Podporovatel'),
    ('PATRON', 'en', 'Patron'),
    ('PATRON', 'cs', 'Mecenáš'),
    ('ARENA_NEWBIE', 'en', 'Newbie'),
    ('ARENA_NEWBIE', 'cs', 'Nováček'),
    ('ARENA_BADASS', 'en', 'Arena Badass'),
    ('ARENA_BADASS', 'cs', 'Kruťák z arény'),
    ('ARENA_PHANTOM', 'en', 'Phantom of the Arena'),
    ('ARENA_PHANTOM', 'cs', 'Fantom Arény'),
    ('BOB_BUILDER', 'en', 'Bob the Builder'),
    ('BOB_BUILDER', 'cs', 'Bořek Stavitel'),
    ('CAMPER', 'en', 'Camper'),
    ('CAMPER', 'cs', 'Kemper'),
    ('BRICK_WALL', 'en', 'Another Brick in the Wall'),
    ('BRICK_WALL', 'cs', 'Another Brick in the Wall');

INSERT INTO localized_texts (achievement_description, lang, text) VALUES
    ('DEDICATED', 'en', 'Login to the game 5 days in a row.'),
    ('DEDICATED', 'cs', 'Přihlas se do hry 5 dnů v řadě.'),
    ('ON_FIRE', 'en', 'Login to the game 21 days in a row.'),
    ('ON_FIRE', 'cs', 'Přihlas se do hry 21 dnů v řadě.'),
    ('SUPPORTER', 'en', 'Get a Silver account. This can be done by contributing to the game development of via Patreon.'),
    ('SUPPORTER', 'cs', 'Získej Silver účet. Možné získat nefinanční podporou hry a nebo přes Patreon.'),
    ('PATRON', 'en', 'Get a Gold account. This can be done via Patreon.'),
    ('PATRON', 'cs', 'Získej Gold účet. Možné získat přes Patreon.'),
    ('ARENA_NEWBIE', 'en', 'Win at least one fight in the arena.'),
    ('ARENA_NEWBIE', 'cs', 'Vyhraj alespoň jeden zápas v aréně'),
    ('ARENA_BADASS', 'en', 'Win at least seven fight in the arena with a same character.'),
    ('ARENA_BADASS', 'cs', 'Vyhraj alespoň sedm zápasů v aréně se stejnou postavou.'),
    ('ARENA_PHANTOM', 'en', 'Win at least twenty fights in the arena with a same character.'),
    ('ARENA_PHANTOM', 'cs', 'Vyhraj alespoň dvacet zápasů v aréně se stejnou postavou.'),
    ('BOB_BUILDER', 'en', 'Build at least one level of every building.'),
    ('BOB_BUILDER', 'cs', 'Postav alespoň jednu úroveň od každé budovy.'),('CAMPER', 'en', 'Camper'),
    ('CAMPER', 'en', 'Build at least four levels of a defense building.'),
    ('CAMPER', 'cs', 'Postav svou obrannou budovu alespoň na čtvrtou úroveň.'),
    ('BRICK_WALL', 'en', 'Build at least six levels of a single building.'),
    ('BRICK_WALL', 'cs', 'Postav alespoň jednu ze svých budov na šestou úroveň.');