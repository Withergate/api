-- Research details
INSERT INTO research_details(identifier, value, information_level, cost, image_url) VALUES
    ('FORGERY', 0, 0, 20, 'https://storage.googleapis.com/withergate-images/research/forgery.png'),
    ('BEGGING', 0, 0, 20, 'https://storage.googleapis.com/withergate-images/research/begging.png'),
    ('ARCHITECTURE', 2, 2, 35, 'https://storage.googleapis.com/withergate-images/research/architecture.png'),
    ('COLLECTING', 2, 2, 35, 'https://storage.googleapis.com/withergate-images/research/collecting.png'),
    ('CULINARY', 25, 4, 50, 'https://storage.googleapis.com/withergate-images/research/culinary.png'),
    ('DECORATION', 25, 4, 50, 'https://storage.googleapis.com/withergate-images/research/decoration.png');

INSERT INTO localized_texts(research_name, lang, text) VALUES
    ('FORGERY', 'en', 'Forgery'),
    ('FORGERY', 'cs', 'Padělání'),
    ('BEGGING', 'en', 'Begging'),
    ('BEGGING', 'cs', 'Žebrání'),
    ('ARCHITECTURE', 'en', 'Wasteland architecture'),
    ('ARCHITECTURE', 'cs', 'Architektura Pustiny'),
    ('COLLECTING', 'en', 'Collecting'),
    ('COLLECTING', 'cs', 'Collecting'),
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
    ('COLLECTING', 'en', 'This is not just a "thing". This is masterpiece! Soon, our collection will be famous all around the Wasteland!'),
    ('COLLECTING', 'cs', 'Říkáš, že to je jen "věc"? To je mistrovské dílo! Naše sbírka bude brzy proslulá po celé Pustině!'),
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
    ('COLLECTING', 'en', 'Whenever you buy an item, you will pay 10 extra caps (if possible). If you do, 2 fame will be awarded to your clan. This effect triggers during turn evaluation (both extra caps payment and fame income).'),
    ('COLLECTING', 'cs', 'Kdykoli zakoupíš předmět na tržišti, bude ti odečteno 10 zátek navíc (pokud to je možné). Stane-li se tak, obdržíš 2 slávy navíc. Tento efekt se vyhodnocuje při přepočtu kola (platba extra zátek i příjem slávy).'),
    ('CULINARY', 'en', 'At the end of every turn, your clan will receive 1 fame for every 25 food you own. This effect is calculated after food consumption.'),
    ('CULINARY', 'cs', 'Tvůj klan dostane 1 slávu na koci každého kola za každých 25 jídel, které máš v zásobě. Tento efekt se počítá po konzumaci jídla.'),
    ('DECORATION', 'en', 'At the end of every turn, your clan will receive 1 fame for every 25 junk you own.'),
    ('DECORATION', 'cs', 'Tvůj klan dostane 1 slávu na konci každého kola za každých 25 šrotů, které máš v zásobě.');
