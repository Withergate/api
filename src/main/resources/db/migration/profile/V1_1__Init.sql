DROP TABLE IF EXISTS profiles;
CREATE TABLE profiles (
    profile_id INT,
    name VARCHAR(32) UNIQUE NOT NULL,
    premium_type VARCHAR(16) DEFAULT NULL,
    avatar_url VARCHAR(256),
    ranking INT,
    PRIMARY KEY (profile_id)
);

DROP TABLE IF EXISTS historical_results;
CREATE TABLE historical_results (
    result_id BIGINT AUTO_INCREMENT,
    player_id INT NOT NULL,
    clan_name VARCHAR(32) NOT NULL,
    place INT,
    fame INT,
    num_clans INT,
    num_turns INT,
    game_version VARCHAR(16),
    game_ended TIMESTAMP,
    PRIMARY KEY (result_id)
);