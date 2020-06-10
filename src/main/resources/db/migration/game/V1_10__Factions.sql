-- Revolution
INSERT INTO factions (identifier, image_url, icon_url) VALUES
    ('REVOLUTION', 'https://storage.googleapis.com/withergate-images/factions/revolution.png', 'https://storage.googleapis.com/withergate-images/factions/revolution-icon.png');

INSERT INTO localized_texts (faction_name, lang, text) VALUES
    ('REVOLUTION', 'en', 'De Fenestra'),
    ('REVOLUTION', 'cs', 'De Fenestra');

INSERT INTO localized_texts (faction_description, lang, text) VALUES
    ('REVOLUTION', 'en', 'Some people do not believe in progress. Those are the exact people that were afraid of big inventions and were not able to control them. They were the real cause of the end of our civilization. They still walk amongst us - ruling the city, pacting with Atomists... We have to take them down. There is no time for talking at the Clan Assembly or waiting for King beyond the Wasteland to come and rescue us. Let us overthrow the corrupt honoration and create a true democracy, like the one from before the collapse.'),
    ('REVOLUTION', 'cs', 'Nejhorší na světě jsou zpátečníci, kteří nevěří v pokrok. Přesně takoví lidé, kteří se báli velkých vynálezů a neuměli je správně využít, způsobili zkázu civilizace. A jsou stále mezi námi - vládnou městu, vládnou Pustině a společně s atomisty ovládají vše... Tomu je třeba učinit přítrž. Není čas na dlouhé debaty na sněmu klanů, není čas čekat na příchod Krále za Pustinou, pojďme společně svrhnout zkorumpovanou honoraci a nastolit opravdovou vládu lidu, jaká dle legend existovala před dávnými věky.');

INSERT INTO faction_aids (identifier, faction, aid_type, in_lead, caps_cost, faction_points_cost, aid, num_aid, health_cost, item_cost, faction_points, fame) VALUES
    ('r.item', 'REVOLUTION', 'FACTION_SUPPORT', false, 0, 0, 0, 0, false, 'ANY', 10, 0),
    ('r.resources', 'REVOLUTION', 'RESOURCE_SUPPORT', false, 12, 0, 1, 3, false, null, 10, 1),
    ('r.leading', 'REVOLUTION', 'HEALING_REWARD', true, 0, 20, 10, 0, false, null, 0, 0);

INSERT INTO localized_texts (faction_aid, lang, text) VALUES
    ('r.item', 'en', 'Revolution support - hando ver an item for the Revolution'),
    ('r.item', 'cs', 'Podpora revoluce - odevzdat předmět do skladu'),
    ('r.resources', 'en', 'Support our faction members with resources'),
    ('r.resources', 'cs', 'Podpořit naše spolubojovníky surovinami'),
    ('r.leading', 'en', 'Rest in the exclusive medi-tent in the faction camp. This action will recover high amount of health to the character.'),
    ('r.leading', 'cs', 'Polenošit ve specielním medi-stanu naší frakce. Tato akce vyléčí postavě vysoký počet životů.');

INSERT INTO quest_details (identifier, quest_type, quest_condition, difficulty, completion, caps_reward, fame_reward, faction_reward, food_cost, junk_cost, item_cost, health_cost, faction_specific, follow_up, faction, image_url) VALUES
    ('quest.r1', 'AUTOMATIC', null, 0, 5, 10, 5, 5, 0, 0, null, false, true, 'quest.r2', 'REVOLUTION', 'https://storage.googleapis.com/withergate-images/quests/quest-r1.png'),
    ('quest.r2', 'INTELLECT_LOW', null, 5, 2, 20, 10, 10, 0, 0, null, false, true, 'quest.r3', null, 'https://storage.googleapis.com/withergate-images/quests/quest-r2.png'),
    ('quest.r3', 'CRAFTSMANSHIP', null, 6, 3, 30, 15, 15, 0, 5, null, false, true, 'quest.r4', null, 'https://storage.googleapis.com/withergate-images/quests/quest-r3.png'),
    ('quest.r4', 'INTELLECT', null, 8, 5, 40, 20, 20, 0, 0, null, false, true, null, null, 'https://storage.googleapis.com/withergate-images/quests/quest-r4.png');

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

INSERT INTO faction_aids (identifier, faction, aid_type, in_lead, caps_cost, faction_points_cost, aid, num_aid, health_cost, item_cost, faction_points, fame) VALUES
    ('c.sacrifice', 'CULT', 'FACTION_SUPPORT', false, 0, 0, 0, 0, true, null, 10, 0),
    ('c.resources', 'CULT', 'RESOURCE_SUPPORT', false, 12, 0, 1, 3, false, null, 10, 1),
    ('c.leading', 'CULT', 'ITEM_REWARD', true, 0, 20, 0, 0, false, null, 0, 0);

INSERT INTO localized_texts (faction_aid, lang, text) VALUES
    ('c.sacrifice', 'en', 'Flagellum dei - Spend the day with a fruitful flagellation'),
    ('c.sacrifice', 'cs', 'Flagellum dei - strávit den plodným sebemrskačstvím'),
    ('c.resources', 'en', 'Support our faction members with resources'),
    ('c.resources', 'cs', 'Podpořit naše spolubojovníky surovinami'),
    ('c.leading', 'en', 'Take some ritual items from our sanctuary. This action will grant a random rare item.'),
    ('c.leading', 'cs', 'Odnést si ze svatyně nějakou obětinu. Tato akce ti poskytne náhodný vzácný předmět.');

INSERT INTO quest_details (identifier, quest_type, quest_condition, difficulty, completion, caps_reward, fame_reward, faction_reward, food_cost, junk_cost, item_cost, health_cost, faction_specific, follow_up, faction, image_url) VALUES
    ('quest.c1', 'AUTOMATIC', null, 0, 3, 10, 5, 5, 0, 0, null, true, true, 'quest.c2', 'CULT', 'https://storage.googleapis.com/withergate-images/quests/quest-c1.png'),
    ('quest.c2', 'AUTOMATIC', null, 0, 2, 20, 10, 10, 0, 0, 'ANY', false, true, 'quest.c3', null, 'https://storage.googleapis.com/withergate-images/quests/quest-c2.png'),
    ('quest.c3', 'INTELLECT', null, 6, 3, 30, 15, 15, 5, 0, null, false, true, 'quest.c4', null, 'https://storage.googleapis.com/withergate-images/quests/quest-c3.png'),
    ('quest.c4', 'CRAFTSMANSHIP_LOW', null, 5, 3, 40, 20, 20, 0, 0, null, false, true, null, null, 'https://storage.googleapis.com/withergate-images/quests/quest-c4.png');

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

-- Syndicate
INSERT INTO factions (identifier, image_url, icon_url) VALUES
    ('SYNDICATE', 'https://storage.googleapis.com/withergate-images/factions/syndicate.png', 'https://storage.googleapis.com/withergate-images/factions/syndicate-icon.png');

INSERT INTO localized_texts (faction_name, lang, text) VALUES
    ('SYNDICATE', 'en', 'The Web'),
    ('SYNDICATE', 'cs', 'Síť');

INSERT INTO localized_texts (faction_description, lang, text) VALUES
    ('SYNDICATE', 'en', 'Everything has a price. Everyone can be bought. And the price of human life has dropped so low lately… We are doing the same as everyone - gathering wealth and power - the difference is we`re organized and successful. We have followers or spies in each clan, in each faction... sometimes they even do not have a clue. We let them win a few caps playing dice and in the meantime they give away their clans` secrets. We are the spiders web. If you fall, we will catch you (for a few caps). If you``re on to success, we``ll be there to make sure it`s a great one. We offer endless possibilities. You can arm the fanatics, you can arm the revolutionaries... once they end up dead, it is us with their caps in our pockets who will prevail.'),
    ('SYNDICATE', 'cs', 'Všechno má svou cenu. Každý se dá koupit. A po Apokalypse je cena lidského života tak nízká... Děláme jen to, o co se snaží každý - hromadit bohatství a moc. Rozdíl je ale v tom, že my jsme organizovaní a úspěšní. Není frakce, není klanu, kde bychom neměli sympatizanta nebo alespoň špeha. Nevěříš? A pamatuješ, jak jsi nedávno podezřele snadno vyhrál těch pár zátek? Tajemství svého klanu, co jsi nám nevědomky vyklopil, nám vydělá mnohem víc. Přesně takhle my fungujeme. My jsme Síť, v dobrém i ve zlém... Nedaří se ti? Za pár zátek pomůžeme... Daří se ti? Díky nám se ti bude dařit o to víc. Otevřeme ti nové možnosti. Rádi vyzbrojíme náboženské fanatiky i zaslepené revolucionáře... ať se klidně pozabíjí, my jen zinkasujeme zátky.');

INSERT INTO faction_aids (identifier, faction, aid_type, in_lead, caps_cost, faction_points_cost, aid, num_aid, information_cost, item_cost, faction_points, fame) VALUES
    ('s.information', 'SYNDICATE', 'FACTION_SUPPORT', false, 0, 0, 0, 0, 5, null, 10, 0),
    ('s.resources', 'SYNDICATE', 'RESOURCE_SUPPORT', false, 12, 0, 1, 3, 0, null, 10, 1),
    ('s.leading', 'SYNDICATE', 'CAPS_REWARD', true, 0, 20, 20, 0, 0, null, 0, 0);

INSERT INTO localized_texts (faction_aid, lang, text) VALUES
    ('s.information', 'en', 'Share some unique information with our leadership'),
    ('s.information', 'cs', 'Mluviti stříbro, nemlčeti zlato'),
    ('s.resources', 'en', 'Support our faction members with resources'),
    ('s.resources', 'cs', 'Podpořit naše spolubojovníky surovinami'),
    ('s.leading', 'en', 'Beg for some change. This action will grant you 20 caps.'),
    ('s.leading', 'cs', 'Vyžebrat pár zátek z frakční kasy. Tato akce ti poskytne 20 zátek.');

INSERT INTO quest_details (identifier, quest_type, quest_condition, difficulty, completion, caps_reward, fame_reward, faction_reward, food_cost, junk_cost, caps_cost, item_cost, health_cost, faction_specific, follow_up, faction, image_url) VALUES
    ('quest.s1', 'INTELLECT', null, 5, 2, 0, 5, 5, 0, 0, 10, null, false, true, 'quest.s2', 'SYNDICATE', 'https://storage.googleapis.com/withergate-images/quests/quest-s1.png'),
    ('quest.s2', 'AUTOMATIC', null, 0, 2, 20, 10, 10, 0, 0, 0, 'ANY', false, true, 'quest.s3', null, 'https://storage.googleapis.com/withergate-images/quests/quest-s2.png'),
    ('quest.s3', 'CRAFTSMANSHIP', null, 8, 3, 30, 15, 15, 0, 3, 0, null, false, true, 'quest.s4', null, 'https://storage.googleapis.com/withergate-images/quests/quest-s3.png'),
    ('quest.s4', 'AUTOMATIC', null, 0, 3, 0, 20, 20, 0, 0, 50, null, false, true, null, null, 'https://storage.googleapis.com/withergate-images/quests/quest-s4.png');

INSERT INTO localized_texts(quest_name, lang, text) VALUES
    ('quest.s1', 'en', 'Everything has a price'),
    ('quest.s1', 'cs', 'Všechno má svou cenu'),
    ('quest.s2', 'en', 'A kind request'),
    ('quest.s2', 'cs', 'Zdvořilá prosba'),
    ('quest.s3', 'en', 'One of a kind'),
    ('quest.s3', 'cs', 'Originál'),
    ('quest.s4', 'en', 'The rise of a magnate'),
    ('quest.s4', 'cs', 'Vzestup magnáta');

INSERT INTO localized_texts(quest_description, lang, text) VALUES
    ('quest.s1', 'en', 'The Syndicate is rich but that doesn`t mean that it wouldn`t want your money. A to have a share of a future profit is costly. The brighter ones definitely pay less but no one is excluded...so it``s 5 caps...are you still considering it...? Well now it``s 10...there you go, you might actually be smarter than you look.'),
    ('quest.s1', 'cs', 'Syndikát je bohatý, ale ne tak bohatý, aby nestál i o tvoje peníze. A za podíl na dalším zisku se platí. Když jsi chytřejší, možná zaplatíš míň, ale to neznamená, že se tě to netýká... 5 zátek... že váháš...? Tak 10… no vidíš, že si plácneme, nakonec nebudeš tak hloupý, jak vypadáš.'),
    ('quest.s2', 'en', 'We have been informed that an attack on the Syndicate Hall is imminent. The Syndicate kindly requests any material help your clan can provide to strengthen the defences of the Hall...and you may be sure, that if the Syndicate kindly requests something, it is going to get it...even if there is nowhere to take.'),
    ('quest.s2', 'cs', 'Naši informátoři zjistili, že Síni Syndikátu hrozí napadení. Členové jsou zdvořile žádáni o poskytnutí materiálu na obranu Síně... a můžeš si být jist, že když o něco Syndikát zdvořile žádá, tak to taky dostane... a to i když není kde brát.'),
    ('quest.s3', 'en', 'Right now, a bunch of them are pressing charges against the Syndicate concerning some long forgotten debts. We would show utmost gratitude if you were to manufacture some "genuine" proof of the payment. Of course if the whole thing is discovered you cannot count on us backing you but there is no profit without the risk, heh?'),
    ('quest.s3', 'cs', 'Nezdá se to pravděpodobné, ale po Apokalypse se vliv právníků ještě zvětšil. A zrovna teď jich pár tlačí na Syndikát kvůli nějakým zatraceným dluhům. Kdyby se ti podařilo vytvořit "zaručeně pravý" doklad o zaplacení, byl by ti Syndikát moc vděčný... samozřejmě, že když to praskne, krýt tě nemůžeme, ale bez rizika není zisk.'),
    ('quest.s4', 'en', 'The Syndicate membership should in theory bring wealth to anyone but in reality, most of the profit is divided among the class of the most influential and wealthiest members - the magnates. It is definitely not easy to get among their ranks, it takes cunning and loads and loads of caps.'),
    ('quest.s4', 'cs', 'Členství v Syndikátu zdánlivě přinese bohatství každému... opravdové bohatství si ale mezi sebe rozděluje jen pár nejbohatších členů - magnátů. Není snadné mezi ně proniknout, vyžaduje to prohnanost a hlavně spoustu a spoustu peněz.');
