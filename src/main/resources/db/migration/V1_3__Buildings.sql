-- Building details
INSERT INTO building_details(identifier, cost, visitable, visit_junk_cost, image_url) VALUES
    ('SICK_BAY', 15, false, 0, 'https://storage.googleapis.com/withergate-images/buildings/sickbay.png'),
    ('GMO_FARM', 12, false, 0, 'https://storage.googleapis.com/withergate-images/buildings/gmofarm.png'),
    ('TRAINING_GROUNDS', 8, 0, false, 'https://storage.googleapis.com/withergate-images/buildings/traininggrounds.png'),
    ('MONUMENT', 15, false, 0, 'https://storage.googleapis.com/withergate-images/buildings/monument.png'),
    ('FORGE', 12, true, 10, 'https://storage.googleapis.com/withergate-images/buildings/forge.png'),
    ('WORKSHOP', 12, true, 10, 'https://storage.googleapis.com/withergate-images/buildings/workshop.png'),
    ('RAGS_SHOP', 12, true, 10, 'https://storage.googleapis.com/withergate-images/buildings/ragsshop.png'),
    ('QUARTERS', 15, false, 10, 'https://storage.googleapis.com/withergate-images/buildings/quarters.png');

INSERT INTO localized_texts(building_name, lang, text) VALUES
    ('SICK_BAY', 'en', 'Sick bay'),
    ('SICK_BAY', 'cs', 'Marodka'),
    ('GMO_FARM', 'en', 'GMO farm'),
    ('GMO_FARM', 'cs', 'GMO farma'),
    ('TRAINING_GROUNDS', 'en', 'Training grounds'),
    ('TRAINING_GROUNDS', 'cs', 'Cvičiště'),
    ('MONUMENT', 'en', 'Monument'),
    ('MONUMENT', 'cs', 'Monument'),
    ('FORGE', 'en', 'Forge'),
    ('FORGE', 'cs', 'Kovárna'),
    ('WORKSHOP', 'en', 'Workshop'),
    ('WORKSHOP', 'cs', 'Dílna'),
    ('RAGS_SHOP', 'en', 'Rags shop'),
    ('RAGS_SHOP', 'cs', 'Hadrárna'),
    ('QUARTERS', 'en', 'Quarters'),
    ('QUARTERS', 'cs', 'Ubikace');

INSERT INTO localized_texts(building_description, lang, text) VALUES
    ('SICK_BAY', 'en', 'Feeling under the weather? Grab a bed and pull yourself together!'),
    ('SICK_BAY', 'cs', 'Je vám pod psa? Pojďte se u nás natáhnout!'),
    ('GMO_FARM', 'en', 'We finally realized how to genetically modify our animals without them growing a third head!'),
    ('GMO_FARM', 'cs', 'Teď už víme, jak těch našich pár zvířat geneticky vylepšit, aniž by jim narostla třetí hlava!'),
    ('TRAINING_GROUNDS', 'en', 'There is nothing we cant learn!'),
    ('TRAINING_GROUNDS', 'cs', 'Není nic, co by se tvůj velkolepý klan nemohl naučit!'),
    ('MONUMENT', 'en', 'Let everybody know the glory of your mighty clan by building a monument near your campsite!'),
    ('MONUMENT', 'cs', 'Ať všichni vidí, jak je náš klan úžasný!'),
    ('FORGE', 'en', 'Got some junk? Let`s forge some weapons for the arena!'),
    ('FORGE', 'cs', 'Roztavíme všechno. A slijeme z toho něco do arény!'),
    ('WORKSHOP', 'en', 'Great way how to turn junk into different type of junk.'),
    ('WORKSHOP', 'cs', 'Skvělý způsob, jak ze šrotu vyrobit jiný typ šrotu.'),
    ('RAGS_SHOP', 'en', 'We will make you a great outfit out of everything you found in the wasteland.'),
    ('RAGS_SHOP', 'cs', 'Uděláme ti skvělý vohoz ze všeho, cos našel v pustině.'),
    ('QUARTERS', 'en', 'No place to put your head down? We have a room for you! Dont mind the roaches.'),
    ('QUARTERS', 'cs', 'Nemáš, kde složit hlavu? Máme pro tebe fajnovej pokoj. Pokud ti teda nevadí pár švábů.');

INSERT INTO localized_texts(building_info, lang, text) VALUES
    ('SICK_BAY', 'en', 'Each level of this building increases the hitpoints healed when resting.'),
    ('SICK_BAY', 'cs', 'Každá úroveň této budovy zvyšuje počet vyléčených životů postav, které odpočívají.'),
    ('GMO_FARM', 'en', 'Each level of this building grants free food every turn.'),
    ('GMO_FARM', 'cs', 'Každá úroveň této budovy poskytuje jídlo zdarma každé kolo.'),
    ('TRAINING_GROUNDS', 'en', 'Each level of this building grants free experience to all resting characters every turn.'),
    ('TRAINING_GROUNDS', 'cs', 'Odpočívající postavy dostanou každé kolo zkušenosti zdarma.'),
    ('MONUMENT', 'en', 'Each level of this building grants free fame every turn.'),
    ('MONUMENT', 'cs', 'Každá úroveň této budovy poskutuje slávu každé kolo.'),
    ('FORGE', 'en', 'Pay junk and craft a random weapon! High craftsmanship affects the rarity of the crafted item.'),
    ('FORGE', 'cs', 'Zaplať šrot a vyrob náhodnou zbraň! Zručnost ovlivňuje šanci na vyšší raritu předmětu.'),
    ('WORKSHOP', 'en', 'Pay junk and craft a random gear! High craftsmanship affects the rarity of the crafted item.'),
    ('WORKSHOP', 'cs', 'Zaplať šrot a vyrob náhodný kus výbavy! Zručnost ovlivňuje šanci na vyšší raritu předmětu.'),
    ('RAGS_SHOP', 'en', 'Pay junk and craft a random outfit! High craftsmanship affects the rarity of the crafted item.'),
    ('RAGS_SHOP', 'cs', 'Zaplať šrot a vyrob náhodný oděv! Zručnost ovlivňuje šanci na vyšší raritu předmětu.'),
    ('QUARTERS', 'en', 'Each level of this building increases your population limit.'),
    ('QUARTERS', 'cs', 'Každá úroveň této budovy navyšuje tvůj populační limit.');
