--- Trait details
INSERT INTO trait_details(identifier, bonus, image_url) VALUES
    ('FIGHTER', 1, 'https://storage.googleapis.com/withergate-images/traits/fighter-trait.png'),
    ('SHARPSHOOTER', 1, 'https://storage.googleapis.com/withergate-images/traits/sharpshooter-trait.png'),
    ('ASCETIC', 0, 'https://storage.googleapis.com/withergate-images/traits/ascetic-trait.png'),
    ('HUNTER', 2, 'https://storage.googleapis.com/withergate-images/traits/hunter-trait.png'),
    ('HOARDER', 2, 'https://storage.googleapis.com/withergate-images/traits/hoarder-trait.png'),
    ('BUILDER', 2, 'https://storage.googleapis.com/withergate-images/traits/builder-trait.png'),
    ('LIZARD', 2, 'https://storage.googleapis.com/withergate-images/traits/lizard-trait.png');

INSERT INTO localized_texts(trait_name, lang, text) VALUES
    ('FIGHTER', 'en', 'Fighter'),
    ('FIGHTER', 'cs', 'Rváč'),
    ('SHARPSHOOTER', 'en', 'Sharpshooter'),
    ('SHARPSHOOTER', 'cs', 'Trefa do černého'),
    ('ASCETIC', 'en', 'Ascetic'),
    ('ASCETIC', 'cs', 'Asketa'),
    ('HUNTER', 'en', 'Hunter'),
    ('HUNTER', 'cs', 'Lovec'),
    ('HOARDER', 'en', 'Hoarder'),
    ('HOARDER', 'cs', 'Křeček'),
    ('BUILDER', 'en', 'Builder'),
    ('BUILDER', 'cs', 'Stavitel'),
    ('LIZARD', 'en', 'Lizard skin'),
    ('LIZARD', 'cs', 'Ještěří kůže');

INSERT INTO localized_texts(trait_description, lang, text) VALUES
    ('FIGHTER', 'en', 'Character with this trait has a chance to gain a combat bonus when fighting with a melee weapon.'),
    ('FIGHTER', 'cs', 'Postava s touto schopností má šanci získat bonus k bojové síle při souboji se zbraní nablízko.'),
    ('SHARPSHOOTER', 'en', 'Character with this trait has a chance to gain a combat bonus when fighting with a ranged weapon.'),
    ('SHARPSHOOTER', 'cs', 'Postava s touto schopností má šanci získat bonus k bojové síle při souboji se střelnou zbraní.'),
    ('ASCETIC', 'en', 'Character with this trait does not need to consume any food at the end of each turn.'),
    ('ASCETIC', 'cs', 'Postava s touto schopností nepotřebuje žádné jídlo na konci kola.'),
    ('HUNTER', 'en', 'Character with this trait will get bonus food when scavenging.'),
    ('HUNTER', 'cs', 'Postava s touto schopností najde více jídla při prohledávání lokací.'),
    ('HOARDER', 'en', 'Character with this trait will get bonus junk when scavenging.'),
    ('HOARDER', 'cs', 'Postava s touto schopností najde více šrotu při prohledávání lokací.'),
    ('BUILDER', 'en', 'Character with this trait constructs building faster without paying extra junk for the construction.'),
    ('BUILDER', 'cs', 'Postava s touto schopností staví budovy rychleji, aniž musela platit šrot navíc.'),
    ('LIZARD', 'en', 'Character with this trait heals faster when resting.'),
    ('LIZARD', 'cs', 'Postava s touto schopností se při odpočinku rychleji uzdravuje.');
