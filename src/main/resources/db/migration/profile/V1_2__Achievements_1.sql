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
    ('PATRON', 'PREMIUM', 'EPIC', 0, 'GOLD', 'https://storage.googleapis.com/withergate-images/achievements/patron.png');

INSERT INTO localized_texts (achievement_name, lang, text) VALUES
    ('DEDICATED', 'en', 'Dedicated'),
    ('DEDICATED', 'en', 'Odhodlaný'),
    ('ON_FIRE', 'en', 'On fire'),
    ('ON_FIRE', 'en', 'Zapálený'),
    ('SUPPORTER', 'en', 'Supporter'),
    ('SUPPORTER', 'en', 'Podporovatel'),
    ('PATRON', 'en', 'Patron'),
    ('PATRON', 'en', 'Mecenáš');

INSERT INTO localized_texts (achievement_description, lang, text) VALUES
    ('DEDICATED', 'en', 'Login to the game 5 days in a row.'),
    ('DEDICATED', 'en', 'Přihlas se do hry 5 dnů v řadě.'),
    ('ON_FIRE', 'en', 'Login to the game 21 days in a row.'),
    ('ON_FIRE', 'en', 'Přihlas se do hry 21 dnů v řadě.'),
    ('SUPPORTER', 'en', 'Get a Silver account. This can be done by contributing to the game development of via Patreon.'),
    ('SUPPORTER', 'en', 'Získej Silver účet. Možné získat nefinanční podporou hry a nebo přes Patreon.'),
    ('PATRON', 'en', 'Get a Gold account. This can be done via Patreon.'),
    ('PATRON', 'en', 'Získej Gold účet. Možné získat přes Patreon.');