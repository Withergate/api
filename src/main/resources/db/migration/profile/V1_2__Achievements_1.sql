-- Achievements
DROP TABLE IF EXISTS achievement_details;
CREATE TABLE achievement_details (
    identifier VARCHAR(16) UNIQUE NOT NULL,
    achievement_type VARCHAR(32) NOT NULL,
    rarity VARCHAR(16) NOT NULL,
    image_url VARCHAR(256) NOT NULL,
    value_number INT,
    value_string VARCHAR(32),
    PRIMARY KEY (identifier)
);

DROP TABLE IF EXISTS achievements;
CREATE TABLE achievements (
    achievement_id BIGINT AUTO_INCREMENT,
    identifier VARCHAR(16),
    profile_id INT NOT NULL,
    award_date TIMESTAMP NOT NULL,
    PRIMARY KEY (achievement_id),
    CONSTRAINT achievement_details_fk FOREIGN KEY (identifier) REFERENCES achievement_details (identifier),
    CONSTRAINT achievement_profile_fk FOREIGN KEY (profile_id) REFERENCES profiles (profile_id),
    CONSTRAINT achievement_unique UNIQUE (profile_id, identifier)
);

-- Localized texts
DROP TABLE IF EXISTS localized_texts;
CREATE TABLE localized_texts (
    text_id BIGINT AUTO_INCREMENT,
    lang VARCHAR(4) NOT NULL,
    text VARCHAR(2048) NOT NULL,
    achievement_name VARCHAR(16),
    achievement_description VARCHAR(16),
    PRIMARY KEY (text_id),
    CONSTRAINT localized_text_achievement_name_fk FOREIGN KEY (achievement_name) REFERENCES achievement_details (identifier),
    CONSTRAINT localized_text_achievement_description_fk FOREIGN KEY (achievement_description) REFERENCES achievement_details (identifier)
);

-- Achievements data
INSERT INTO achievement_details (identifier, achievement_type, rarity, value_number, value_string, image_url) VALUES
    ('DEDICATED', 'CONSECUTIVE_LOGINS', 'COMMON', 5, null, 'https://storage.googleapis.com/withergate-images/achievements/dedicated.png'),
    ('ON_FIRE', 'CONSECUTIVE_LOGINS', 'RARE', 21, null, 'https://storage.googleapis.com/withergate-images/achievements/on-fire.png'),
    ('SUPPORTER', 'PREMIUM', 'RARE', 0, 'SILVER', 'https://storage.googleapis.com/withergate-images/achievements/supporter.png'),
    ('PATRON', 'PREMIUM', 'EPIC', 0, 'GOLD', 'https://storage.googleapis.com/withergate-images/achievements/patron.png'),
    ('ARENA_NEWBIE', 'ARENA_WINS', 'COMMON', 1, null, 'https://storage.googleapis.com/withergate-images/achievements/arena-newbie.png'),
    ('ARENA_BADASS', 'ARENA_WINS', 'RARE', 7, null, 'https://storage.googleapis.com/withergate-images/achievements/arena-badass.png'),
    ('ARENA_PHANTOM', 'ARENA_WINS', 'EPIC', 20, null, 'https://storage.googleapis.com/withergate-images/achievements/arena-phantom.png'),
    ('BOB_BUILDER', 'BUILDING_ALL', 'COMMON', 0, null, 'https://storage.googleapis.com/withergate-images/achievements/bob-builder.png'),
    ('CAMPER', 'BUILDING_DEFENSE', 'COMMON', 4, null, 'https://storage.googleapis.com/withergate-images/achievements/camper.png'),
    ('BRICK_WALL', 'BUILDING_TOP', 'RARE', 6, null, 'https://storage.googleapis.com/withergate-images/achievements/brick-wall.png'),
    ('MASTERMIND', 'RESEARCH_COUNT', 'RARE', 8, null, 'https://storage.googleapis.com/withergate-images/achievements/mastermind.png'),
    ('KEEPER_SECRETS', 'INFORMATION_LEVEL', 'COMMON', 5, null, 'https://storage.googleapis.com/withergate-images/achievements/keeper-secrets.png'),
    ('WEB_LIES', 'INFORMATION_LEVEL', 'RARE', 7, null, 'https://storage.googleapis.com/withergate-images/achievements/web-lies.png'),
    ('HORDES', 'CHARACTER_COUNT', 'EPIC', 10, null, 'https://storage.googleapis.com/withergate-images/achievements/hordes.png'),
    ('LOGISTICIAN', 'NO_STARVATION', 'COMMON', 0, null, 'https://storage.googleapis.com/withergate-images/achievements/logistician.png'),
    ('NO_WORRY', 'DISASTERS_AVERTED', 'COMMON', 0, null, 'https://storage.googleapis.com/withergate-images/achievements/no-worry.png'),
    ('STEADY', 'GAME_COUNT', 'COMMON', 3, null, 'https://storage.googleapis.com/withergate-images/achievements/steady.png'),
    ('UNWAVERING', 'GAME_COUNT', 'RARE', 8, null, 'https://storage.googleapis.com/withergate-images/achievements/unwavering.png'),
    ('HANDY', 'CRAFT_COUNT', 'COMMON', 5, null, 'https://storage.googleapis.com/withergate-images/achievements/handy.png'),
    ('ALIEXPRESS', 'CRAFT_COUNT', 'RARE', 15, null, 'https://storage.googleapis.com/withergate-images/achievements/aliexpress.png'),
    ('NO_SOW', 'ATTACK_SUCCESS_COUNT', 'RARE', 10, null, 'https://storage.googleapis.com/withergate-images/achievements/no-sow.png'),
    ('UNBENT', 'DEFENSE_SUCCESS_COUNT', 'RARE', 5, null, 'https://storage.googleapis.com/withergate-images/achievements/unbent.png'),
    ('MACKIE', 'COMBAT_KILL', 'EPIC', 0, null, 'https://storage.googleapis.com/withergate-images/achievements/mackie.png'),
    ('PUSCHKIN', 'COMBAT_DEATH', 'RARE', 0, null, 'https://storage.googleapis.com/withergate-images/achievements/puschkin.png'),
    ('LUCKY_WINNER', 'COMBAT_WIN_LUCKY', 'EPIC', 0, null, 'https://storage.googleapis.com/withergate-images/achievements/lucky-winner.png'),
    ('LUCKY_LOSER', 'COMBAT_FLEE_LUCKY', 'RARE', 0, null, 'https://storage.googleapis.com/withergate-images/achievements/lucky-loser.png'),
    ('DIBBLER', 'TRADE_PRICE_MAX', 'RARE', 0, null, 'https://storage.googleapis.com/withergate-images/achievements/dibbler.png'),
    ('RIGHT_SIDE', 'MEMBER_OF_TOP_FACTION', 'COMMON', 1, null, 'https://storage.googleapis.com/withergate-images/achievements/right-side.png'),
    ('FOLLOW_ME', 'TOP_FACTION_MEMBER', 'RARE', 0, null, 'https://storage.googleapis.com/withergate-images/achievements/follow-me.png'),
    ('MASTER', 'GAME_WINNER', 'EPIC', 0, null, 'https://storage.googleapis.com/withergate-images/achievements/master.png'),
    ('WELL_PLAYED', 'GAME_FAME', 'COMMON', 200, null, 'https://storage.googleapis.com/withergate-images/achievements/well-played.png'),
    ('GOOD_JOB', 'GAME_FAME', 'RARE', 350, null, 'https://storage.googleapis.com/withergate-images/achievements/good-job.png'),
    ('WTF', 'GAME_FAME', 'EPIC', 500, null, 'https://storage.googleapis.com/withergate-images/achievements/wtf.png'),
    ('ON_QUEST', 'QUEST_COUNT', 'COMMON', 1, null, 'https://storage.googleapis.com/withergate-images/achievements/on-quest.png'),
    ('ADVENTURER', 'QUEST_COUNT', 'RARE', 6, null, 'https://storage.googleapis.com/withergate-images/achievements/adventurer.png'),
    ('HERO', 'QUEST_COUNT', 'EPIC', 10, null, 'https://storage.googleapis.com/withergate-images/achievements/hero.png');

INSERT INTO localized_texts (achievement_name, lang, text) VALUES
    ('DEDICATED', 'en', 'Dedicated'),
    ('DEDICATED', 'cs', 'Odhodlaný'),
    ('ON_FIRE', 'en', 'On fire'),
    ('ON_FIRE', 'cs', 'Zapálený'),
    ('SUPPORTER', 'en', 'Supporter'),
    ('SUPPORTER', 'cs', 'Podporovatel'),
    ('PATRON', 'en', 'Patron'),
    ('PATRON', 'cs', 'Mecenáš'),
    ('ARENA_NEWBIE', 'en', 'Newbie'),
    ('ARENA_NEWBIE', 'cs', 'Nováček'),
    ('ARENA_BADASS', 'en', 'Arena Badass'),
    ('ARENA_BADASS', 'cs', 'Kruťák z arény'),
    ('ARENA_PHANTOM', 'en', 'Phantom of the Arena'),
    ('ARENA_PHANTOM', 'cs', 'Fantom Arény'),
    ('BOB_BUILDER', 'en', 'Bob the Builder'),
    ('BOB_BUILDER', 'cs', 'Bořek Stavitel'),
    ('CAMPER', 'en', 'Camper'),
    ('CAMPER', 'cs', 'Kemper'),
    ('BRICK_WALL', 'en', 'Another Brick in the Wall'),
    ('BRICK_WALL', 'cs', 'Another Brick in the Wall'),
    ('MASTERMIND', 'en', 'Mastermind'),
    ('MASTERMIND', 'cs', 'Heuréka'),
    ('KEEPER_SECRETS', 'en', 'Keeper of Secrets'),
    ('KEEPER_SECRETS', 'cs', 'Strážce Tajemství'),
    ('WEB_LIES', 'en', 'Web of Lies'),
    ('WEB_LIES', 'cs', 'Pavučina Lží'),
    ('HORDES', 'en', 'Hordes of Chernobyl'),
    ('HORDES', 'cs', 'Hordy z Černobylu'),
    ('LOGISTICIAN', 'en', 'Logistician'),
    ('LOGISTICIAN', 'cs', 'Svačinář'),
    ('NO_WORRY', 'en', 'No worries'),
    ('NO_WORRY', 'cs', 'V pohodě'),
    ('STEADY', 'en', 'Steady'),
    ('STEADY', 'cs', 'Spolehlivý'),
    ('UNWAVERING', 'en', 'Unwavering'),
    ('UNWAVERING', 'cs', 'Neochvějný'),
    ('HANDY', 'en', 'Handy'),
    ('HANDY', 'cs', 'Šikovný'),
    ('ALIEXPRESS', 'en', 'Aliexpress'),
    ('ALIEXPRESS', 'cs', 'Aliepxress'),
    ('NO_SOW', 'en', 'We do not sow'),
    ('NO_SOW', 'cs', 'My nesijeme'),
    ('UNBENT', 'en', 'Unbowed, Unbent, Unbroken'),
    ('UNBENT', 'cs', 'Neohnuti, Neskloněni, Nezlomeni'),
    ('MACKIE', 'en', 'Mackie Messer'),
    ('MACKIE', 'cs', 'Kajínek'),
    ('PUSCHKIN', 'en', 'Puschkin'),
    ('PUSCHKIN', 'cs', 'Puškin'),
    ('LUCKY_WINNER', 'en', 'Lucky winner'),
    ('LUCKY_WINNER', 'cs', 'Klikač'),
    ('LUCKY_LOSER', 'en', 'Lucky Loser'),
    ('LUCKY_LOSER', 'cs', 'Klikař'),
    ('DIBBLER', 'en', 'Cut-Me-Own-Throat Dibbler'),
    ('DIBBLER', 'cs', 'Kolík Aťsepicnu'),
    ('RIGHT_SIDE', 'en', 'Be on the Right Side'),
    ('RIGHT_SIDE', 'cs', 'Na Správné Straně'),
    ('FOLLOW_ME', 'en', 'Follow my Lead'),
    ('FOLLOW_ME', 'cs', 'Dělejte, co říkám'),
    ('MASTER', 'en', 'Master'),
    ('MASTER', 'cs', 'Mistr'),
    ('WELL_PLAYED', 'en', 'Well Played'),
    ('WELL_PLAYED', 'cs', 'Díky za hru!'),
    ('GOOD_JOB', 'en', 'Good Job'),
    ('GOOD_JOB', 'cs', 'Dobrá Práce'),
    ('WTF', 'en', 'WTF?!'),
    ('WTF', 'cs', 'WTF?!'),
    ('ON_QUEST', 'en', 'On a Quest'),
    ('ON_QUEST', 'cs', 'Není Zač'),
    ('ADVENTURER', 'en', 'Adventurer'),
    ('ADVENTURER', 'cs', 'Poutník'),
    ('HERO', 'en', 'Hero'),
    ('HERO', 'cs', 'Hrdina');

INSERT INTO localized_texts (achievement_description, lang, text) VALUES
    ('DEDICATED', 'en', 'Login to the game 5 days in a row.'),
    ('DEDICATED', 'cs', 'Přihlas se do hry 5 dnů v řadě.'),
    ('ON_FIRE', 'en', 'Login to the game 21 days in a row.'),
    ('ON_FIRE', 'cs', 'Přihlas se do hry 21 dnů v řadě.'),
    ('SUPPORTER', 'en', 'Get a Silver account. This can be done by contributing to the game development of via Patreon.'),
    ('SUPPORTER', 'cs', 'Získej Silver účet. Možné získat nefinanční podporou hry a nebo přes Patreon.'),
    ('PATRON', 'en', 'Get a Gold account. This can be done via Patreon.'),
    ('PATRON', 'cs', 'Získej Gold účet. Možné získat přes Patreon.'),
    ('ARENA_NEWBIE', 'en', 'Win at least one fight in the arena.'),
    ('ARENA_NEWBIE', 'cs', 'Vyhraj alespoň jeden zápas v aréně'),
    ('ARENA_BADASS', 'en', 'Win at least seven fight in the arena with a same character.'),
    ('ARENA_BADASS', 'cs', 'Vyhraj alespoň sedm zápasů v aréně se stejnou postavou.'),
    ('ARENA_PHANTOM', 'en', 'Win at least twenty fights in the arena with a same character.'),
    ('ARENA_PHANTOM', 'cs', 'Vyhraj alespoň dvacet zápasů v aréně se stejnou postavou.'),
    ('BOB_BUILDER', 'en', 'Build at least one level of every building.'),
    ('BOB_BUILDER', 'cs', 'Postav alespoň jednu úroveň od každé budovy.'),('CAMPER', 'en', 'Camper'),
    ('CAMPER', 'en', 'Build at least four levels of a defense building.'),
    ('CAMPER', 'cs', 'Postav svou obrannou budovu alespoň na čtvrtou úroveň.'),
    ('BRICK_WALL', 'en', 'Build at least six levels of a single building.'),
    ('BRICK_WALL', 'cs', 'Postav alespoň jednu ze svých budov na šestou úroveň.'),
    ('MASTERMIND', 'en', 'Complete at least eight research projects during a single game.'),
    ('MASTERMIND', 'cs', 'Vynalezni alespoň osm výzkumů během jedinného věku.'),
    ('KEEPER_SECRETS', 'en', 'Reach information level five.'),
    ('KEEPER_SECRETS', 'cs', 'Dosáhni úrovně informací pět.'),
    ('WEB_LIES', 'en', 'Reach information level seven.'),
    ('WEB_LIES', 'cs', 'Dosáhni úrovně informací sedm.'),
    ('HORDES', 'en', 'Have at least ten characters in your clan.'),
    ('HORDES', 'cs', 'Měj alespoň deset postav ve svém klanu.'),
    ('LOGISTICIAN', 'en', 'Your characters did not starve during a single era.'),
    ('LOGISTICIAN', 'cs', 'Tvé postavy ani jednou za celý věk nehladověly.'),
    ('NO_WORRY', 'en', 'Avert every disaster during a single era.'),
    ('NO_WORRY', 'cs', 'Úspěšně vyřeš všechny pohromy v jednom věku.'),
    ('STEADY', 'en', 'Play at least three games.'),
    ('STEADY', 'cs', 'Odehraj alespoň tři herní věky.'),
    ('UNWAVERING', 'en', 'Play at least eight games.'),
    ('UNWAVERING', 'cs', 'Odehraj alespoň osm herních věků.'),
    ('HANDY', 'en', 'Craft at least five items.'),
    ('HANDY', 'cs', 'Vyrob alespoň pět předmětů.'),
    ('ALIEXPRESS', 'en', 'Craft at least fifteen items.'),
    ('ALIEXPRESS', 'cs', 'Vyrob alespoň patnáct předmětů.'),
    ('NO_SOW', 'en', 'Perform at least ten successful attacks against other players.'),
    ('NO_SOW', 'cs', 'Podnikni alespoň deset úspěšných útoků proti ostatním hráčům.'),
    ('UNBENT', 'en', 'Successfully defend your clan base against at least five attacks from other players.'),
    ('UNBENT', 'cs', 'Ubraň se alespoň pěti útokům od ostatních hráčů.'),
    ('MACKIE', 'en', 'Kill another player`s character during combat.'),
    ('MACKIE', 'cs', 'Zabij v souboji postavu jiného hráče.'),
    ('PUSCHKIN', 'en', 'Lose a character during a combat.'),
    ('PUSCHKIN', 'cs', 'Přijď o postavu během souboje.'),
    ('LUCKY_WINNER', 'en', 'Win a combat with one remaining health.'),
    ('LUCKY_WINNER', 'cs', 'Vyhraj souboj s posledním zbývajícím životem.'),
    ('LUCKY_LOSER', 'en', 'Flee from combat with one remaining health.'),
    ('LUCKY_LOSER', 'cs', 'Uteč ze souboje s poledním zbývajícím životem.'),
    ('DIBBLER', 'en', 'Sell item on marketplace for double its market price.'),
    ('DIBBLER', 'cs', 'Prodej předmět na tržišti za dvojnásobek jeho tržní hodnoty.'),
    ('RIGHT_SIDE', 'en', 'Be a member of the winning faction with at least one influence point at the end of the game.'),
    ('RIGHT_SIDE', 'cs', 'Buď členem nejsilnější frakce s alespoň jedním vlivem na konci hry.'),
    ('FOLLOW_ME', 'en', 'Be the most influential clan of your faction at the end of the game..'),
    ('FOLLOW_ME', 'cs', 'Buď nejvlivnějším klanem své frakce na konci hry.'),
    ('MASTER', 'en', 'Win the whole game era.'),
    ('MASTER', 'cs', 'Vyhraj celý herní věk.'),
    ('WELL_PLAYED', 'en', 'Have at least 200 fame at the end of the game.'),
    ('WELL_PLAYED', 'cs', 'Měj na konci věku alespoň 200 slávy.'),
    ('GOOD_JOB', 'en', 'Have at least 350 fame at the end of the game.'),
    ('GOOD_JOB', 'cs', 'Měj na konci věku alespoň 350 slávy.'),
    ('WTF', 'en', 'Have at least 500 fame at the end of the game.'),
    ('WTF', 'cs', 'Měj na konci věku alespoň 500 slávy.'),
    ('ON_QUEST', 'en', 'Complete at lest one quest.'),
    ('ON_QUEST', 'cs', 'Splň alespoň jeden úkol.'),
    ('ADVENTURER', 'en', 'Complete at lest six quests.'),
    ('ADVENTURER', 'cs', 'Splň alespoň šest úkolů.'),
    ('HERO', 'en', 'Complete at least ten quests.'),
    ('HERO', 'cs', 'Splň alespoň deset úkolů.');