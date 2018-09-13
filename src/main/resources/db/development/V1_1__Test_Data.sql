INSERT INTO clans (clan_id, clan_name, fame, caps, junk) VALUES
    (1, 'Stalkers', 10, 100, 250),
    (2, 'Dragons', 5, 50, 80);

INSERT INTO weapons (weapon_id, item_name, clan_id) VALUES
    (1, 'Kitchen knife', null),
    (2, 'Glock', 1);

INSERT INTO consumables(consumable_id, item_name, clan_id) VALUES
    (1, 'Small medkit', 1);

INSERT INTO characters (character_id, clan_id, character_name, gender, state, hitpoints, max_hitpoints, combat, scavenge, craftsmanship, intellect, weapon_id) VALUES
    (1, 1, 'Rusty Nick', 'MALE', 'BUSY', 7, 9, 3, 2, 1, 2, null),
    (2, 1, 'Foxy Ann', 'FEMALE', 'READY', 8, 8, 1, 3, 2, 5, null),
    (3, 1, 'Sticky Marry', 'FEMALE', 'READY', 1, 10, 2, 4, 2, 1, 1),
    (4, 2, 'Bloody Susan', 'FEMALE', 'READY', 9, 9, 1, 1, 2, 2, null);

INSERT INTO location_actions (action_id, state, character_id, location) VALUES
    (1, 'COMPLETED', 1, 'WASTELEND'),
    (2, 'PENDING', 1, 'CITY');