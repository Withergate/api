-- Research details
INSERT INTO research_details(identifier, information_level, cost, image_url) VALUES
    ('FORGERY', 0, 20, 'https://storage.googleapis.com/withergate-images/reserach/forgery.png'),
    ('BEGGING', 0, 20, 'https://storage.googleapis.com/withergate-images/buildings/begging.png'),
    ('ARCHITECTURE', 2, 35, 'https://storage.googleapis.com/withergate-images/buildings/architecture.png'),
    ('CULINARY', 4, 50, 'https://storage.googleapis.com/withergate-images/buildings/culinary.png'),
    ('DECORATION', 4, 50, 'https://storage.googleapis.com/withergate-images/buildings/decoration.png');

INSERT INTO localized_texts(research_name, lang, text) VALUES
    ('FORGERY', 'en', 'Forgery'),
    ('FORGERY', 'cs', 'Padělání'),
    ('BEGGING', 'en', 'Begging'),
    ('BEGGING', 'cs', 'Žebrání'),
    ('ARCHITECTURE', 'en', 'Wasteland architecture'),
    ('ARCHITECTURE', 'cs', 'Architektura Pustiny'),
    ('CULINARY', 'en', 'Culinary'),
    ('CULINARY', 'cs', 'Kulinářství'),
    ('DECORATION', 'en', 'Decoration'),
    ('DECORATION', 'cs', 'Dekorace');

INSERT INTO localized_texts(research_description, lang, text) VALUES
    ('FORGERY', 'en', 'There is always some time left after work. We have some spare in sheets in the storage. Perhaps, we could craft some caps out of it. Nobody would notice the difference...'),
    ('FORGERY', 'cs', 'Po práci občas zbude čas a máme v dílně staré kusy plechu. S trochou šikovnosti by se z nich dalo vyrobit pár zátek. Určitě si nikdo ničeho nevšimne.'),
    ('BEGGING', 'en', 'If we manage to look hungry and desperate enough, people might give us some food.'),
    ('BEGGING', 'cs', 'Stačí se tvářit trochu hladově a zoufale a lidé nám určitě dají nějaké jídlo.'),
    ('ARCHITECTURE', 'en', 'When we build something, let`s make it look nice. And it should also withstand the harsh Wasteland conditions.'),
    ('ARCHITECTURE', 'cs', 'Když už něco postavíme, tak by to taky mohlo aspoň nějak esteticky vypadat... a taky to hned nespadne.'),
    ('CULINARY', 'en', 'True chef can prepare a feast from anything Wasteland provides. It might not receive a Michelin star but can taste damn good anyway.'),
    ('CULINARY', 'cs', 'Skutečný šéfkuchař dokáže připravit hostinu ze všeho, co dá Pustina. Sice to možná nedostane Michelinskou hvězdičku, ale i tak to může chutnat zatraceně dobře.'),
    ('DECORATION', 'en', 'It might be just junk but with some time and effort, we can decorate our base to make it more cozy.'),
    ('DECORATION', 'cs', 'Je to sice jen šrot, ale s trochou času a úsilí s ním můžeme ozdobit a zútulnit naši bázi.');

INSERT INTO localized_texts(research_info, lang, text) VALUES
    ('FORGERY', 'en', 'Your characters will receive a small amount of caps when crafting an item.'),
    ('FORGERY', 'cs', 'Tvé postavy dostanou malé množství zátek při výrobě předmětu.'),
    ('BEGGING', 'en', 'Your characters will receive a small amount of food when scouting for information. This does not apply when an encounter is triggered.'),
    ('BEGGING', 'cs', 'Tvé postavy dostanou malé množství jídla při pátrání po informacích. Tento efekt platí pouze, pokud nedojde k náhodné události.'),
    ('ARCHITECTURE', 'en', 'Whenever you build a new building or increase a level of an existing one, you will be awarded additional fame.'),
    ('ARCHITECTURE', 'cs', 'Kdykoli postavíš novou budovu nebo vylepšíš nějakou stávající, tak obdržíš slávu navíc.'),
    ('CULINARY', 'en', 'At the end of every turn, your clan will receive 1 fame for every 20 food you own. This effect is calculated after food consumption.'),
    ('CULINARY', 'cs', 'Tvůj klan dostane 1 slávu na koci každého kola za každých 20 jídle, které máš v zásobě. Tento efekt se počítá po konzumaci jídla.'),
    ('DECORATION', 'en', 'At the end of every turn, your clan will receive 1 fame for every 20 junk you own.'),
    ('DECORATION', 'cs', 'Tvůj klan dostane 1 slávu na koci každého kola za každých 20 šrotů, které máš v zásobě.');