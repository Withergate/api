-- Research details
INSERT INTO research_details(identifier, information_level, cost, image_url) VALUES
    ('FORGERY', 0, 20, 'https://storage.googleapis.com/withergate-images/reserach/forgery.png'),
    ('BEGGING', 0, 20, 'https://storage.googleapis.com/withergate-images/buildings/begging.png'),
    ('ARCHITECTURE', 2, 30, 'https://storage.googleapis.com/withergate-images/buildings/architecture.png');

INSERT INTO localized_texts(research_name, lang, text) VALUES
    ('FORGERY', 'en', 'Forgery'),
    ('FORGERY', 'cs', 'Padělání'),
    ('BEGGING', 'en', 'Begging'),
    ('BEGGING', 'cs', 'Žebrání'),
    ('ARCHITECTURE', 'en', 'Wasteland architecture'),
    ('ARCHITECTURE', 'cs', 'Architektura Pustiny');

INSERT INTO localized_texts(research_description, lang, text) VALUES
    ('FORGERY', 'en', 'Forgery'),
    ('FORGERY', 'cs', 'Padělání'),
    ('BEGGING', 'en', 'Begging'),
    ('BEGGING', 'cs', 'Žebrání'),
    ('ARCHITECTURE', 'en', 'Wasteland architecture'),
    ('ARCHITECTURE', 'cs', 'Architektura Pustiny');

INSERT INTO localized_texts(research_info, lang, text) VALUES
    ('FORGERY', 'en', 'Forgery'),
    ('FORGERY', 'cs', 'Padělání'),
    ('BEGGING', 'en', 'Begging'),
    ('BEGGING', 'cs', 'Žebrání'),
    ('ARCHITECTURE', 'en', 'Wasteland architecture'),
    ('ARCHITECTURE', 'cs', 'Architektura Pustiny');
