DROP TABLE IF EXISTS profiles;
CREATE TABLE profiles (
    profile_id INT,
    name VARCHAR(32) UNIQUE NOT NULL,
    premium_type VARCHAR(16) DEFAULT NULL,
    avatar_url VARCHAR(256),
    theme VARCHAR(16),
    ranking INT,
    last_activity TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    consecutive_logins INT DEFAULT 0,
    PRIMARY KEY (profile_id)
);

DROP TABLE IF EXISTS historical_results;
CREATE TABLE historical_results (
    result_id BIGINT AUTO_INCREMENT,
    profile_id INT NOT NULL,
    clan_name VARCHAR(32) NOT NULL,
    place INT,
    fame INT,
    num_clans INT,
    num_turns INT,
    game_version VARCHAR(16),
    game_ended TIMESTAMP,
    faction VARCHAR(16),
    completed_quests INT,
    research INT,
    buildings INT,
    PRIMARY KEY (result_id),
    CONSTRAINT result_profile_fk FOREIGN KEY (profile_id) REFERENCES profiles (profile_id)
);