-- First turn
INSERT INTO turns (turn_id) VALUES
    (1);

-- Weapon details
INSERT INTO item_details(identifier, item_type, rarity, weapon_type, price, bonus, craftable, image_url) VALUES
    ('KNIFE', 'WEAPON', 'COMMON', 'MELEE', 10, 1, true, 'https://storage.googleapis.com/withergate-images/items/knife.jpg'),
    ('AXE', 'WEAPON', 'RARE', 'MELEE', 20, 2, true, 'https://storage.googleapis.com/withergate-images/items/axe.jpg'),
    ('BAT', 'WEAPON', 'COMMON', 'MELEE', 10, 1, true, 'https://storage.googleapis.com/withergate-images/items/bat.jpg'),
    ('CHAINSAW', 'WEAPON', 'RARE', 'MELEE', 60, 3, false, 'https://storage.googleapis.com/withergate-images/items/chainsaw.jpg'),
    ('BOOMERANG', 'WEAPON', 'COMMON', 'RANGED', 10, 1, true, 'https://storage.googleapis.com/withergate-images/items/boomerang.jpg'),
    ('THROWING_KNIFES', 'WEAPON', 'COMMON', 'RANGED', 20, true, 2, 'https://storage.googleapis.com/withergate-images/items/throwing-knifes.jpg'),
    ('CROSSBOW', 'WEAPON', 'RARE', 'RANGED', 50, 3, true, 'https://storage.googleapis.com/withergate-images/items/crossbow.jpg'),
    ('HAND_GUN', 'WEAPON', 'RARE', 'RANGED', 80, 4, false, 'https://storage.googleapis.com/withergate-images/items/handgun.jpg');

INSERT INTO localized_texts(item_name, lang, text) VALUES
    ('KNIFE', 'en', 'Knife'),
    ('KNIFE', 'cs', 'Nůž'),
    ('AXE', 'en', 'Axe'),
    ('AXE', 'cs', 'Sekera'),
    ('BAT', 'en', 'Baseball bat'),
    ('BAT', 'cs', 'Palice'),
    ('CHAINSAW', 'en', 'Chainsaw'),
    ('CHAINSAW', 'cs', 'Motorová pila'),
    ('BOOMERANG', 'en', 'Boomerang'),
    ('BOOMERANG', 'cs', 'Boomerang'),
    ('THROWING_KNIFES', 'en', 'Throwing knifes'),
    ('THROWING_KNIFES', 'cs', 'Vrhací nože'),
    ('CROSSBOW', 'en', 'Crossbow'),
    ('CROSSBOW', 'cs', 'Kuše'),
    ('HAND_GUN', 'en', 'Hand gun'),
    ('HAND_GUN', 'cs', 'Pistole');

INSERT INTO localized_texts(item_description, lang, text) VALUES
    ('KNIFE', 'en', 'A dull edged, rusty kitchen knife stained with blood.'),
    ('KNIFE', 'cs', 'Orezlý kuchyňský nůž od krve.'),
    ('AXE', 'en', 'One-handed axe.'),
    ('AXE', 'cs', 'Jednoruční sekera.'),
    ('BAT', 'en', 'Baseball bat full of splinters enhanced with some nails.'),
    ('BAT', 'cs', 'Baseballová pálka vylepšená několika hřebíky.'),
    ('CHAINSAW', 'en', 'Gas powered chainsaw with some fuel still left in the tank.'),
    ('CHAINSAW', 'cs', 'Benzinová motorová pila s palivem v nádrži.'),
    ('BOOMERANG', 'en', 'Wooden boomerang. With some razors attached to it.'),
    ('BOOMERANG', 'cs', 'Dřevěný boomerang s přidělanými žiletkami.'),
    ('THROWING_KNIFES', 'en', 'A set of well balanced throwing knives.'),
    ('THROWING_KNIFES', 'cs', 'Několik dobře vybalancovaných vrhacích nožů.'),
    ('CROSSBOW', 'en', 'Modern crossbows can be precise and silent at the same time.'),
    ('CROSSBOW', 'cs', 'Moderní kuše. Přesná a tichá.'),
    ('HAND_GUN', 'en', 'Hand gun. Light, practical, deadly.'),
    ('HAND_GUN', 'cs', 'Pistole. Lehká, praktická, smrtící.');

-- Consumable details
INSERT INTO item_details(identifier, item_type, rarity, price, bonus, prereq, effect_type, image_url) VALUES
    ('SMALL_MEDKIT', 'CONSUMABLE', 'COMMON', 10, 2, 0, 'HEALING', 'https://storage.googleapis.com/withergate-images/items/small-medkit.jpg'),
    ('LARGE_MEDKIT', 'CONSUMABLE', 'RARE', 30, 4, 0, 'HEALING', 'https://storage.googleapis.com/withergate-images/items/large-medkit.jpg'),
    ('MANUSCRIPT', 'CONSUMABLE', 'COMMON', 20, 5, 3, 'EXPERIENCE', 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('OLD_BOOK', 'CONSUMABLE', 'RARE', 40, 10, 4, 'EXPERIENCE', 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('STEROIDS', 'CONSUMABLE', 'RARE', 50, 1, 0, 'BUFF_COMBAT', 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('TOXICAT', 'CONSUMABLE', 'RARE', 50, 1, 0, 'BUFF_SCAVENGE', 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('TRIFFID_HAND', 'CONSUMABLE', 'RARE', 50, 1, 0, 'BUFF_CRAFTSMANSHIP', 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('MIND_PATCH', 'CONSUMABLE', 'RARE', 50, 1, 0, 'BUFF_INTELLECT', 'https://storage.googleapis.com/withergate-images/no-image.jpg');


INSERT INTO localized_texts(item_name, lang, text) VALUES
    ('SMALL_MEDKIT', 'en', 'Small medkit'),
    ('SMALL_MEDKIT', 'cs', 'Malá lékárnička'),
    ('LARGE_MEDKIT', 'en', 'Large medkit'),
    ('LARGE_MEDKIT', 'cs', 'Velká lékárnička'),
    ('MANUSCRIPT', 'en', 'Manuscript'),
    ('MANUSCRIPT', 'cs', 'Rukopis'),
    ('OLD_BOOK', 'en', 'Old book'),
    ('OLD_BOOK', 'cs', 'Stará kniha'),
    ('STEROIDS', 'en', 'Anabolic steroids'),
    ('STEROIDS', 'cs', 'Anabolické steroidy'),
    ('TOXICAT', 'en', 'Toxicat'),
    ('TOXICAT', 'cs', 'Toxiqaat'),
    ('TRIFFID_HAND', 'en', 'Triffid hand'),
    ('TRIFFID_HAND', 'cs', 'Triffidí palčák'),
    ('MIND_PATCH', 'en', 'Mind patch'),
    ('MIND_PATCH', 'cs', 'Mozkový štěp');

INSERT INTO localized_texts(item_description, lang, text) VALUES
    ('SMALL_MEDKIT', 'en', 'Basic medical equipment for providing first aid and treating minor wounds.'),
    ('SMALL_MEDKIT', 'cs', 'Malá lékárnička se základními potřebami pro první pomoc.'),
    ('LARGE_MEDKIT', 'en', 'Large bag containing all sorts of medical equipment. Useful for treating more dangerous wounds.'),
    ('LARGE_MEDKIT', 'cs', 'Velká lékárnička s vybavením na ošetření težších zranění.'),
    ('MANUSCRIPT', 'en', 'Those who are able to read can learn a lot from this ancient text.'),
    ('MANUSCRIPT', 'cs', 'Z těchto starodávných textů se můžeš naučit mnoho, pokud tedy umíš číst.'),
    ('OLD_BOOK', 'en', 'Not everyone can understand an ancient source of knowledge - but if you are the one, a powerful knowledge is waiting for you as a reward.'),
    ('OLD_BOOK', 'cs', 'Ne každý pochopí tento starodávný zdroj vědění - ale pokud to zvládneš, odměnou ti budou mocné znalosti.'),
    ('STEROIDS', 'en', 'This will make your muscles grow... and put hair on your lips, maybe even hair on your hairs. Permanently improves combat by 1.'),
    ('STEROIDS', 'cs', 'Po tomhle ti rychle narostou svaly... a chlupy i na rtech, možná chlupy i na chlupech. Trvale zvýší bojovou sílu o 1.'),
    ('TOXICAT', 'en', 'There is a lot of aromatic and slightly psychadelic herb called khat (or mad cat) so many people love chewing its leafs. Qaat macerated in a suspicious toxic solution reportedly temporaly improves your scavenge by 1.'),
    ('TOXICAT', 'cs', 'V pustině lze snadno nalézt aromatickou a mírně halucinogenní bylinu jménem qaat a kdekdo tak žvýká její listy. Qaat namočený v pochybném toxickém roztoku údajně trvale zvýší hledání o 1.'),
    ('TRIFFID_HAND', 'en', 'Some wastelanders are wrapping their hands into a sting of a very aggressive genetically modified plant. This painful procedure makes their hand hardy and, oddly enough, agile - which permanently improves craftsmanship by 1.'),
    ('TRIFFID_HAND', 'cs', 'Po chvíli bolestivého nošení tohoto módního doplňku vyrobeného z žahadla vysoce agresivní geneticky modifikované rostliny se ruce stávají odolnějšími a z nějakého důvodu i hbitějšími, což trvale zvýší zručnost o 1.'),
    ('MIND_PATCH', 'en', 'Give your head an injection of ideas of smarter people and improve permanently your intellect by 1. And who is smarter now, huh?'),
    ('MIND_PATCH', 'cs', 'Injekcí do hlavy lze vpravit myšlenky chytřejších lidí a zvýšit tak intelekt trvale o 1. A kdo je chytřejší teď, co?');

-- Gear details
INSERT INTO item_details(identifier, item_type, rarity, craftable, price, bonus, bonus_type, image_url) VALUES
    ('SAW', 'GEAR', 'COMMON', true, 20, 2, 'CONSTRUCT', 'https://storage.googleapis.com/withergate-images/items/large-medkit.jpg'),
    ('TRAP', 'GEAR', 'COMMON', true, 20, 2, 'SCAVENGE_FOOD', 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('DETECTOR', 'GEAR', 'RARE', false, 50, 4, 'SCAVENGE_JUNK', 'https://storage.googleapis.com/withergate-images/no-image.jpg');

INSERT INTO localized_texts(item_name, lang, text) VALUES
    ('SAW', 'en', 'Saw'),
    ('SAW', 'cs', 'Pila'),
    ('TRAP', 'en', 'Trap'),
    ('TRAP', 'cs', 'Past'),
    ('DETECTOR', 'en', 'Detector'),
    ('DETECTOR', 'cs', 'Detektor');

INSERT INTO localized_texts(item_description, lang, text) VALUES
    ('SAW', 'en', 'Somewhat toothless, but still working. Improves craftsmanship when building without the necessity to pay any extra junk.'),
    ('SAW', 'cs', 'Poněkud bezzubá, ale svůj účel splní. Zvýší zručnost při stavbě budov, aniž by bylo nutné platit materiál navíc.'),
    ('TRAP', 'en', 'All you need is to place it and tomorrow you can collect it. Improves the chance of finding food.'),
    ('TRAP', 'cs', 'Stačí rozmístit a zítra sesbírat. Zvýší šanci na nalezení jídla.'),
    ('DETECTOR', 'en', 'A few decades old, so it doesn’t work really well. Improves the chance of finding junk.'),
    ('DETECTOR', 'cs', 'Je stár minimálně několik desetiletí, tak za ta léta už moc neslouží. Zvýší šanci na nalezení kovu.');

-- Outfit details
INSERT INTO item_details(identifier, item_type, rarity, craftable, price, bonus, image_url) VALUES
    ('LEATHER_CLOTHES', 'OUTFIT', 'COMMON', true, 20, 1, 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('LEATHER_COVERED', 'OUTFIT', 'RARE', true, 40, 2, 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('KEVLAR', 'OUTFIT', 'RARE', false, 100, 3, 'https://storage.googleapis.com/withergate-images/no-image.jpg');

INSERT INTO localized_texts(item_name, lang, text) VALUES
    ('LEATHER_CLOTHES', 'en', 'Leather clothes'),
    ('LEATHER_CLOTHES', 'cs', 'Kožené oblečení'),
    ('LEATHER_COVERED', 'en', 'Leather clothes covered with nails'),
    ('LEATHER_COVERED', 'cs', 'Hřeby pobité kožené oblečení'),
    ('KEVLAR', 'en', 'Kevlar vest'),
    ('KEVLAR', 'cs', 'Kevlarová vesta');

INSERT INTO localized_texts(item_description, lang, text) VALUES
    ('LEATHER_CLOTHES', 'en', 'Standard clothing providing a basic defense.'),
    ('LEATHER_CLOTHES', 'cs', 'Standardní oděv poskytující základní ochranu.'),
    ('LEATHER_COVERED', 'en', 'Clothing made of leather covered with nails and other metal junk. Unattractive, yes, but it protects every important parts of the body.'),
    ('LEATHER_COVERED', 'cs', 'Oděv z kůže pobité hřeby a dalšími kovovými zbytky. Nevzhledně vypadající, leč dostatečně chránící všechny důležité části těla.'),
    ('KEVLAR', 'en', 'Vest made of ancient material which nobody can produce today. Apart from few scratches, it is a real masterpiece.'),
    ('KEVLAR', 'cs', 'Vesta z pradávného materiálu, který dnes již nikdo neumí vyrobit. Až na pár odřenin je to skutečný skvost..');

-- Building details
INSERT INTO building_details(identifier, cost, visitable, visit_junk_cost, image_url) VALUES
    ('SICK_BAY', 15, false, 0, 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('GMO_FARM', 10, false, 0, 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('TRAINING_GROUNDS', 20, 0, false, 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('MONUMENT', 20, false, 0, 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('FORGE', 10, true, 10, 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('QUARTERS', 10, false, 10, 'https://storage.googleapis.com/withergate-images/no-image.jpg');

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
    ('QUARTERS', 'en', 'No place to put your head down? We have a room for you! Dont mind the roaches.'),
    ('FORGE', 'cs', 'Nemáš, kde složit hlavu? Máme pro tebe fajnovej pokoj. Pokud ti teda nevadí pár švábů.');

INSERT INTO localized_texts(building_info, lang, text) VALUES
    ('SICK_BAY', 'en', 'Each level of this building increases the hitpoints healed when resting.'),
    ('SICK_BAY', 'cs', 'Každá úroveň této budovy zvyšuje počet vyléčených životů postav, které odpočívají.'),
    ('GMO_FARM', 'en', 'Each level of this building grants free food every turn.'),
    ('GMO_FARM', 'cs', 'Každá úroveň této budovy poskytuje jídlo zdarma každé kolo.'),
    ('TRAINING_GROUNDS', 'en', 'Each level of this building grants free experience to all resting characters every turn.'),
    ('TRAINING_GROUNDS', 'cs', 'Odpočívající postavy dostanou každé kolo zkušenosti zdarma.'),
    ('MONUMENT', 'en', 'Each level of this building grants free fame every turn.'),
    ('MONUMENT', 'cs', 'Každá úroveň této budovy poskutuje slávu každé kolo.'),
    ('FORGE', 'en', 'Pay [10] junk and craft a random weapon! High craftsmanship affects the rarity of the crafted item.'),
    ('FORGE', 'cs', 'Zaplať [10] šrotu a vyrob náhodnou zbraň! Zručnost ovlivňuje šanci na vyšší raritu předmětu.'),
    ('QUARTERS', 'en', 'Each level of this building increases your population limit.'),
    ('QUARTERS', 'cs', 'Každá úroveň této budovy navyšuje tvůj populační limit.');

--- Trait details
INSERT INTO trait_details(identifier, bonus, image_url) VALUES
    ('FIGHTER', 1, 'https://storage.googleapis.com/withergate-images/traits/fighter-trait.png'),
    ('ASCETIC', 0, 'https://storage.googleapis.com/withergate-images/traits/ascetic-trait.png'),
    ('HUNTER', 2, 'https://storage.googleapis.com/withergate-images/traits/strong-trait.png'),
    ('HOARDER', 2, 'https://storage.googleapis.com/withergate-images/traits/strong-trait.png'),
    ('BUILDER', 2, 'https://storage.googleapis.com/withergate-images/traits/builder-trait.png');

INSERT INTO localized_texts(trait_name, lang, text) VALUES
    ('FIGHTER', 'en', 'Fighter'),
    ('FIGHTER', 'cs', 'Rváč'),
    ('ASCETIC', 'en', 'Ascetic'),
    ('ASCETIC', 'cs', 'Asketa'),
    ('HUNTER', 'en', 'Hunter'),
    ('HUNTER', 'cs', 'Lovec'),
    ('HOARDER', 'en', 'Hoarder'),
    ('HOARDER', 'cs', 'Křeček'),
    ('BUILDER', 'en', 'Builder'),
    ('BUILDER', 'cs', 'Stavitel');

INSERT INTO localized_texts(trait_description, lang, text) VALUES
    ('FIGHTER', 'en', 'Character with this trait has a chance to gain a combat bonus when fighting with a melee weapon.'),
    ('FIGHTER', 'cs', 'Postava s touto schopností má šanci získat bonus k bojové síle při souboji se zbraní nablízko.'),
    ('ASCETIC', 'en', 'Character with this trait does not need to consume any food at the end of each turn.'),
    ('ASCETIC', 'cs', 'Postava s touto schopností nepotřebuje žádné jídlo na konci kola.'),
    ('HUNTER', 'en', 'Character with this trait will get bonus food when scavenging.'),
    ('HUNTER', 'cs', 'Postava s touto schopností najde více jídla při prohledávání lokací.'),
    ('HOARDER', 'en', 'Character with this trait will get bonus junk when scavenging.'),
    ('HOARDER', 'cs', 'Postava s touto schopností najde více šrotu při prohledávání lokací.'),
    ('BUILDER', 'en', 'Character with this trait constructs building faster without paying extra junk for the construction.'),
    ('BUILDER', 'cs', 'Postava s touto schopností staví budovy rychleji, aniž musela platit šrot navíc.');

-- Location descriptions
INSERT INTO location_descriptions(location, scouting, description, info, image_url) VALUES
    ('NEIGHBORHOOD', false, 'locations.neighborhood.description', 'locations.neighborhood.info',
    'https://storage.googleapis.com/withergate-images/locations/neighborhood.jpg'),
    ('WASTELAND', true, 'locations.wasteland.description', 'locations.wasteland.info',
    'https://storage.googleapis.com/withergate-images/locations/wasteland.jpg'),
    ('CITY_CENTER', true, 'locations.citycenter.description', 'locations.citycenter.info',
    'https://storage.googleapis.com/withergate-images/locations/city.jpg');

INSERT INTO localized_texts(location_name, lang, text) VALUES
    ('NEIGHBORHOOD', 'en', 'Neighborhood'),
    ('NEIGHBORHOOD', 'cs', 'Sousedství'),
    ('WASTELAND', 'en', 'Wasteland'),
    ('WASTELAND', 'cs', 'Pustina'),
    ('CITY_CENTER', 'en', 'City center'),
    ('CITY_CENTER', 'cs', 'Centrum města');

INSERT INTO localized_texts(location_description, lang, text) VALUES
    ('NEIGHBORHOOD', 'en', 'Neighborhood is the area around your camp. It is a relatively safe place since you have been living there for quite some time. It is a safe location to search for junk and food. Do not expect to find anything too valuable, though.'),
    ('NEIGHBORHOOD', 'cs', 'Sousedství je území v okolí tvého kempu. Je to relativně klidné místo, už tady přeci jen chvíli žijete. Ideální kratochvílí v této oblasti je sbírání jídla a šrotu. Nečekejte, že tady narazíte na nějaké zázraky.'),
    ('WASTELAND', 'en', 'Wasteland is the desolated area all around you. It might seem abandonded but do not be mistaken. Other characters roam this area so searching this place can sometimes be dangerous.'),
    ('WASTELAND', 'cs', 'Pustina je vyprahlá oblast všude okolo. Může se zdát opuštěná, ale věz, že se zde nachází mnoho nebezpečí.'),
    ('CITY_CENTER', 'en', 'The ruins of the center of Withergate hides the most valuable treasures. Unfortunately, run by local gangs and roamed by scavengers, this place can sometimes prove to be very dangerous. On the other hand, if you are lucky, you can find some useful items here.'),
    ('CITY_CENTER', 'cs', 'Ruiny centra Withergate ukrývají ty největší poklady. Jedná o velmi nebezpečnou oblast obývanou lokálními gangy, mutanty a tím nejhorším, co pustina vyplivla. Pokud budete mít štěstí, můžete zde nalézt opravdové poklady.');

INSERT INTO localized_texts(location_info, lang, text) VALUES
    ('NEIGHBORHOOD', 'en', 'Neighborhood is a relatively safe location. The most probable outcome is finding some junk or food.'),
    ('NEIGHBORHOOD', 'cs', 'Sousedství je relativně klidná oblast. Nejpravděpodobnější výsledek průzkumu bude jídlo a šrot.'),
    ('WASTELAND', 'en', 'Wasteland has an increased chance for encountering some potentially dangerous events. However, handling such event well might lead to an interesting reward.'),
    ('WASTELAND', 'cs', 'V pustině je vyšší šance na nebezpečné události. Na druhou stranu, při úspěchu je zde také šance na vyšší odměnu. Je možné zde zjistit zajímavé informace.'),
    ('CITY_CENTER', 'en', 'City has the highest risk of encoutering dangerous events. On the other hand, it also provides higher chances for finding valuable loot.'),
    ('CITY_CENTER', 'cs', 'Centrum města má nejvyšší riziko na nebezpečné události. Pokud se vám podaří přežít, tak je zde ale i šance na vysoké odměny. Také se zde postavy mohou dozvědět hodně zajímavých informací při pátrání.');

-- Quests
INSERT INTO quest_details (identifier, quest_type, difficulty, information_level, completion, caps_reward, fame_reward, image_url) VALUES
    ('quest.info1.1', 'COMBAT', 3, 1, 4, 40, 20, 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('quest.info1.2', 'INTELLECT', 4, 1, 3, 60, 25, 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('quest.info2.1', 'CRAFTSMANSHIP', 3, 2, 4, 60, 50, 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('quest.info3.1', 'COMBAT', 6, 3, 2, 100, 3, 'https://storage.googleapis.com/withergate-images/no-image.jpg');

INSERT INTO localized_texts(quest_name, lang, text) VALUES
    ('quest.info1.1', 'en', 'Deratization'),
    ('quest.info1.1', 'cs', 'Deratizace'),
    ('quest.info1.2', 'en', 'Encrypted message'),
    ('quest.info1.2', 'cs', 'Zašifrovaná zpráva'),
    ('quest.info2.1', 'en', 'Water purifier renovation'),
    ('quest.info2.1', 'cs', 'Oprava čističky vody'),
    ('quest.info3.1', 'en', 'Wolf den'),
    ('quest.info3.1', 'cs', 'Vlčí doupě');

INSERT INTO localized_texts(quest_description, lang, text) VALUES
    ('quest.info1.1', 'en', 'There is a rat infestation in the sewers under the tavern. Get rid of those mutated bastards and reward will be yours!'),
    ('quest.info1.1', 'cs', 'Ve sklepení pod hospodou se usídlily zmutované krysy. Zbav se jich a odměna tě nemine.'),
    ('quest.info1.2', 'en', 'Hey, pst! I found this encrypted message from one of my colleages, who passed away long time ago. Help me with the decryption and I will pay you.'),
    ('quest.info1.2', 'cs', 'Hej, ty! Můžeš mi pomoct? Našel jsem zápisky od svého zesnulého kolegy. Text je ale zašifrovaný. Když mi ho pomůžeš rozluštit, tak ti zaplatím.'),
    ('quest.info2.1', 'en', 'Our water purifier broke down and we are running out of clean water. Help us with the renovation and we will spread the fame of your clan!'),
    ('quest.info2.1', 'cs', 'Naše čistička vod se rozbila a nám dochází voda. Když nám ji pomůžeš opravit, tak budeme šířit slávu tvého klanu!'),
    ('quest.info3.1', 'en', 'There is a wolf den in the forest. Those bloody beasts are killing our sheep! Get rid of them for us and we will never forget your courage.'),
    ('quest.info3.1', 'cs', 'V lese je doupě vlků. Ty krvežíznivé potvory nám zabíjí naše ovce. Když je zabiješ, tak nikdy nezapomeneme na tvou odvahu.');

-- Placeholder texts notifications
INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('location.tavern.hired', 'en', 'Character went to the tavern to hire someone for your clan. After spending the evening chatting with several people, the decision fell on {}'),
    ('location.tavern.hired', 'cs', 'Postava šla do hospody s nabídkou lehce vydělaných zátek. Po několika hodinách padlo rozhodnutí na {}.'),
    ('location.resources', 'en', 'Character found some resources.'),
    ('location.resources', 'cs', 'Postava  našla suroviny.'),
    ('location.loot', 'en', 'Character found loot.'),
    ('location.loot', 'cs', 'Postava našla předmět.'),
    ('location.information', 'en', 'Character found valuable information when scouting.'),
    ('location.information', 'cs', 'Postava zjistila při pátrání cenné informace.'),
    ('building.work', 'en', 'Character worked on a construction of [].'),
    ('building.work', 'cs', 'Postava  pracovala na stavbě [].'),
    ('building.crafting.weapon', 'en', 'Character was crafting a weapon..'),
    ('building.crafting.weapon', 'cs', 'Postava vyráběla zbraně.'),
    ('building.monument.income', 'en', 'Your Monument generated fame for your clan.'),
    ('building.monument.income', 'cs', 'Tvůj Monument získal slávu pro tvůj klan.'),
    ('building.gmofarm.income', 'en', 'Your GMO farm generated food for your clan.'),
    ('building.gmofarm.income', 'cs', 'Na GMO farmě se podařilo vyprodukovat jídlo pro tvůj klan.'),
    ('building.traininggrounds.income', 'en', 'Character gained experience when training at the Training grounds.'),
    ('building.traininggrounds.income', 'cs', 'Postava získala zkušenosti tréninkem na cvičišti.'),
    ('character.healing', 'en', 'Character has recovered some health when resting.'),
    ('character.healing', 'cs', 'Postavě se zvedlo zdraví zdraví při odpočinku.'),
    ('character.levelup', 'en', 'Character has advanced to a higher level.'),
    ('character.levelup', 'cs', 'Postava postoupila na další úroveň.'),
    ('character.quest.success', 'en', 'Character successfully completed part of the quest.'),
    ('character.quest.success', 'cs', 'Postava úspěšně splnila část úkolu.'),
    ('character.quest.failure', 'en', 'Character did not manage to complete part of the quest.'),
    ('character.quest.failure', 'cs', 'Postavě se nepodařilo splnit část úkolu.'),
    ('character.trade.resourcesBuy', 'en', 'Character went to the markeplace and bought some resources.'),
    ('character.trade.resourcesBuy', 'cs', 'Postava na tržišti koupila nějaké zásoby.'),
    ('character.trade.resourcesSell', 'en', 'Character went to the markeplace and sold some resources.'),
    ('character.trade.resourcesSell', 'cs', 'Postava na tržišti prodala nějaké zásoby.'),
    ('quest.completed', 'en', 'Quest was completed: [].'),
    ('quest.completed', 'cs', 'Úkol byl dokončen: [].'),
    ('clan.foodConsumption', 'en', 'Characters are consuming food.'),
    ('clan.foodConsumption', 'cs', 'Postavy konzumují jídlo.'),
    ('combat.arena.description', 'en', '{} faced {} in the arena.'),
    ('combat.arena.description', 'cs', '{} se utkal/a s {} v aréně.'),
    ('combat.arena.win', 'en', '{} won the fight.'),
    ('combat.arena.win', 'cs', '{} vyhrál/a souboj.'),
    ('combat.arena.lose', 'en', '{} lost the fight.'),
    ('combat.arena.lose', 'cs', '{} prohrál/a souboj.'),
    ('combat.death', 'en', 'Unfortunately, {} died during the combat.'),
    ('combat.death', 'cs', 'Bohužel, {} zemřel/a během souboje.');

-- Placeholder texts detail
INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('detail.character.joined', 'en', '{} joined your clan.'),
    ('detail.character.joined', 'cs', '{} se přidal/a ke tvému klanu.'),
    ('detail.character.starvationdeath', 'en', '{} died of starvation.'),
    ('detail.character.starvationdeath', 'cs', '{} zemřel/a hlady.'),
    ('detail.character.foodConsumption', 'en', '{} consumes food.'),
    ('detail.character.foodConsumption', 'cs', '{} konzumuje jídlo.'),
    ('detail.character.injurydeath', 'en', '{} died from suffering the injury.'),
    ('detail.character.injurydeath', 'cs', '{} podlehl/a zraněním.'),
    ('detail.character.levelup.trait', 'en', '{} got a new trait.'),
    ('detail.character.levelup.trait', 'cs', '{} získal/a novou schopnost.'),
    ('detail.character.starving', 'en', '{} is starving.'),
    ('detail.character.starving', 'cs', '{} hladoví.'),
    ('detail.character.crafting', 'en', '{} crafted [].'),
    ('detail.character.crafting', 'cs', '{} vyrobil/a [].'),
    ('detail.information.levelup', 'en', 'Your clan advanced to the next information level.'),
    ('detail.information.levelup', 'cs', 'Tvůj klan postoupil na další úroveň informací.'),
    ('detail.quest.assigned', 'en', 'New quest assigned: [].'),
    ('detail.quest.assigned', 'cs', 'Nový úkol: [].'),
    ('detail.healing.roll', 'en', 'Rolled {} when computing healing.'),
    ('detail.healing.roll', 'cs', 'Na kostce padlo {} při hodu na léčení.'),
    ('detail.healing.building', 'en', 'Sick bay increased healing by {}.'),
    ('detail.healing.building', 'cs', 'Marodka zlepšila léčbu o {}.'),
    ('detail.combat.rolls', 'en', '{} rolled {} on combat dice. {} rolled {}.'),
    ('detail.combat.rolls', 'cs', '{} hodil/a při souboji {}. {} hodil/a {}.'),
    ('detail.combat.roundresult', 'en', '{} won the round with combat value {}. {} had combat value {}. {} lost {} health'),
    ('detail.combat.roundresult', 'cs', '{} vyhrál/a kolo souboje s bojovým číslem {}. {} měl bojové číslo {}. {} ztrácí {} životy.'),
    ('detail.combat.flee', 'en', '{} fleed the combat with {} dice roll and {}% flee chance.'),
    ('detail.combat.flee', 'cs', '{} uprchl/a ze souboje s hodem {} při šanci {}% na útěk.'),
    ('detail.item.found.storage', 'en', '{} found [] and took it to your clan storage.'),
    ('detail.item.found.storage', 'cs', '{} nalezl/a [] a vzal/a tento úlovek do klanového skladu.'),
    ('detail.building.levelup', 'en', '[] advanced to the next level.'),
    ('detail.building.levelup', 'cs', 'Tvé budově [] se zvýšila úroveň.'),
    ('detail.trait.builder', 'en', '{} used his/her Builder trait and increased the construction speed.'),
    ('detail.trait.builder', 'cs', '{} použil svou schopnost Stavitel a rychlost stavby se zvýšila.'),
    ('detail.trait.fighter', 'en', '{} got a combat bonus for having a trait Fighter and fighting with a melee weapon.'),
    ('detail.trait.fighter', 'cs', '{} získal/a bonus k bojové síly za schopnost Rváč při boji se zbraní nablízko.'),
    ('detail.trait.ascetic', 'en', '{} does not need to consume any food because of his Ascetic trait.'),
    ('detail.trait.ascetic', 'cs', '{} nemusí konzumovat žádné jídlo díky své schopnosti Asketa.'),
    ('detail.trait.scavenge', 'en', 'Additional resources were found because of character`s trait [].'),
    ('detail.trait.scavenge', 'cs', 'Bylo nalezeno více kořisti díky schopnosti [].'),
    ('gear.bonus.junk', 'en', 'Character found more junk thanks to the quipped gear: [].'),
    ('gear.bonus.junk', 'cs', 'Postava našla více šrotu díky předmětu: [].'),
    ('gear.bonus.food', 'en', 'Character found more food thanks to the quipped gear: [].'),
    ('gear.bonus.food', 'cs', 'Postava našla více jídla díky předmětu: [].'),
    ('gear.bonus.work', 'en', 'Character worked more efficiently thanks to the quipped gear: [].'),
    ('gear.bonus.work', 'cs', 'Postava pracovala efektivněji díky svému vybavení: [].');

-- Placeholder texts encounters
INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('encounter.w.1.desc', 'en', 'Character was attacked by mutants while scavenging in the ruins of a wasteland village.'),
    ('encounter.w.1.desc', 'cs', 'V průběhu prohledávání trosek opuštěné vesnice postavu napadli mutanti.'),
    ('encounter.w.1.succ', 'en', 'Character managed to repel the attackers and collect some of their dropped belongigs.'),
    ('encounter.w.1.succ', 'cs', 'Postava zahnala útočníky na bezhlavý útěk, při němž raději zahodili část svého majetku.'),
    ('encounter.w.1.fail', 'en', 'Character was wounded during the combat and had to flee without having collected anything.'),
    ('encounter.w.1.fail', 'cs', 'V souboji byla postava zraněna a musela utéci, aniž by se jí podařilo cokoliv získat.'),
    ('encounter.w.2.desc', 'en', 'Character was attacked by bandits while scavenging in wasteland.'),
    ('encounter.w.2.desc', 'cs', 'Při cestě pustinou se postava dostala do spárů lapků.'),
    ('encounter.w.2.succ', 'en', 'Character managed to overcome the opponents and stole some caps from one of the fleeing bandits.'),
    ('encounter.w.2.succ', 'cs', 'Postavě se podařilo přemoci překvapené lupiče. Pro spásu svých bídných životů raději nechali postavě několik zátek.'),
    ('encounter.w.2.fail', 'en', 'Character was wounded during the combat and had to flee without having collected anything.'),
    ('encounter.w.2.fail', 'cs', 'V souboji byla postava zraněna a musela utéci, aniž by se jí podařilo cokoliv získat.'),
    ('encounter.w.3.desc', 'en', 'Character was suddenly surprised by a wandering mutated wolf during the exploration of wasteland.'),
    ('encounter.w.3.desc', 'cs', 'Postavu v průběhu prohlídky pustiny náhle překvapil toulavý zmutovaný vlk.'),
    ('encounter.w.3.succ', 'en', 'Character overcame the beast after a short combat and managed to collect some junk on the way back.'),
    ('encounter.w.3.succ', 'cs', 'Postava po krátkém boji zvíře přemohla. Na cestě zpět získala trochu šrotu.'),
    ('encounter.w.3.fail', 'en', 'Character was wounded during the combat and had run back home without having collected anything.'),
    ('encounter.w.3.fail', 'cs', 'V souboji byla postava zraněna a musela utéci, aniž by se jí podařilo cokoliv získat.'),
    ('encounter.w.4.desc', 'en', 'Character was attacked by a mutated cow after passing through an open field.'),
    ('encounter.w.4.desc', 'cs', 'Uprostřed polí napadla postavu zmutovaná kráva. Dvouhlavá, šílená, zřejmě se jmenuje Brahmína.'),
    ('encounter.w.4.succ', 'en', 'Character overcame the beast during the combat and managed to find some junk in the junkyard next to the field.'),
    ('encounter.w.4.succ', 'cs', 'Postava zvíře skolila v souboji. Maso a kůže ze zmutované krávy je nepoužitelné, ale její čtyři výstavní rohy a kopyta se budou ještě hodit.'),
    ('encounter.w.4.fail', 'en', 'Character was wounded during the combat and had run back home without having collected anything.'),
    ('encounter.w.4.fail', 'cs', 'V souboji byla postava zraněna a musela utéci, aniž by se jí podařilo cokoliv získat.'),
    ('encounter.w.5.desc', 'en', 'Character saw a wounded woman in the middle of the road, crying for help.'),
    ('encounter.w.5.desc', 'cs', 'Postava uprostřed cesty spatřila zraněnou ženu prosící o pomoc.'),
    ('encounter.w.5.succ', 'en', 'After stepping a bit closer, the blood on the road did not seem very real and something was moving in the shadows. Our hero avoided this trap and went to collect some junk elsewhere.'),
    ('encounter.w.5.succ', 'cs', 'Postavu zarazilo, že krev na cestě byla příliš zaschlá a neodpovídala zraněním neznámé ženy. V ten moment se stíny v zákoutí u cesty podezřele pohnuly. Postava toto ochotnické představení proto opatrně obešla a šla sbírat šrot někam, kde takovými amatéry nebude rušena.'),
    ('encounter.w.5.fail', 'en', 'After kneeling next to the woman, something stepped out from the nearby shadows and hit our hero in the head. After regaining consciousness, some caps were found missing from the carried bag.'),
    ('encounter.w.5.fail', 'cs', 'Postava důvěřivě poklekla k neznámé ženě, poté však z blízkého zákoutí něco vyběhlo a udeřilo postavu do hlavy. Postava se probrala s pořádnou bolestí hlavy a ochuzena o několik zátek.'),
    ('encounter.w.6.desc', 'en', 'A hidden shelter was found and it seemed abandoned on the first glance. Our character started exploring the hideout but was suddenly attacked by an old man who was apparently living there.'),
    ('encounter.w.6.desc', 'cs', 'Postava nalezla úkryt, který se jevil jako opuštěný, a začala jej prohledávat. Ve skrýši však zřejmě žil jakýsi stařec, který se náhle rozhodl bránit svůj domov.'),
    ('encounter.w.6.succ', 'en', 'Character overcame the inhabitant and took some of his belongings. This is no land for the weak.'),
    ('encounter.w.6.succ', 'cs', 'Postava vyhnala starce z jeho domova a vzala si jeho věci. Asi se sem vrátí, ale bez svého vybavení moc dlouho nepřežije… ale co, tahle země není pro slabé.'),
    ('encounter.w.6.fail', 'en', 'The man turned out to be quite dangerous and our character was wounded during the combat.'),
    ('encounter.w.6.fail', 'cs', 'Stařec se ukázal jako vcelku nebezpečný a zranil postavu v souboji.'),
    ('encounter.w.7.desc', 'en', 'Character encountered a group of merchants and was invited by them for a game of dice poker.'),
    ('encounter.w.7.desc', 'cs', 'Postava potkala skupinu překupníků, kteří jí nabídli partii kostek.'),
    ('encounter.w.7.succ', 'en', 'After playing couple of games, our hero managed to outsmart the other players and left with some additional caps.'),
    ('encounter.w.7.succ', 'cs', 'Po několika hrách se soupeři nechali obrat o několik zátek.'),
    ('encounter.w.7.fail', 'en', 'Character lost couple of games and returned home with nothing.'),
    ('encounter.w.7.fail', 'cs', 'Po několika hrách se postava musela vrátit domů s holým zadkem.'),
    ('encounter.w.8.desc', 'en', 'The wind picked up speed during the exploration of wasteland. Soon enough, blizzard broke out and forced our hero to run for a shelter.'),
    ('encounter.w.8.desc', 'cs', 'Během průzkumu pustiny se začal zvedat vítr. Vánice donutila postavu urychleně hledat přístřeší.'),
    ('encounter.w.8.succ', 'en', 'Character managed to dig a small hole and hid in it until the blizzard passed. Some junk was collected on the way back.'),
    ('encounter.w.8.succ', 'cs', 'Postava vykopala jámu a schovala se v ní, dokud bouře nepřešla. Cestou zpět našla méně šťastné cestovatele. Nebo spíš jejich kusy… ten šrot, který měli u sebe, už jistě nebudou potřebovat...'),
    ('encounter.w.8.fail', 'en', 'Character tried to run to the nearby forest to hide under a tree but was hit by one of the falling branches.'),
    ('encounter.w.8.fail', 'cs', 'Postava se pokusila schovat pod stromy v blízkém lese, ale za tento jalový nápad byla potrestána zásahem jedné z padajících větví.'),
    ('encounter.w.9.desc', 'en', 'Character discovered a closed heavy metal chest in one of the abandonded hideouts. It was locked with something that looked like some kind of strange home-made mechanism.'),
    ('encounter.w.9.desc', 'cs', 'Postava v jedné opuštěné skrýši nalezla těžkou kovovou truhlu. Byla opatřena podivným podomácku vyrobeným zámkem.'),
    ('encounter.w.9.succ', 'en', 'Character spent couple of hours with the chest and managed to open the lock. An item! Nice...'),
    ('encounter.w.9.succ', 'cs', 'Tady hrubá síla nebyla nic platná. Po několika hodinách jemné mechanické práce se podařilo zámek otevřít. A uvnitř - předmět! A moc pěkný...'),
    ('encounter.w.9.fail', 'en', 'Character spent couple of hours trying to open or break the mechanism but was not able to get inside.'),
    ('encounter.w.9.fail', 'cs', 'Po několika minutách pokusu o jemnou mechanickou práci postava změnila taktiku. Ale ani po několika dalších hodinách tvůrčího násilí nebylo možné truhlu otevřít.'),
    ('encounter.c.1.desc', 'en', 'Character was attacked by a mutated cat while roaming through the abandoned streets of the city ruins.'),
    ('encounter.c.1.desc', 'cs', 'Postavu při toulkách opuštěnými ulicemi napadla zmutovaná kočka. Vřískající a škrábající sakra rychlé malé černé cosi.'),
    ('encounter.c.1.succ', 'en', 'Character managed to kill the feline and collected some junk on the way back home.'),
    ('encounter.c.1.succ', 'cs', 'Postavě se podařilo tu kočkosvini zabít a posbírat cestou domů trochu šrotu.'),
    ('encounter.c.1.fail', 'en', 'Character was wounded during the combat.'),
    ('encounter.c.1.fail', 'cs', 'Postava si ze souboje odnesla několik hrozivě vypadajících škrábanců.'),
    ('encounter.c.2.desc', 'en', 'Character was attacked by a mutated dog while roaming through the abandoned streets of the city ruins.'),
    ('encounter.c.2.desc', 'cs', 'Postavu při toulkách opuštěnými ulicemi napadl zmutovaný pes. Má sice jen jednu hlavu, ta je ale odpudivá za dvě.'),
    ('encounter.c.2.succ', 'en', 'Character managed to kill the beast and sell it in the city market for meat.'),
    ('encounter.c.2.succ', 'cs', 'Postavě se podařilo obludu zabít a prodat ji na blízkém trhu na maso. Místní zřejmě ocení novou mutaci svých chuťových pohárků.'),
    ('encounter.c.2.fail', 'en', 'Character was wounded during the combat. The wound got infected and required buing a desinfection in the city market.'),
    ('encounter.c.2.fail', 'cs', 'Netvor postavu ošklivě pokousal. Rána se rychle zanítila a tak bylo nezbytné na městském trhu koupit předraženou dezinfekci.'),
    ('encounter.c.3.desc', 'en', 'Character met a junkie while searching in the streets of the city. The junkie asked for some caps and, after being rejected, pulled out a knife.'),
    ('encounter.c.3.desc', 'cs', 'Při průzkumu města postava potkala pochybnou smažku. Feťák obhrouble požádal o několika zátek a poté, co je nedostal, vytáhl nůž.'),
    ('encounter.c.3.succ', 'en', 'Character overcame the attacker and took the gear he was carrying.'),
    ('encounter.c.3.succ', 'cs', 'Postava porazila útočníka a obrala jej o vybavení, které měl u sebe.'),
    ('encounter.c.3.fail', 'en', 'Character was wounded during the combat. The wound got infected and required buing a desinfection in the city market.'),
    ('encounter.c.3.fail', 'cs', 'Feťák postavu pořezal nožem. Rána se zanítila a tak bylo nutné na městském trhu koupit dezinfekci.'),
    ('encounter.c.4.desc', 'en', 'A priest in a torn cape was met while roaming the city streets. The priest asked if our hero believed in the cult of the Atom.'),
    ('encounter.c.4.desc', 'cs', 'V průběhu toulky ulicemi postavu oslovil kněz v otrhané kápi. Otevřeně se zeptal, zda postava uznává Atomův kult.'),
    ('encounter.c.4.succ', 'en', 'Character nodded, knowing that rejection might turn out to be dangerous when speaking with fanatics. The priest said a blessing and some some junk was collected on the way back.'),
    ('encounter.c.4.succ', 'cs', 'Postava raději přikývla, protože fanatici mohou být nebezpeční. Spokojený kněz postavě požehnal. Vzápětí nalezla několik kusů cenného šrotu. Náhoda?'),
    ('encounter.c.4.fail', 'en', 'Character did not know anything about such cult so cursed and tried to leave the strange man behind. Maybe it was just a strange coincidence, but nothing was found that day.'),
    ('encounter.c.4.fail', 'cs', 'Postava nikoho takového neznala a tak raději cizince rychle opustila. Kněz zřejmě nebyl potěšen a postavu z dálky proklel. Možná je to jen náhoda, ale ten den postava nenalezla vůbec nic cenného ani zajímavého.');

-- Random encounters
INSERT INTO encounters(location, encounter_type, reward_type, penalty_type, difficulty, description_text, success_text, failure_text) VALUES
    ('WASTELAND', 'COMBAT', 'ITEM', 'NONE', 4, 'encounter.w.1.desc', 'encounter.w.1.succ', 'encounter.w.1.fail'),
    ('WASTELAND', 'COMBAT', 'CAPS', 'NONE', 5, 'encounter.w.2.desc', 'encounter.w.2.succ', 'encounter.w.2.fail'),
    ('WASTELAND', 'COMBAT', 'JUNK', 'NONE', 6, 'encounter.w.3.desc', 'encounter.w.3.succ', 'encounter.w.3.fail'),
    ('WASTELAND', 'COMBAT', 'JUNK', 'NONE', 5, 'encounter.w.4.desc', 'encounter.w.4.succ', 'encounter.w.4.fail'),
    ('WASTELAND', 'INTELLECT', 'JUNK', 'CAPS', 8, 'encounter.w.5.desc', 'encounter.w.5.succ', 'encounter.w.5.fail'),
    ('WASTELAND', 'COMBAT', 'ITEM', 'NONE', 3, 'encounter.w.6.desc', 'encounter.w.6.succ', 'encounter.w.6.fail'),
    ('WASTELAND', 'INTELLECT', 'CAPS', 'CAPS', 7, 'encounter.w.7.desc', 'encounter.w.7.succ', 'encounter.w.7.fail'),
    ('WASTELAND', 'INTELLECT', 'JUNK', 'INJURY', 6, 'encounter.w.8.desc', 'encounter.w.8.succ', 'encounter.w.8.fail'),
    ('WASTELAND', 'INTELLECT', 'ITEM', 'NONE', 8, 'encounter.w.9.desc', 'encounter.w.9.succ', 'encounter.w.9.fail'),
    ('CITY_CENTER', 'COMBAT', 'JUNK', 'CAPS', 5, 'encounter.c.1.desc', 'encounter.c.1.succ', 'encounter.c.1.fail'),
    ('CITY_CENTER', 'COMBAT', 'CAPS', 'CAPS', 5, 'encounter.c.2.desc', 'encounter.c.2.succ', 'encounter.c.2.fail'),
    ('CITY_CENTER', 'COMBAT', 'ITEM', 'CAPS', 6, 'encounter.c.3.desc', 'encounter.c.3.succ', 'encounter.c.3.fail'),
    ('CITY_CENTER', 'INTELLECT', 'JUNK', 'NONE', 7, 'encounter.c.4.desc', 'encounter.c.4.succ', 'encounter.c.4.fail');

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
    ('Troubled'),
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

INSERT INTO avatars (avatar_id, gender, image_url) VALUES
    (1, 'MALE', 'https://storage.googleapis.com/withergate-images/characters/male01.jpg'),
    (2, 'FEMALE', 'https://storage.googleapis.com/withergate-images/characters/female01.jpg');

