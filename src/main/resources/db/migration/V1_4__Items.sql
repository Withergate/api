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
    ('SMALL_MEDKIT', 'CONSUMABLE', 'COMMON', 10, 4, 0, 'HEALING', 'https://storage.googleapis.com/withergate-images/items/small-medkit.jpg'),
    ('LARGE_MEDKIT', 'CONSUMABLE', 'RARE', 30, 8, 0, 'HEALING', 'https://storage.googleapis.com/withergate-images/items/large-medkit.jpg'),
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
    ('SAW', 'GEAR', 'COMMON', true, 20, 2, 'CONSTRUCT', 'https://storage.googleapis.com/withergate-images/items/no-image.jpg'),
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
