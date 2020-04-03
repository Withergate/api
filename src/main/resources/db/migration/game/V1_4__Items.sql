-- Weapon details
INSERT INTO item_details(identifier, item_type, rarity, weapon_type, price, combat, crafting_cost, crafting_level, image_url) VALUES
    ('KNIFE', 'WEAPON', 'COMMON', 'MELEE', 12, 1, 13, 2, 'https://storage.googleapis.com/withergate-images/items/knife.png'),
    ('AXE', 'WEAPON', 'RARE', 'MELEE', 25, 2, 15, 3, 'https://storage.googleapis.com/withergate-images/items/axe.png'),
    ('CHAINSAW', 'WEAPON', 'EPIC', 'MELEE', 45, 3, 0, 0, 'https://storage.googleapis.com/withergate-images/items/chainsaw.png'),
    ('BOOMERANG', 'WEAPON', 'COMMON', 'RANGED', 12, 1, 11, 1, 'https://storage.googleapis.com/withergate-images/items/boomerang.png'),
    ('CROSSBOW', 'WEAPON', 'RARE', 'RANGED', 30, 3, 16, 3, 'https://storage.googleapis.com/withergate-images/items/crossbow.png'),
    ('HAND_GUN', 'WEAPON', 'EPIC', 'RANGED', 50, 4, 0, 0, 'https://storage.googleapis.com/withergate-images/items/handgun.png');

INSERT INTO item_details(identifier, item_type, rarity, weapon_type, price, combat, bonus, bonus_type, bonus_text, crafting_cost, crafting_level, image_url) VALUES
    ('BAT', 'WEAPON', 'COMMON', 'MELEE', 12, 0, 2, 'PIERCING', 'detail.weapon.bonus.piercing', 10, 1, 'https://storage.googleapis.com/withergate-images/items/bat.png'),
    ('SHINY_CROWBAR', 'WEAPON', 'COMMON', 'MELEE', 15, 1, -10, 'CAMOUFLAGE', 'detail.weapon.bonus.camouflage', 12, 1, 'https://storage.googleapis.com/withergate-images/items/shiny-crowbar.png'),
    ('THROWING_KNIVES', 'WEAPON', 'RARE', 'RANGED', 22, 2, 1, 'PIERCING', 'detail.weapon.bonus.piercing', 14, 2, 'https://storage.googleapis.com/withergate-images/items/throwing-knives.png');

INSERT INTO localized_texts(item_name, lang, text) VALUES
    ('KNIFE', 'en', 'Knife'),
    ('KNIFE', 'cs', 'Nůž'),
    ('AXE', 'en', 'Axe'),
    ('AXE', 'cs', 'Sekera'),
    ('BAT', 'en', 'Baseball bat'),
    ('BAT', 'cs', 'Palice'),
    ('SHINY_CROWBAR', 'en', 'Shiny crowbar'),
    ('SHINY_CROWBAR', 'cs', 'Třpytivé páčidlo'),
    ('CHAINSAW', 'en', 'Chainsaw'),
    ('CHAINSAW', 'cs', 'Motorová pila'),
    ('BOOMERANG', 'en', 'Boomerang'),
    ('BOOMERANG', 'cs', 'Boomerang'),
    ('THROWING_KNIVES', 'en', 'Throwing knives'),
    ('THROWING_KNIVES', 'cs', 'Vrhací nože'),
    ('CROSSBOW', 'en', 'Crossbow'),
    ('CROSSBOW', 'cs', 'Kuše'),
    ('HAND_GUN', 'en', 'Hand gun'),
    ('HAND_GUN', 'cs', 'Pistole');

INSERT INTO localized_texts(item_description, lang, text) VALUES
    ('KNIFE', 'en', 'A dull edged, rusty kitchen knife stained with blood.'),
    ('KNIFE', 'cs', 'Orezlý kuchyňský nůž od krve.'),
    ('AXE', 'en', 'One-handed axe.'),
    ('AXE', 'cs', 'Jednoruční sekera.'),
    ('BAT', 'en', 'Baseball bat full of splinters enhanced with some nails. It is hard to hit something but when it does, it pierces through the armor of your opponent.'),
    ('BAT', 'cs', 'Baseballová pálka vylepšená několika hřebíky. Díky své váze je poměrně náročné někoho trefit, ale když se to povede, stojí to za to. Ignoruje část zbroje protivníka.'),
    ('SHINY_CROWBAR', 'en', 'Handy crowbar with a shiny finish. Looks really nice but increases the encounter chance when searching and scouting.'),
    ('SHINY_CROWBAR', 'cs', 'Páčidlo v provedení vysokého lesku. Vyadá velmi efektně, ale přitahuje pozornost. Zvyšuje šanci na náhodnou událost při hledání a pátrání.'),
    ('CHAINSAW', 'en', 'Gas powered chainsaw with some fuel still left in the tank.'),
    ('CHAINSAW', 'cs', 'Benzinová motorová pila s palivem v nádrži.'),
    ('BOOMERANG', 'en', 'Wooden boomerang. With some razors attached to it.'),
    ('BOOMERANG', 'cs', 'Dřevěný boomerang s přidělanými žiletkami.'),
    ('THROWING_KNIVES', 'en', 'A set of well balanced throwing knives. Especially effective if you hit someone in the face. Ignores part of the armor of your opponent.'),
    ('THROWING_KNIVES', 'cs', 'Několik dobře vybalancovaných vrhacích nožů. Efektivní zejména, pokud někoho trefíte do xichtu. Ignorují část zbroje vašho protivníka.'),
    ('CROSSBOW', 'en', 'Modern crossbows can be precise and silent at the same time.'),
    ('CROSSBOW', 'cs', 'Moderní kuše. Přesná a tichá.'),
    ('HAND_GUN', 'en', 'Hand gun. Light, practical, deadly.'),
    ('HAND_GUN', 'cs', 'Pistole. Lehká, praktická, smrtící.');

-- Consumable details
INSERT INTO item_details(identifier, item_type, rarity, price, bonus, prereq, effect_type, image_url) VALUES
    ('SMALL_MEDKIT', 'CONSUMABLE', 'COMMON', 12, 4, 0, 'HEALING', 'https://storage.googleapis.com/withergate-images/items/small-medkit.png'),
    ('LARGE_MEDKIT', 'CONSUMABLE', 'RARE', 20, 8, 0, 'HEALING', 'https://storage.googleapis.com/withergate-images/items/large-medkit.png'),
    ('MANUSCRIPT', 'CONSUMABLE', 'COMMON', 12, 5, 3, 'EXPERIENCE', 'https://storage.googleapis.com/withergate-images/items/manuscript.png'),
    ('OLD_BOOK', 'CONSUMABLE', 'RARE', 30, 10, 4, 'EXPERIENCE', 'https://storage.googleapis.com/withergate-images/items/old-book.png'),
    ('STEROIDS', 'CONSUMABLE', 'EPIC', 40, 1, 0, 'BUFF_COMBAT', 'https://storage.googleapis.com/withergate-images/items/steroids.png'),
    ('TOXICAT', 'CONSUMABLE', 'EPIC', 40, 1, 0, 'BUFF_SCAVENGE', 'https://storage.googleapis.com/withergate-images/items/toxicat.png'),
    ('TRIFFID_HAND', 'CONSUMABLE', 'EPIC', 40, 1, 0, 'BUFF_CRAFTSMANSHIP', 'https://storage.googleapis.com/withergate-images/items/triffid-hand.png'),
    ('MIND_PATCH', 'CONSUMABLE', 'EPIC', 40, 1, 0, 'BUFF_INTELLECT', 'https://storage.googleapis.com/withergate-images/items/mind-patch.png');

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
    ('STEROIDS', 'en', 'This will make your muscles grow... and put hair on your lips, maybe even hair on your hairs. Permanently improves combat.'),
    ('STEROIDS', 'cs', 'Po tomhle ti rychle narostou svaly... a chlupy i na rtech, možná chlupy i na chlupech. Trvale zvýší bojovou sílu postavy.'),
    ('TOXICAT', 'en', 'There is a lot of aromatic and slightly psychadelic herb called khat (or mad cat) so many people love chewing its leafs. Qaat macerated in a suspicious toxic solution reportedly improves your scavenge skills.'),
    ('TOXICAT', 'cs', 'V pustině lze snadno nalézt aromatickou a mírně halucinogenní bylinu jménem qaat a kdekdo tak žvýká její listy. Qaat namočený v pochybném toxickém roztoku údajně trvale zvýší schopnost hledání.'),
    ('TRIFFID_HAND', 'en', 'Some wastelanders wrap their hands into a sting of a very aggressive genetically modified plant. This painful procedure makes their hand hardy and, oddly enough, agile - which permanently improves craftsmanship.'),
    ('TRIFFID_HAND', 'cs', 'Po chvíli bolestivého nošení tohoto módního doplňku vyrobeného z žahadla vysoce agresivní geneticky modifikované rostliny se ruce stávají odolnějšími a z nějakého důvodu i hbitějšími, což trvale zvýší zručnost.'),
    ('MIND_PATCH', 'en', 'Give your head an injection of ideas of smarter people and improve permanently your intellect. And who is smarter now, huh?'),
    ('MIND_PATCH', 'cs', 'Injekcí do hlavy lze vpravit myšlenky chytřejších lidí a zvýšit tak intelekt postavy. A kdo je chytřejší teď, co?');

-- Gear details
INSERT INTO item_details(identifier, item_type, rarity, price, bonus, bonus_type, bonus_text, crafting_cost, crafting_level, image_url) VALUES
    ('SAW', 'GEAR', 'COMMON', 12, 1, 'CONSTRUCT', 'detail.gear.bonus.work', 11, 1, 'https://storage.googleapis.com/withergate-images/items/saw.png'),
    ('TRAP', 'GEAR', 'COMMON', 12, 1, 'SCAVENGE_FOOD', 'detail.gear.bonus.food', 11, 1, 'https://storage.googleapis.com/withergate-images/items/trap.png'),
    ('GLASSES', 'GEAR', 'COMMON', 12, 1, 'RESEARCH', 'detail.gear.bonus.work', 11, 1, 'https://storage.googleapis.com/withergate-images/items/glasses.png'),
    ('HAMMER', 'GEAR', 'COMMON', 12, 2, 'CRAFTING', null, 10, 1, 'https://storage.googleapis.com/withergate-images/items/hammer.png'),
    ('NOTEBOOK', 'GEAR', 'COMMON', 12, 1, 'SCOUTING', 'detail.gear.bonus.information', 11, 1, 'https://storage.googleapis.com/withergate-images/items/notebook.png'),
    ('SHOVEL', 'GEAR', 'RARE', 20, 2, 'CONSTRUCT', 'detail.gear.bonus.work', 14, 2, 'https://storage.googleapis.com/withergate-images/items/shovel.png'),
    ('BINOCULARS', 'GEAR', 'RARE', 20, 2, 'SCAVENGE_FOOD', 'detail.gear.bonus.food', 13, 2, 'https://storage.googleapis.com/withergate-images/items/binoculars.png'),
    ('BANNER', 'GEAR', 'RARE', 20, 2, 'FACTION_POINTS', 'detail.gear.bonus.factionPoints', 14, 3, 'https://storage.googleapis.com/withergate-images/items/banner.png'),
    ('TRINKETS', 'GEAR', 'RARE', 20, 2, 'SCOUTING', 'detail.gear.bonus.information', 14, 2, 'https://storage.googleapis.com/withergate-images/items/trinkets.png'),
    ('SURVIVAL_KIT', 'GEAR', 'RARE', 20, 2, 'DISASTER', 'detail.gear.bonus.disaster', 14, 2, 'https://storage.googleapis.com/withergate-images/items/survival-kit.png'),
    ('DETECTOR', 'GEAR', 'EPIC', 35, 3, 'SCAVENGE_JUNK', 'detail.gear.bonus.junk', 0, 0, 'https://storage.googleapis.com/withergate-images/items/detector.png');

INSERT INTO localized_texts(item_name, lang, text) VALUES
    ('SAW', 'en', 'Saw'),
    ('SAW', 'cs', 'Pila'),
    ('TRAP', 'en', 'Trap'),
    ('TRAP', 'cs', 'Past'),
    ('GLASSES', 'en', 'Glasses'),
    ('GLASSES', 'cs', 'Brýle'),
    ('HAMMER', 'en', 'Hammer'),
    ('HAMMER', 'cs', 'Kladivo'),
    ('NOTEBOOK', 'en', 'Notebook'),
    ('NOTEBOOK', 'cs', 'Zápisník'),
    ('SHOVEL', 'en', 'Shovel'),
    ('SHOVEL', 'cs', 'Lopata'),
    ('BINOCULARS', 'en', 'Binoculars'),
    ('BINOCULARS', 'cs', 'Dalekohled'),
    ('BANNER', 'en', 'Banner'),
    ('BANNER', 'cs', 'Standarta'),
    ('TRINKETS', 'en', 'Trinkets'),
    ('TRINKETS', 'cs', 'Cetky'),
    ('SURVIVAL_KIT', 'en', 'Survival kit'),
    ('SURVIVAL_KIT', 'cs', 'KPZ'),
    ('DETECTOR', 'en', 'Detector'),
    ('DETECTOR', 'cs', 'Detektor');

INSERT INTO localized_texts(item_description, lang, text) VALUES
    ('SAW', 'en', 'Somewhat toothless, but still working. Improves craftsmanship when building without the necessity to pay any extra junk.'),
    ('SAW', 'cs', 'Poněkud bezzubá, ale svůj účel splní. Zvýší zručnost při stavbě budov, aniž by bylo nutné platit materiál navíc.'),
    ('TRAP', 'en', 'All you need is to place it and tomorrow you can collect it. Improves the chance of finding food.'),
    ('TRAP', 'cs', 'Stačí rozmístit a zítra sesbírat. Zvýší šanci na nalezení jídla.'),
    ('GLASSES', 'en', 'Little scratched but still working. Plus, you might actually look a bit more nerdy with them. Improves research capabilities of the character.'),
    ('GLASSES', 'cs', 'Trošku poškrábané, ale stále funkční. Navíc v nich konečně můžeš vypadat trošku inteligentně. Tento předmět zlepší schopnosti výzkumu dané postavy.'),
    ('HAMMER', 'en', 'When you have a hammer, everything`s a nail. Decreases the cost when crafting items.'),
    ('HAMMER', 'cs', 'Všechno je hřebík, když máš velké kladivo. Snižuje cenu při výrobě předmětů.'),
    ('NOTEBOOK', 'en', 'Some pages are missing but it can still be useful as s journey log. Increases the information gained from scouting.'),
    ('NOTEBOOK', 'cs', 'Pár stránek sice chybí, ale může se hodit na zápisky z cest. Zvýší počet získaných informací při průzkumu.'),
    ('SHOVEL', 'en', 'Finally something I can lean on when working. Improves craftsmanship when building without the necessity to pay any extra junk.'),
    ('SHOVEL', 'cs', 'Konečně něco, o co se mohu při práci opřít. Zvýší zručnost při stavbě budov, aniž by bylo nutné platit materiál navíc.'),
    ('BINOCULARS', 'en', 'If I see it, I can hit it. Improves the chance of finding food.'),
    ('BINOCULARS', 'cs', 'Když to uvidím, tak to zabiju. Zvýší šanci na nalezení jídla.'),
    ('BANNER', 'en', 'What better way of showing devotion for your faction than carrying a giant-ass banner with you all the time. Grants bonus influence when performing faction support actions.'),
    ('BANNER', 'cs', 'Není lepší způsob, jak vyjádřit zápal pro vlastní frakci, než s sebou všude tahat obří standartu. Předmět dává bonus k zisku vlivu k akcím podporujícím frakci.'),
    ('TRINKETS', 'en', 'Collection of small shiny trinkets that could be used as bribes for locals. Increases the information gained from scouting.'),
    ('TRINKETS', 'cs', 'Kolekce třpytivé veteše, která se dá použít jako úplatky pro místní. Zvýší počet získaných informací při průzkumu.'),
    ('SURVIVAL_KIT', 'en', 'A small box containing all basic equipment for survival. Add bonus progress when successfully preventing disaster.'),
    ('SURVIVAL_KIT', 'cs', 'Malá krabička se základními potřebami pro přežití. Dává postup navíc při úspěšném odvracení pohromy.'),
    ('DETECTOR', 'en', 'A few decades old, so it doesn’t work really well. Improves the chance of finding junk.'),
    ('DETECTOR', 'cs', 'Je stár minimálně několik desetiletí, tak za ta léta už moc neslouží. Zvýší šanci na nalezení kovu.');

-- Outfit details
INSERT INTO item_details(identifier, item_type, rarity, price, combat, bonus_type, bonus, bonus_text, crafting_cost, crafting_level, image_url) VALUES
    ('LEATHER_CLOTHES', 'OUTFIT', 'COMMON', 12, 1, null, 0, null, 12, 1, 'https://storage.googleapis.com/withergate-images/items/leather-clothes.png'),
    ('LAB_COAT', 'OUTFIT', 'COMMON', 12, 0, 'RESEARCH', 1, 'detail.gear.bonus.work', 11, 1, 'https://storage.googleapis.com/withergate-images/items/lab-coat.png'),
    ('LEATHER_COVERED', 'OUTFIT', 'RARE', 20, 2, null, 0, null, 14, 2, 'https://storage.googleapis.com/withergate-images/items/leather-covered.png'),
    ('CAMO_SUIT', 'OUTFIT', 'RARE', 20, 1, 'CAMOUFLAGE', 10, 'detail.gear.bonus.camouflage', 14, 2, 'https://storage.googleapis.com/withergate-images/items/camo-suit.png'),
    ('KEVLAR', 'OUTFIT', 'EPIC', 45, 3, null, 0, null, 0, 0, 'https://storage.googleapis.com/withergate-images/items/kevlar.png');

INSERT INTO localized_texts(item_name, lang, text) VALUES
    ('LEATHER_CLOTHES', 'en', 'Leather clothes'),
    ('LEATHER_CLOTHES', 'cs', 'Kožené oblečení'),
    ('LAB_COAT', 'en', 'Lab coat'),
    ('LAB_COAT', 'cs', 'Laboratorní plášť'),
    ('LEATHER_COVERED', 'en', 'Leather clothes covered with nails'),
    ('LEATHER_COVERED', 'cs', 'Hřeby pobité kožené oblečení'),
    ('CAMO_SUIT', 'en', 'Camo suit'),
    ('CAMO_SUIT', 'cs', 'Maskáče'),
    ('KEVLAR', 'en', 'Kevlar vest'),
    ('KEVLAR', 'cs', 'Kevlarová vesta');

INSERT INTO localized_texts(item_description, lang, text) VALUES
    ('LEATHER_CLOTHES', 'en', 'Standard clothing providing a basic defense.'),
    ('LEATHER_CLOTHES', 'cs', 'Standardní oděv poskytující základní ochranu.'),
    ('LAB_COAT', 'en', 'Torn lab coat. Does not offer any protection but looks incredibly smart. Increases research speed.'),
    ('LAB_COAT', 'cs', 'Potrhaný laboratorní plášť. Neposkytuje žádnou ochranu, ale vypadá zatraceně chytře. Zvyšuje rychlost výzkumu.'),
    ('LEATHER_COVERED', 'en', 'Clothing made of leather covered with nails and other metal junk. Unattractive, yes, but it protects every important parts of the body.'),
    ('LEATHER_COVERED', 'cs', 'Oděv z kůže pobité hřeby a dalšími kovovými zbytky. Nevzhledně vypadající, leč dostatečně chránící všechny důležité části těla.'),
    ('CAMO_SUIT', 'en', 'Camouflage clothing designed to resemble the background environment. It provides basic defense and also decreases chances for random encounters.'),
    ('CAMO_SUIT', 'cs', 'Maskovací oblečení pro splynutí s okolím. Tento oděv poskytuje základní ochranu a navíc snižuje šanci na náhodné události.'),
    ('KEVLAR', 'en', 'Vest made of ancient material which nobody can produce today. Apart from few scratches, it is a real masterpiece.'),
    ('KEVLAR', 'cs', 'Vesta z pradávného materiálu, který dnes již nikdo neumí vyrobit. Až na pár odřenin je to skutečný skvost..');
