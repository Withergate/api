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