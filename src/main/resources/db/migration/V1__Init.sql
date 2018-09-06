DROP TABLE IF EXISTS turns;
CREATE TABLE turns (
    turn_id INT,
    PRIMARY KEY (turn_id)
);

DROP TABLE IF EXISTS clans;
CREATE TABLE clans (
    clan_id INT,
    clan_name VARCHAR(16) UNIQUE NOT NULL,
    caps INT NOT NULL,
    junk INT NOT NULL,
    PRIMARY KEY (clan_id)
);

DROP TABLE IF EXISTS weapon_details;
CREATE TABLE weapon_details (
    weapon_name VARCHAR(16) NOT NULL,
    description VARCHAR(256) NOT NULL,
    combat INT NOT NULL,
    weapon_type VARCHAR(8) NOT NULL,
    PRIMARY KEY (weapon_name)
);

DROP TABLE IF EXISTS weapons;
CREATE TABLE weapons (
    weapon_id INT AUTO_INCREMENT,
    weapon_name VARCHAR(16),
    clan_id INT DEFAULT NULL,
    PRIMARY KEY (weapon_id),
    CONSTRAINT weapon_weapon_details_fk FOREIGN KEY (weapon_name) REFERENCES weapon_details (weapon_name),
    CONSTRAINT weapon_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id)
);

DROP TABLE IF EXISTS characters;
CREATE TABLE characters (
    character_id INT AUTO_INCREMENT,
    clan_id INT NOT NULL,
    character_name VARCHAR(32) NOT NULL,
    gender VARCHAR(8) NOT NULL,
    state VARCHAR(8) NOT NULL,
    combat INT NOT NULL,
    scavenge INT NOT NULL,
    craftsmanship INT NOT NULL,
    charm INT NOT NULL,
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
    image_url VARCHAR(256) NOT NULL,
    PRIMARY KEY (location)
);

DROP TABLE IF EXISTS clan_notifications;
CREATE TABLE clan_notifications (
    notification_id BIGINT AUTO_INCREMENT,
    clan_id INT NOT NULL,
    turn_id INT NOT NULL,
    text VARCHAR(256) NOT NULL,
    details VARCHAR(256),
    PRIMARY KEY (notification_id),
    CONSTRAINT p_notification_turn_fk FOREIGN KEY (turn_id) REFERENCES turns (turn_id),
    CONSTRAINT p_notification_clan_fk FOREIGN KEY (clan_id) REFERENCES clans (clan_id)
);

DROP TABLE IF EXISTS encounters;
CREATE TABLE encounters (
    encounter_id INT AUTO_INCREMENT,
    encounter_type VARCHAR(16) NOT NULL,
    reward_type VARCHAR(16) NOT NULL,
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
    PRIMARY KEY (action_id),
    CONSTRAINT character_exp_action_fk FOREIGN KEY (character_id) REFERENCES characters (character_id)
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

-- First turn
INSERT INTO turns (turn_id) VALUES
    (1);

-- Weapon details
INSERT INTO weapon_details(weapon_name, description, weapon_type, combat) VALUES
    ('Kitchen knife', 'Rusty kitchen knife.', 'MELEE', 1),
    ('Glock', 'Hand gun.', 'RANGED', 4);

-- Location descriptions
INSERT INTO location_descriptions(location, description, image_url) VALUES
    ('NEIGHBORHOOD', 'Neighborhood is the area around your camp. It is a relatively safe place since you have been living there for quite some time. It is a great place to search for junk and some low-value items. Do not expect to find anything too valuable, though.',
    'https://image.ibb.co/gcR9Xz/vault.jpg'),
    ('WASTELAND', 'Wasteland is the desolated area all around you. It might seem abandonded but do not be mistaken. Other characters roam this area so searching this area can sometimes be dangerous.',
    'https://image.ibb.co/dxwXkK/wasteland.jpg'),
    ('CITY', 'The ruins of the destroyed city hides the most valuable treasures. But keep in mind that many scavengers and wastelanders go there in a hope for better life. That means you are most likely to encounter enemy characters in this location. On the other hand, if you are lucky, you can find some useful items here.',
    'https://image.ibb.co/jVgMee/city.jpg');

-- Random encounters
INSERT INTO encounters(encounter_id, encounter_type, reward_type, difficulty, description_text, success_text, failure_text) VALUES
    (1, 'COMBAT', 'JUNK', 7,
    '[CH] was attacked by a mutated cow during the exploration of [L].',
    'Fortunately, [CH] managed to kill the beast and survived without a scratch. After the fight [CH] collected some junk that was lying around.',
    'Unfortunately, [CH] was wounded during the combat.'),
    (2, 'INTELLECT', 'CAPS', 7,
    '[CH] encountered a group of merchants during the exploration of [L]. The merchats offered to play a few rounds of dice poker.',
    'Due to the high value of [CH]`s intellect, he managed to win some caps.', 'Unfortunately, [CH] lost couple of games.'),
    (3, 'CHARM', 'CHARACTER', 7,
    '[CH] found a wounded person during the exploration of [L].',
    'Due to the high value of [CH]`s charm, the wander let accepted [CH]`s help and decided to join your clan.',
    'Unfortunately, [CH]`s scarry appearance made the person run away.'),
    (4, 'COMBAT', 'ITEM', 6,
    '[CH] was attacked by an older man during the exploration of a hidden hideout in [L].',
    'All those combat training paid off and [CH] managed to get rid of the attacker and loot his hideout.',
    'The angry inhabitant turned out to be quite dangerous and wounded [CH] during the combat.');

-- Random names
INSERT INTO name_prefixes (value) VALUES
    ('Quick'),
    ('Skinny'),
    ('Big'),
    ('Loud'),
    ('Dancing'),
    ('Smelly'),
    ('Strong'),
    ('Foxy'),
    ('Clever'),
    ('Silent'),
    ('Good'),
    ('Captain'),
    ('Acid'),
    ('Fierce'),
    ('Spotty'),
    ('Faithful'),
    ('Toubled'),
    ('Mad'),
    ('Lunatic'),
    ('Hungry'),
    ('Thirsty'),
    ('General'),
    ('Groovy'),
    ('Salty'),
    ('Mean'),
    ('Hysterical'),
    ('Cold'),
    ('Plastic'),
    ('Black'),
    ('Rotting');

INSERT INTO names (gender, value) VALUES
    ('MALE', 'Nick'),
    ('MALE', 'Kevin'),
    ('MALE', 'Joe'),
    ('MALE', 'Bob'),
    ('MALE', 'Petee'),
    ('MALE', 'John'),
    ('MALE', 'Sam'),
    ('MALE', 'Rufus'),
    ('MALE', 'Igor'),
    ('MALE', 'Dan'),
    ('MALE', 'Frank'),
    ('MALE', 'James'),
    ('MALE', 'Simon'),
    ('MALE', 'Hugo'),
    ('MALE', 'Dexter'),
    ('MALE', 'Tom'),
    ('MALE', 'Paul'),
    ('MALE', 'Martin'),
    ('MALE', 'Luke'),
    ('MALE', 'Zack'),
    ('MALE', 'Worm'),
    ('MALE', 'Atlas'),
    ('MALE', 'Zero'),
    ('MALE', 'Viper'),
    ('MALE', 'Exo'),
    ('MALE', 'Fiddles'),
    ('MALE', 'Patch'),
    ('MALE', 'Smuggy'),
    ('MALE', 'Patches'),
    ('MALE', 'Weeds'),
    ('MALE', 'Spot'),
    ('MALE', 'Grub'),
    ('MALE', 'Hog'),
    ('FEMALE', 'Angie'),
    ('FEMALE', 'Tamara'),
    ('FEMALE', 'Fox'),
    ('FEMALE', 'Misty'),
    ('FEMALE', 'Ember'),
    ('FEMALE', 'Vyolet'),
    ('FEMALE', 'Julia'),
    ('FEMALE', 'Carol'),
    ('FEMALE', 'Witch'),
    ('FEMALE', 'Enigma'),
    ('FEMALE', 'Moon'),
    ('FEMALE', 'Rascal'),
    ('FEMALE', 'Snow-white'),
    ('FEMALE', 'Toots'),
    ('FEMALE', 'Chance'),
    ('FEMALE', 'Muse'),
    ('FEMALE', 'Faythe'),
    ('FEMALE', 'Sunshine'),
    ('FEMALE', 'Grace'),
    ('FEMALE', 'Flower'),
    ('FEMALE', 'Honey'),
    ('FEMALE', 'Nemo'),
    ('FEMALE', 'Pixy'),
    ('FEMALE', 'Pepper'),
    ('FEMALE', 'Piper'),
    ('FEMALE', 'Kara');
