-- Trait details
INSERT INTO factions (identifier, image_url, icon_url) VALUES
    ('REVOLUTION', 'https://storage.googleapis.com/withergate-images/factions/revolution.png', 'https://storage.googleapis.com/withergate-images/factions/revolution-icon.png'),
    ('CULT', 'https://storage.googleapis.com/withergate-images/factions/cult.png', 'https://storage.googleapis.com/withergate-images/factions/cult-icon.png');

INSERT INTO localized_texts (faction_name, lang, text) VALUES
    ('REVOLUTION', 'en', 'Viva la Revolution'),
    ('REVOLUTION', 'cs', 'Viva la Revolution'),
    ('CULT', 'en', 'Cult of Blood Moon'),
    ('CULT', 'cs', 'Kult Krvavého Měsíce');

INSERT INTO localized_texts (faction_description, lang, text) VALUES
    ('REVOLUTION', 'en', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin in tellus sit amet nibh dignissim sagittis. Aliquam erat volutpat. Duis pulvinar. Donec vitae arcu.'),
    ('REVOLUTION', 'cs', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin in tellus sit amet nibh dignissim sagittis. Aliquam erat volutpat. Duis pulvinar. Donec vitae arcu.'),
    ('CULT', 'en', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin in tellus sit amet nibh dignissim sagittis. Aliquam erat volutpat. Duis pulvinar. Donec vitae arcu.'),
    ('CULT', 'cs', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin in tellus sit amet nibh dignissim sagittis. Aliquam erat volutpat. Duis pulvinar. Donec vitae arcu.');

INSERT INTO faction_aids (faction, aid_type, cost, aid, num_aid, health_cost, faction_points, fame) VALUES
    ('REVOLUTION', 'RESOURCES', 10, 2, 3, false, 10, 1),
    ('CULT', 'SACRIFICE', 0, 0, 0, true, 5, 1),
    ('CULT', 'RESOURCES', 10, 2, 3, false, 10, 1);