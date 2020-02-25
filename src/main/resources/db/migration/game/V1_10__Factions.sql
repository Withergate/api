-- Revolution
INSERT INTO factions (identifier, image_url, icon_url) VALUES
    ('REVOLUTION', 'https://storage.googleapis.com/withergate-images/factions/revolution.png', 'https://storage.googleapis.com/withergate-images/factions/revolution-icon.png');

INSERT INTO localized_texts (faction_name, lang, text) VALUES
    ('REVOLUTION', 'en', 'De Fenestra'),
    ('REVOLUTION', 'cs', 'De Fenestra');


INSERT INTO localized_texts (faction_description, lang, text) VALUES
    ('REVOLUTION', 'en', 'Some people do not believe in progress. Those are the exact people that were afraid of big inventions and were not able to control them. They were the real cause of the end of our civilization. They still walk amongst us - ruling the city, pacting with Atomists... We have to take them down. There is no time for talking at the Clan Assembly or waiting for King beyond the Wasteland to come and rescue us. Let us overthrow the corrupt honoration and create a true democracy, like the one from before the collapse.'),
    ('REVOLUTION', 'cs', 'Nejhorší na světě jsou zpátečníci, kteří nevěří v pokrok. Přesně takoví lidé, kteří se báli velkých vynálezů a neuměli je správně využít, způsobili zkázu civilizace. A jsou stále mezi námi - vládnou městu, vládnou Pustině a společně s atomisty ovládají vše... Tomu je třeba učinit přítrž. Není čas na dlouhé debaty na sněmu klanů, není čas čekat na příchod Krále za Pustinou, pojďme společně svrhnout zkorumpovanou honoraci a nastolit opravdovou vládu lidu, jaká dle legend existovala před dávnými věky.');

INSERT INTO faction_aids (identifier, faction, aid_type, cost, aid, num_aid, health_cost, item_cost, faction_points, fame) VALUES
    ('r.item', 'REVOLUTION', 'FACTION_SUPPORT', 0, 0, 0, false, 'ANY', 5, 0),
    ('r.resources', 'REVOLUTION', 'RESOURCE_SUPPORT', 12, 1, 3, false, null, 10, 1);

INSERT INTO localized_texts (faction_aid, lang, text) VALUES
    ('r.item', 'en', 'Revolution support - hando ver an item for the Revolution'),
    ('r.item', 'cs', 'Podpora revoluce - odevzdat předmět do skladu'),
    ('r.resources', 'en', 'Support our faction members with resources'),
    ('r.resources', 'cs', 'Podpořit naše spolubojovníky surovinami');

INSERT INTO quest_details (identifier, quest_type, quest_condition, difficulty, completion, caps_reward, fame_reward, faction_reward, food_cost, junk_cost, item_cost, health_cost, faction_specific, follow_up, faction, image_url) VALUES
    ('quest.r1', 'AUTOMATIC', null, 0, 6, 10, 5, 3, 0, 0, null, false, true, 'quest.r2', 'REVOLUTION', 'https://storage.googleapis.com/withergate-images/quests/quest-r1.png'),
    ('quest.r2', 'INTELLECT_LOW', null, 5, 2, 20, 10, 6, 0, 0, null, false, true, 'quest.r3', null, 'https://storage.googleapis.com/withergate-images/quests/quest-r2.png'),
    ('quest.r3', 'CRAFTSMANSHIP', null, 6, 3, 30, 15, 9, 0, 5, null, false, true, 'quest.r4', null, 'https://storage.googleapis.com/withergate-images/quests/quest-r3.png'),
    ('quest.r4', 'INTELLECT', null, 8, 6, 40, 20, 12, 0, 0, null, false, true, null, null, 'https://storage.googleapis.com/withergate-images/quests/quest-r4.png');

INSERT INTO localized_texts(quest_name, lang, text) VALUES
    ('quest.r1', 'en', 'The voice of a crowd'),
    ('quest.r1', 'cs', 'Hlas davu'),
    ('quest.r2', 'en', 'Infiltration'),
    ('quest.r2', 'cs', 'Infiltrace'),
    ('quest.r3', 'en', 'Barricades'),
    ('quest.r3', 'cs', 'Barikády'),
    ('quest.r4', 'en', 'Chain reaction'),
    ('quest.r4', 'cs', 'Řetězová reakce');

 INSERT INTO localized_texts(quest_description, lang, text) VALUES
    ('quest.r1', 'en', 'I am looking for people to come with to the City and shout about the discrimination of us Wastelanders. And who knows, maybe one day, when the Revolution finally takes place, everyone will speak about our courage and presence at the first rally.'),
    ('quest.r1', 'cs', 'Sháním lidi, kteří se mnou půjdou pořvávat hesla o diskriminaci a přehlížení nás z Pustiny. A kdo ví, až jednou uděláme ve Withergate revoluci, budou si všichni vyprávět, že jsme na té legendární demonstraci byli.'),
    ('quest.r2', 'en', 'To actually start a Revolution, we need some inner information. We have to infiltrate the self-proclaimed City council. They cannot detect us which is gonna be hard since most of the clerks are pretty dumb. If there was anyone who would fit in...?'),
    ('quest.r2', 'cs', 'Abychom mohli dělat Revoluci, musíme mít dostatek informací přímo od zdroje. Musíme infiltrovat samozvanou městskou samosprávu. Nesmí nás ale odhalit, což bude těžké, většina úředníků jsou tupci, koho tak jen poslat, aby zapadl...'),
    ('quest.r3', 'en', 'It is time we prepare ourselves for the Revolution... and every revolution needs barricades. If one is handy enough, a barricade can be built of anything...but the more we have, the higher the flag of revolution should fly!'),
    ('quest.r3', 'cs', 'Je čas se připravit na Revoluci...a každá správná revoluce potřebuje barikády. S trochou šikovnosti jde barikádu postavit téměř z čehokoliv, ale čím víc toho bude, tím výše bude revoluční vlajka vlát!'),
    ('quest.r4', 'en', 'A word here, a scornful remark there...and the thought of a revolution is spreading like a wildfire. And do not be mistaken, the Revolution absolutely needs a bit of propaganda... and a reward is a sure thing.'),
    ('quest.r4', 'cs', 'Slovo tu, pohrdavá poznámka tamhle...a myšlenka revoluce se šíří po celé Pustině. A věř, že Revoluce dobré propagandisty potřebuje... zadarmo to určitě nebude.');

-- Cult
INSERT INTO factions (identifier, image_url, icon_url) VALUES
    ('CULT', 'https://storage.googleapis.com/withergate-images/factions/cult.png', 'https://storage.googleapis.com/withergate-images/factions/cult-icon.png');

INSERT INTO localized_texts (faction_name, lang, text) VALUES
    ('CULT', 'en', 'Deus Vault'),
    ('CULT', 'cs', 'Deus Vault');

INSERT INTO localized_texts (faction_description, lang, text) VALUES
    ('CULT', 'en', 'We have to stop the ongoing mechanization and stay strong in our faith that unites us. That is the only way of restoring the world order. Atom and his priests are trying to hide the truth - the truth of our only Lord, Rarraq. His word is as old as humanity itself. The language might sound like gibberish but it surely carries big ideas.'),
    ('CULT', 'cs', 'Vše, co jsme měli, nám zničil pokrok. Ďábelské vynálezy přinesly zkázu, zmar a způsobily konec světa. Lidstvo se neponaučilo. Musíme zastavit znovunastupující mechanizaci a být silní ve víře, která nás sjednotí. Jen tak obnovíme světový řád. Atom a jeho kněží se tuto pravdu snaží zakrýt a nečinně přihlíží všeobecnému úpadku. Pravdu ale vidí pouze kněží boha Rarraqa - své učení postavili na prastarém náboženství starším než lidstvo samo. Mluví sice v divném jazyce, ale určitě to jsou samé velké myšlenky. Vlastně malé myšlenky, protože velké myšlenky jsou zlo.');

INSERT INTO faction_aids (identifier, faction, aid_type, cost, aid, num_aid, health_cost, item_cost, faction_points, fame) VALUES
    ('c.sacrifice', 'CULT', 'FACTION_SUPPORT', 0, 0, 0, true, null, 5, 0),
    ('c.resources', 'CULT', 'RESOURCE_SUPPORT', 12, 1, 3, false, null, 10, 1);

INSERT INTO localized_texts (faction_aid, lang, text) VALUES
    ('c.sacrifice', 'en', 'Flagellum dei - Spend the day with a fruitful flagellation'),
    ('c.sacrifice', 'cs', 'Flagellum dei - strávit den plodným sebemrskačstvím'),
    ('c.resources', 'en', 'Support our faction members with resources'),
    ('c.resources', 'cs', 'Podpořit naše spolubojovníky surovinami');

INSERT INTO quest_details (identifier, quest_type, quest_condition, difficulty, completion, caps_reward, fame_reward, faction_reward, food_cost, junk_cost, item_cost, health_cost, faction_specific, follow_up, faction, image_url) VALUES
    ('quest.c1', 'AUTOMATIC', null, 0, 3, 10, 5, 3, 0, 0, null, true, true, 'quest.c2', 'CULT', 'https://storage.googleapis.com/withergate-images/quests/quest-c1.png'),
    ('quest.c2', 'AUTOMATIC', null, 0, 2, 20, 10, 6, 0, 0, 'ANY', false, true, 'quest.c3', null, 'https://storage.googleapis.com/withergate-images/quests/quest-c2.png'),
    ('quest.c3', 'INTELLECT', null, 6, 3, 30, 15, 9, 5, 0, null, false, true, 'quest.c4', null, 'https://storage.googleapis.com/withergate-images/quests/quest-c3.png'),
    ('quest.c4', 'CRAFTSMANSHIP_LOW', null, 5, 3, 40, 20, 12, 0, 0, null, false, true, null, null, 'https://storage.googleapis.com/withergate-images/quests/quest-c4.png');

INSERT INTO localized_texts(quest_name, lang, text) VALUES
    ('quest.c1', 'en', 'Trial of flesh'),
    ('quest.c1', 'cs', 'Zkouška víry'),
    ('quest.c2', 'en', 'Offerings'),
    ('quest.c2', 'cs', 'Obětiny'),
    ('quest.c3', 'en', 'Believe and you shall be fed'),
    ('quest.c3', 'cs', 'Věř a víra tvá tě nasytí'),
    ('quest.c4', 'en', 'Iconoclasm'),
    ('quest.c4', 'cs', 'Ikonoklasmus');

 INSERT INTO localized_texts(quest_description, lang, text) VALUES
    ('quest.c1', 'en', '"There is no god but Rarraq and Boblig is his prophet..." Do you really mean that? Strong words but words are wind. You can only prove your true faith with blood. Let`s see yours, there`s a knife!'),
    ('quest.c1', 'cs', '"Není jiného boha než Rarraq a Boblig je jeho prorok..." Opravdu tomu věříš? Silná slova, ale pořád jen slova. Opravdová víra se dokazuje krví. Tak do toho, tady máš nůž!'),
    ('quest.c2', 'en', 'You have stumbled upon a sanctuary of Rarraq. You have noticed some pretty rare weapons and gear among the body parts and blood splotches on the altar. Only by offering something yourself can you reach a higher level of initiation, riches and glory of the heavenly Kingdom of Rarraq.'),
    ('quest.c2', 'cs', 'Narazil jsi na svatyni Rarraqa..na oltáři se vyjma částí těl a cákanců krve nachází i množství obětovaných předmětů. Jen poskytnutím oběti se ti otevře cesta k vyššímu zasvěcení, bohatství a slávě nebeského království Rarraqova.'),
    ('quest.c3', 'en', 'Let`s preach to the beggars and save their souls. With the help of Rarraq and our eloquence, success is a sure thing. And if that is not enough a full belly would sway them no doubt. I can just see our ranks expanding...'),
    ('quest.c3', 'cs', 'Což takhle poučit nějaké chudáky o víře a spasit jejich duši? S pomocí Rarraqa a trochou výmluvnosti se to podaří. A když jim nabídneme jídlo, brzy nás budou zástupy...'),
    ('quest.c4', 'en', 'Even destruction can be beautiful. And a masterfully performed obliteration is an art of sorts. Especially if it is those heretic contraptions that are being shattered. They have got us here, to the very bottom of humanity...every sprocket flying from the  device is an ode to Rarraq and a victory for mankind.'),
    ('quest.c4', 'cs', 'I v destrukci je krása. A ničit je vlastně umění. Zvlášť, když se jedná o heretické vynálezy, které nás dostaly až na samé dno. Na čím víc ozubených koleček se tohle zařízení rozletí, tím větší slávu prokážeme Rarraqovi a lidstvu...');
