-- Research details
INSERT INTO research_details(identifier, bonus_type, bonus_text, value, information_level, cost, cost_action, fame, image_url) VALUES
    ('FORGERY', 'CRAFTING_CAPS', 'detail.research.forgery', 0, 0, 20, 0, 1, 'https://storage.googleapis.com/withergate-images/research/forgery.png'),
    ('BEGGING', 'SCOUT_FOOD', 'detail.research.begging', 0, 0, 20, 0, 1, 'https://storage.googleapis.com/withergate-images/research/begging.png'),
    ('PLENTIFUL', 'NEIGHBORHOOD_LOOT', null, 15, 0, 20, 0, 1, 'https://storage.googleapis.com/withergate-images/research/plentiful.png'),
    ('CULTIVATION', 'REST_FOOD', 'detail.research.cultivation', 2, 0, 20, 0, 1, 'https://storage.googleapis.com/withergate-images/research/cultivation.png'),
    ('ARCHITECTURE', 'BUILDING_FAME', 'detail.research.architecture', 3, 1, 30, 0, 2, 'https://storage.googleapis.com/withergate-images/research/architecture.png'),
    ('BOASTING', 'COMBAT_FAME', 'detail.research.boasting', 2, 1, 30, 0, 2, 'https://storage.googleapis.com/withergate-images/research/boasting.png'),
    ('COLLECTING', 'TRADE_FAME', 'detail.research.collecting', 3, 2, 35, 10, 3, 'https://storage.googleapis.com/withergate-images/research/collecting.png'),
    ('JUNKSMITHERY', 'CRAFTING_FAME', 'detail.research.junksmithery', 3, 2, 35, 5, 3, 'https://storage.googleapis.com/withergate-images/research/junksmithery.png'),
    ('CULINARY', 'FOOD_FAME', 'research.culinary', 30, 4, 50, 0, 5, 'https://storage.googleapis.com/withergate-images/research/culinary.png'),
    ('DECORATION', 'JUNK_FAME', 'research.decoration', 30, 4, 50, 0, 5, 'https://storage.googleapis.com/withergate-images/research/decoration.png');

INSERT INTO localized_texts(research_name, lang, text) VALUES
    ('FORGERY', 'en', 'Forgery'),
    ('FORGERY', 'cs', 'Padělání'),
    ('BEGGING', 'en', 'Begging'),
    ('BEGGING', 'cs', 'Žebrání'),
    ('PLENTIFUL', 'en', 'Plentiful neighborhood'),
    ('PLENTIFUL', 'cs', 'Bohaté sousedství'),
    ('CULTIVATION', 'en', 'Cultivation'),
    ('CULTIVATION', 'cs', 'Pěstitelství'),
    ('ARCHITECTURE', 'en', 'Wasteland architecture'),
    ('ARCHITECTURE', 'cs', 'Architektura Pustiny'),
    ('BOASTING', 'en', 'Boasting'),
    ('BOASTING', 'cs', 'Vychloubání'),
    ('COLLECTING', 'en', 'Collecting'),
    ('COLLECTING', 'cs', 'Sběratelství'),
    ('JUNKSMITHERY', 'en', 'Junk Smithery'),
    ('JUNKSMITHERY', 'cs', 'Šrototepectví'),
    ('CULINARY', 'en', 'Culinary'),
    ('CULINARY', 'cs', 'Kulinářství'),
    ('DECORATION', 'en', 'Decoration'),
    ('DECORATION', 'cs', 'Dekorace');

INSERT INTO localized_texts(research_description, lang, text) VALUES
    ('FORGERY', 'en', 'There is always some time left after work. We have some spare in sheets in the storage. Perhaps, we could craft some caps out of it. Nobody would notice the difference...'),
    ('FORGERY', 'cs', 'Po práci občas zbude čas a máme v dílně staré kusy plechu. S trochou šikovnosti by se z nich dalo vyrobit pár zátek. Určitě si nikdo ničeho nevšimne.'),
    ('BEGGING', 'en', 'If we manage to look hungry and desperate enough, people might give us some food.'),
    ('BEGGING', 'cs', 'Stačí se tvářit trochu hladově a zoufale a lidé nám určitě dají nějaké jídlo.'),
    ('PLENTIFUL', 'en', 'There are more treasures around here than it looks. You just need to know where to look for them.'),
    ('PLENTIFUL', 'cs', 'V okolí je větší bohatství, než se na první pohled může zdát. Jen musíš vědět, kde hledat.'),
    ('CULTIVATION', 'en', 'Not sure if these berries are edible but they taste god-damn good. Let`s grow some at our camp.'),
    ('CULTIVATION', 'cs', 'Nevím, jestli jsou ty bobule jedlé, ale chutnají zatraceně dobře. Pojďme jich pár zasadit u nás v bázi.'),
    ('ARCHITECTURE', 'en', 'When we build something, let`s make it look nice. And it should also withstand the harsh Wasteland conditions.'),
    ('ARCHITECTURE', 'cs', 'Když už něco postavíme, tak by to taky mohlo aspoň nějak esteticky vypadat... a taky to hned nespadne.'),
    ('BOASTING', 'en', 'You say you killed a giant mutated werecow? Well I killed two and twice your size!'),
    ('BOASTING', 'cs', 'Říkáš, žes zabil zmutovaného krávodlaka? To já zabil dva a dvakrát tak velký!'),
    ('COLLECTING', 'en', 'This is not just a "thing". This is masterpiece! Soon, our collection will be famous all around the Wasteland!'),
    ('COLLECTING', 'cs', 'Říkáš, že to je jen "věc"? To je mistrovské dílo! Naše sbírka bude brzy proslulá po celé Pustině!'),
    ('JUNKSMITHERY', 'en', 'Some of us are true artists with junk. Even a shovel can be a piece of art!'),
    ('JUNKSMITHERY', 'cs', 'Někteří z nás dokáží se šrotem dělat opravdové umění. Říkáš, že na tu lopatu nechceš okrasné runy? Abych ti nějaké neudělal na čelo!'),
    ('CULINARY', 'en', 'True chef can prepare a feast from anything Wasteland provides. It might not receive a Michelin star but can taste damn good anyway.'),
    ('CULINARY', 'cs', 'Skutečný šéfkuchař dokáže připravit hostinu ze všeho, co dá Pustina. Sice to možná nedostane Michelinskou hvězdičku, ale i tak to může chutnat zatraceně dobře.'),
    ('DECORATION', 'en', 'It might be just junk but with some time and effort, we can decorate our base to make it more cozy.'),
    ('DECORATION', 'cs', 'Je to sice jen šrot, ale s trochou času a úsilí s ním můžeme ozdobit a zútulnit naši bázi.');

INSERT INTO localized_texts(research_info, lang, text) VALUES
    ('FORGERY', 'en', 'Your characters will receive a small amount of caps when crafting an item.'),
    ('FORGERY', 'cs', 'Tvé postavy dostanou malé množství zátek při výrobě předmětu.'),
    ('BEGGING', 'en', 'Your characters will receive a small amount of food when scouting for information. This does not apply when an encounter is triggered.'),
    ('BEGGING', 'cs', 'Tvé postavy dostanou malé množství jídla při pátrání po informacích. Tento efekt platí pouze, pokud nedojde k náhodné události.'),
    ('PLENTIFUL', 'en', 'This research increases the chances of finding items when searching the Neighborhood.'),
    ('PLENTIFUL', 'cs', 'Tato technologie zvyšuje šance na nalezení předmětu při hledání v Sousedství.'),
    ('CULTIVATION', 'en', 'Your characters will produce a small amount of food when resting.'),
    ('CULTIVATION', 'cs', 'Tvé postavy vyprodukují malé množství jídla při odpočinku.'),
    ('ARCHITECTURE', 'en', 'Whenever you build a new building or increase a level of an existing one, you will be awarded additional fame.'),
    ('ARCHITECTURE', 'cs', 'Kdykoli postavíš novou budovu nebo vylepšíš nějakou stávající, tak obdržíš slávu navíc.'),
    ('BOASTING', 'en', 'Whenever your character wins a combat, you will gain additional fame.'),
    ('BOASTING', 'cs', 'Kdykoli tvá herní postava zvítězí v souboji, dostaneš slávu navíc.'),
    ('COLLECTING', 'en', 'Whenever you buy an item, you will pay 10 extra caps (if possible). If you do, 3 fame will be awarded to your clan. This effect triggers during turn evaluation (both extra caps payment and fame income).'),
    ('COLLECTING', 'cs', 'Kdykoli zakoupíš předmět na tržišti, bude ti odečteno 10 zátek navíc (pokud to je možné). Stane-li se tak, obdržíš 3 slávy navíc. Tento efekt se vyhodnocuje při přepočtu kola (platba extra zátek i příjem slávy).'),
    ('JUNKSMITHERY', 'en', 'Whenever you craft an item, you will pay 5 extra junk (if possible). If you do, 3 fame will be awarded to your clan.'),
    ('JUNKSMITHERY', 'cs', 'Kdykoli vyrobíš předmět, bude ti odečteno 5 šrotu navíc (pokud to je možné). Stane-li se tak, obdržíš 3 slávy navíc.'),
    ('CULINARY', 'en', 'At the end of every turn, your clan will receive 1 fame for every 30 food you own (max 5 per turn). This effect is calculated after food consumption.'),
    ('CULINARY', 'cs', 'Tvůj klan dostane 1 slávu na koci každého kola za každých 30 jídel, které máš v zásobě (maximum 5 za kolo). Tento efekt se počítá po konzumaci jídla.'),
    ('DECORATION', 'en', 'At the end of every turn, your clan will receive 1 fame for every 30 junk you own (max 5 per turn).'),
    ('DECORATION', 'cs', 'Tvůj klan dostane 1 slávu na konci každého kola za každých 30 šrotů, které máš v zásobě (maximum 5 za kolo).');
