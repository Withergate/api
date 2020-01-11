-- Revolution
INSERT INTO factions (identifier, image_url, icon_url) VALUES
    ('REVOLUTION', 'https://storage.googleapis.com/withergate-images/factions/revolution.png', 'https://storage.googleapis.com/withergate-images/factions/revolution-icon.png');

INSERT INTO localized_texts (faction_name, lang, text) VALUES
    ('REVOLUTION', 'en', 'Viva la Revolution'),
    ('REVOLUTION', 'cs', 'Viva la Revolution');

INSERT INTO localized_texts (faction_description, lang, text) VALUES
    ('REVOLUTION', 'en', 'Some people do not believe in progress. Those are the exact people that were afraid of big inventions and were not able to control them. They were the real cause of the end of our civilization. They still walk amongst us - ruling the city, pacting with Atomists... We have to take them down. There is no time for talking at the Clan Assembly or waiting for King beyond the Wasteland to come and rescue us. Let us overthrow the corrupt honoration and create a true democracy, like the one from before the collapse.'),
    ('REVOLUTION', 'cs', 'Nejhorší na světě jsou zpátečníci, kteří nevěří v pokrok. Přesně takoví lidé, kteří se báli velkých vynálezů a neuměli je správně využít, způsobili zkázu civilizace. A jsou stále mezi námi - vládnou městu, vládnou Pustině a společně s atomisty ovládají vše... Tomu je třeba učinit přítrž. Není čas na dlouhé debaty na sněmu klanů, není čas čekat na příchod Krále za Pustinou, pojďme společně svrhnout zkorumpovanou honoraci a nastolit opravdovou vládu lidu, jaká dle legend existovala před dávnými věky.');

INSERT INTO faction_aids (identifier, faction, aid_type, cost, aid, num_aid, health_cost, item_cost, faction_points, fame) VALUES
    ('r.item', 'REVOLUTION', 'FACTION_SUPPORT', 0, 0, 0, false, true, 5, 0),
    ('r.resources', 'REVOLUTION', 'RESOURCE_SUPPORT', 10, 2, 3, false, false, 10, 1);

INSERT INTO localized_texts (faction_aid, lang, text) VALUES
    ('r.item', 'en', 'Revolution support - hando ver an item for the Revolution'),
    ('r.item', 'cs', 'Podpora revoluce - odevzdat předmět do skladu'),
    ('r.resources', 'en', 'Support out faction members with resources'),
    ('r.resources', 'cs', 'Podpořit naše spolubojovníky surovinami');

-- Cult
INSERT INTO factions (identifier, image_url, icon_url) VALUES
    ('CULT', 'https://storage.googleapis.com/withergate-images/factions/cult.png', 'https://storage.googleapis.com/withergate-images/factions/cult-icon.png');

INSERT INTO localized_texts (faction_name, lang, text) VALUES
    ('CULT', 'en', 'Cult of Rarraq'),
    ('CULT', 'cs', 'Rarraqův kult');

INSERT INTO localized_texts (faction_description, lang, text) VALUES
    ('CULT', 'en', 'We have to stop the ongoing mechanization and stay strong in our faith that unites us. That is the only way of restoring the world order. Atom and his priests are trying to hide the truth - the truth of our only Lord, Rarraq. His word is as old as humanity itself. The language might sound like gibberish but it surely carries big ideas.'),
    ('CULT', 'cs', 'Vše, co jsme měli, nám zničil pokrok. Ďábelské vynálezy přinesly zkázu, zmar a způsobily konec světa. Lidstvo se neponaučilo. Musíme zastavit znovunastupující mechanizaci a být silní ve víře, která nás sjednotí. Jen tak obnovíme světový řád. Atom a jeho kněží se tuto pravdu snaží zakrýt a nečinně přihlíží všeobecnému úpadku. Pravdu ale vidí pouze kněží boha Rarraqa - své učení postavili na prastarém náboženství starším než lidstvo samo. Mluví sice v divném jazyce, ale určitě to jsou samé velké myšlenky. Vlastně malé myšlenky, protože velké myšlenky jsou zlo.');

INSERT INTO faction_aids (identifier, faction, aid_type, cost, aid, num_aid, health_cost, item_cost, faction_points, fame) VALUES
    ('c.sacrifice', 'CULT', 'FACTION_SUPPORT', 0, 0, 0, true, false, 5, 0),
    ('c.resources', 'CULT', 'RESOURCE_SUPPORT', 10, 2, 3, false, false, 10, 1);

INSERT INTO localized_texts (faction_aid, lang, text) VALUES
    ('c.sacrifice', 'en', 'Flagellum dei - Spend the day with a fruitful flagellation'),
    ('c.sacrifice', 'cs', 'Flagellum dei - strávit den plodným sebemrskačstvím'),
    ('c.resources', 'en', 'Support out faction members with resources'),
    ('c.resources', 'cs', 'Podpořit naše spolubojovníky surovinami');