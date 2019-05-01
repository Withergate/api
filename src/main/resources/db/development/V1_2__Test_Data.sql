INSERT INTO clans (clan_id, clan_name, fame, caps, junk, food, information, information_level, arena) VALUES
    (1, 'Stalkers', 10, 100, 250, 10, 7, 0, false);

INSERT INTO weapons (weapon_id, item_identifier, clan_id) VALUES
    (1, 'KNIFE', null),
    (2, 'HAND_GUN', 1);

INSERT INTO gear (gear_id, item_identifier, clan_id) VALUES
    (1, 'DETECTOR', 1);

INSERT INTO outfits (outfit_id, item_identifier, clan_id) VALUES
    (1, 'LEATHER_CLOTHES', 1);

INSERT INTO buildings (building_id, identifier, level, progress, clan_id) VALUES
    (1, 'SICK_BAY', 1, 8, 1);

INSERT INTO consumables(consumable_id, item_identifier, clan_id) VALUES
    (1, 'SMALL_MEDKIT', 1),
    (2, 'MANUSCRIPT', 1);

INSERT INTO characters (character_id, clan_id, character_name, gender, state, level, experience, hitpoints, max_hitpoints, combat, scavenge, craftsmanship, intellect, weapon_id, gear_id, image_url) VALUES
    (1, 1, 'Rusty Nick', 'MALE', 'BUSY', 1, 9, 7, 9, 3, 2, 1, 2, null, null, 'https://i.ibb.co/pnQ12b8/male01.jpg'),
    (2, 1, 'Foxy Ann', 'FEMALE', 'READY', 1, 0, 8, 8, 1, 3, 2, 5, null, null, 'https://i.ibb.co/kJYvvWK/female01.jpg'),
    (3, 1, 'Sticky Marry', 'FEMALE', 'READY', 2, 2, 1, 10, 2, 4, 2, 1, 1, null, 'https://i.ibb.co/kJYvvWK/female01.jpg');

INSERT INTO traits(trait_id, identifier, character_id) VALUES
    (1, 'FIGHTER', 1),
    (2, 'ASCETIC', 1),
    (3, 'BUILDER', 3);

INSERT INTO location_actions (action_id, state, character_id, location, action_type) VALUES
    (1, 'COMPLETED', 1, 'WASTELEND', 'VISIT'),
    (2, 'PENDING', 1, 'CITY_CENTER', 'VISIT');
