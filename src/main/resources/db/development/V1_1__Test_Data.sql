INSERT INTO clans (clan_id, clan_name, caps, junk) VALUES
    (1, 'Stalkers', 100, 250);

INSERT INTO weapons (weapon_id, weapon_name) VALUES
    (1, 'Kitchen knife');

INSERT INTO characters (character_id, clan_id, character_name, gender, state, combat, scavenge, craftsmanship, charm, intellect, weapon_id) VALUES
    (1, 1, 'Rusty Nick', 'MALE', 'BUSY', 3, 2, 4, 1, 2, null),
    (2, 1, 'Foxy Ann', 'FEMALE', 'READY', 1, 3, 2, 4, 5, null),
    (3, 1, 'Sticky Marry', 'FEMALE', 'READY', 2, 4, 2, 4, 1, 1);

INSERT INTO location_actions (action_id, state, character_id, location) VALUES
    (1, 'COMPLETED', 1, 'WASTELEND'),
    (2, 'PENDING', 1, 'CITY');