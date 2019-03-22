-- First turn
INSERT INTO turns (turn_id) VALUES
    (1);

-- Weapon details
INSERT INTO weapon_details(item_identifier, item_name, description, rarity, weapon_type, combat, image_url) VALUES
    ('KNIFE', 'items.knife.name', 'items.knife.description', 'COMMON', 'MELEE', 1, 'https://i.ibb.co/vxtBRdS/knife.jpg'),
    ('BAT', 'items.bat.name', 'items.bat.description', 'COMMON', 'MELEE', 1, 'https://i.ibb.co/SXJ5Ywb/bat.jpg'),
    ('AXE', 'items.axe.name', 'items.axe.description', 'RARE', 'MELEE', 2, 'https://i.ibb.co/BwwPWcC/axe.jpg'),
    ('CHAINSAW', 'items.chainsaw.name', 'items.chainsaw.description', 'RARE', 'MELEE', 3, 'https://i.ibb.co/stVSRjG/chainsaw.jpg'),
    ('BOOMERANG', 'items.boomerang.name', 'items.boomerang.description', 'COMMON', 'RANGED', 1, 'https://i.ibb.co/3NdWXS5/boomerang.jpg'),
    ('THROWING_KNIFES', 'items.throwingknifes.name', 'items.throwingknifes.description', 'COMMON', 'RANGED', 2, 'https://i.ibb.co/2yjF8pr/throwing-knifes.jpg'),
    ('CROSSBOW', 'items.crossbow.name', 'items.crossbow.description', 'RARE', 'RANGED', 3, 'https://i.ibb.co/HN0yYqt/crossbow.jpg'),
    ('HAND_GUN', 'items.handgun.name', 'items.handgun.description', 'RARE', 'RANGED', 4, 'https://i.ibb.co/nCW17gy/handgun.jpg');

-- Consumable details
INSERT INTO consumable_details(item_identifier, item_name, description, rarity, effect, effect_type, image_url) VALUES
    ('SMALL_MEDKIT', 'items.smallmedkit.name', 'items.smallmedkit.description', 'COMMON', 2, 'HEALING', 'https://i.ibb.co/XC6jLZS/small-medkit.jpg'),
    ('LARGE_MEDKIT', 'items.largemedkit.name', 'items.largemedkit.description', 'RARE', 4, 'HEALING', 'https://i.ibb.co/SxYKsHv/large-medkit.jpg');

-- Building details
INSERT INTO building_details(identifier, building_name, cost, visitable, description, image_url) VALUES
    ('SICK_BAY', 'buildings.sickbay.name', 15, false, 'buildings.sickbay.description',
    'https://cdn1.imggmi.com/uploads/2019/3/19/5f8fd7d7618937d5b38537e3dc9f1b02-full.jpg'),
    ('GMO_FARM', 'buildings.gmofarm.name', 15, false, 'buildings.gmofarm.description',
    'https://cdn1.imggmi.com/uploads/2019/3/19/5f8fd7d7618937d5b38537e3dc9f1b02-full.jpg'),
    ('TRAINING_GROUNDS', 'buildings.traininggrounds.name', 20, false, 'buildings.traininggrounds.description',
    'https://cdn1.imggmi.com/uploads/2019/3/19/5f8fd7d7618937d5b38537e3dc9f1b02-full.jpg'),
    ('MONUMENT', 'buildings.monument.name', 20, false, 'buildings.monument.description',
    'https://cdn1.imggmi.com/uploads/2019/3/19/5f8fd7d7618937d5b38537e3dc9f1b02-full.jpg');


-- Location descriptions
INSERT INTO location_descriptions(location, name, description, info, image_url) VALUES
    ('NEIGHBORHOOD', 'locations.neighborhood.name', 'locations.neighborhood.description', 'locations.neighborhood.info',
    'https://image.ibb.co/gcR9Xz/vault.jpg'),
    ('WASTELAND', 'locations.wasteland.name', 'locations.wasteland.description', 'locations.wasteland.info',
    'https://image.ibb.co/dxwXkK/wasteland.jpg'),
    ('CITY_CENTER', 'locations.citycenter.name', 'locations.citycenter.description', 'locations.citycenter.info',
    'https://image.ibb.co/jVgMee/city.jpg'),
    ('TAVERN', 'locations.tavern.name', 'locations.tavern.description', 'locations.tavern.info',
    'https://image.ibb.co/iZAMZp/tavern.jpg'),
    ('ARENA', 'locations.arena.name', 'locations.arena.description', 'locations.arena.info',
    'https://image.ibb.co/edVRNK/arena.jpg');

-- Placeholder texts general
INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('location.tavern.hired', 'en', '{} went to the tavern to hire someone for your clan. After spending the evening chatting with several people, the decision fell on {}'),
    ('location.tavern.hired', 'cs', '{} šel/a do hospody s nabídkou lehce vydělaných zátek. Po několika hodinách padlo rozhodnutí na {}.'),
    ('location.junk', 'en', '{} found some junk.'),
    ('location.junk', 'cs', '{}  našel/a šrot.'),
    ('location.food', 'en', '{} found some food.'),
    ('location.food', 'cs', '{}  našel/a jídlo.'),
    ('location.loot', 'en', '{} found loot.'),
    ('location.loot', 'cs', '{} našel/a předmět.'),
    ('building.work', 'en', '{} worked on the construction of [{}]'),
    ('building.work', 'cs', '{}  pracoval/a na stavbě [{}].'),
    ('building.monument.income', 'en', 'Your [{}] generated fame for your clan.'),
    ('building.monument.income', 'cs', 'Tvůj [{}] získal slávu pro tvůj klan.'),
    ('building.gmofarm.income', 'en', 'Your [{}] generated food for your clan.'),
    ('building.gmofarm.income', 'cs', 'Na [{}] se podařilo vyprodukovat jídlo pro tvůj klan.'),
    ('building.traininggrounds.income', 'en', '{} gained experience when training at [{}].'),
    ('building.traininggrounds.income', 'cs', '{} získala zkušenosti tréninkem v [{}].'),
    ('character.starving', 'en', '{} is starving.'),
    ('character.starving', 'cs', '{} hladoví.'),
    ('character.healing', 'en', '{} has recovered some health when resting.'),
    ('character.healing', 'cs', '{} se zvedlo zdraví zdraví při odpočinku.'),
    ('character.levelup', 'en', '{} has advanced to a higher level.'),
    ('character.levelup', 'cs', '{} postoupil/a na další úroveň.'),
    ('combat.arena.description', 'en', '{} faced {} in the arena.'),
    ('combat.arena.description', 'cs', '{} se utkal/a s {} v aréně.'),
    ('combat.arena.win', 'en', '{} won the fight.'),
    ('combat.arena.win', 'cs', '{} vyhrál/a souboj.'),
    ('combat.arena.lose', 'en', '{} lost the fight.'),
    ('combat.arena.lose', 'cs', '{} prohrál/a souboj.'),
    ('combat.death', 'en', 'Unfortunately, {} died during the combat.'),
    ('combat.death', 'cs', 'Bohužel, {} zemřel/a během souboje.');

    -- Placeholder texts detail
INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('detail.character.joined', 'en', '{} joined your clan.'),
    ('detail.character.joined', 'cs', '{} se přidal/a ke tvému klanu.'),
    ('detail.character.starvationdeath', 'en', '{} died of starvation.'),
    ('detail.character.starvationdeath', 'cs', '{} zemřel/a hlady.'),
    ('detail.character.injurydeath', 'en', '{} died from suffering the injury.'),
    ('detail.character.injurydeath', 'cs', '{} podlehl/a zraněním.'),
    ('detail.healing.roll', 'en', 'Rolled {} when computing healing.'),
    ('detail.healing.roll', 'cs', 'Na kostce padlo {} při hodu na léčení.'),
    ('detail.healing.builing', 'en', 'Healing increased by {} due to [{}].'),
    ('detail.healing.building', 'cs', 'Léčba zvýšena o {} díky [{}].'),
    ('detail.combat.rolls', 'en', '{} rolled {} on combat dice. {} rolled {}.'),
    ('detail.combat.rolls', 'cs', '{} hodil při souboji {}. {} hodil {}.'),
    ('detail.combat.roundresult', 'en', '{} won the round with combat value {}. {} had combat value {}. {} lost {} health'),
    ('detail.combat.roundresult', 'cs', '{} vyhrál/a kolo souboje s bojovým číslem {}. {} měl bojové číslo {}. {} ztrácí {} životy.'),
    ('detail.combat.flee', 'en', '{} fleed the combat with {} dice roll and {}% flee chance.'),
    ('detail.combat.flee', 'cs', '{} uprchl/a ze souboje s hodem {} při šanci {}% na útěk.'),
    ('detail.item.found.equipped', 'en', '{} found [{}] and equipped it.'),
    ('detail.item.found.equipped', 'cs', '{} nalezl [{}] a přidal do své výzbroje.'),
    ('detail.item.found.storage', 'en', '{} found [{}] took it to your clan storage.'),
    ('detail.item.found.storage', 'cs', '{} nalezl [{}] a vzal tento úlovek do klanového skladu.'),
    ('detail.building.levelup', 'en', '[{}] advanced to the next level.'),
    ('detail.building.levelup', 'cs', 'Tvé budově [{}] se zvýšila úroveň.');

-- Placeholder texts encounters
INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('encounter.w.1.desc', 'en', '{} was attacked by mutants while scavenging in the ruins of a wasteland village.'),
    ('encounter.w.1.succ', 'en', '{} managed to repel the attackers and collect some of their dropped belongigs.'),
    ('encounter.w.1.fail', 'en', '{} was wounded during the combat and had to flee without having collected anything.'),
    ('encounter.w.2.desc', 'en', '{} was attacked by bandits while scavenging in wasteland.'),
    ('encounter.w.2.succ', 'en', '{} managed to overcome the opponents and stole some caps from one of the fleeing bandits.'),
    ('encounter.w.2.fail', 'en', '{} was wounded during the combat and had to flee without having collected anything.'),
    ('encounter.w.3.desc', 'en', '{} was suddenly surprised by a wandering mutated wolf during the exploration of wasteland.'),
    ('encounter.w.3.succ', 'en', '{} overcame the beast after a short combat and managed to collect some junk on the way back.'),
    ('encounter.w.3.fail', 'en', '{} was wounded during the combat and had run back home without having collected anything.'),
    ('encounter.w.4.desc', 'en', '{} was attacked by a mutated cow after passing through an open field.'),
    ('encounter.w.4.succ', 'en', '{} overcame the beast during the combat and managed to find some junk in the junkyard next to the field.'),
    ('encounter.w.4.fail', 'en', '{} was wounded during the combat and had run back home without having collected anything.'),
    ('encounter.w.5.desc', 'en', '{} saw a wounded woman in the middle of the road, crying for help.'),
    ('encounter.w.5.succ', 'en', 'After stepping a bit closer, the blood on the road did not seem very real and something was moving in the shadows. {} avoided this trap and went to collect some junk elsewhere.'),
    ('encounter.w.5.fail', 'en', 'After kneeling next to the woman, something stepped out from the nearby shadows and hit {} in the head. After regaining consciousness, some caps were found missing from the carried bag.'),
    ('encounter.w.6.desc', 'en', 'A hidden shelter was found and it seemed abandoned on the first glance. {} started exploring the hideout but was suddenly attacked by an old man who was apparently living there.'),
    ('encounter.w.6.succ', 'en', '{} overcame the inhabitant and took some of his belongings. This is no land for the weak.'),
    ('encounter.w.6.fail', 'en', 'The man turned out to be quiet dangerous and {} was wounded during the combat.'),
    ('encounter.w.7.desc', 'en', '{} encountered a group of merchants and was invited by them for a game of dice poker.'),
    ('encounter.w.7.succ', 'en', 'After playing couple of games, {} managed to outsmart the other players and left with some additional caps.'),
    ('encounter.w.7.fail', 'en', '{} lost couple of games and returned home with nothing.'),
    ('encounter.w.8.desc', 'en', 'The wind picked up speed during the exploration of wasteland. Soon enough, blizzard broke out and forced {} to run for a shelter.'),
    ('encounter.w.8.succ', 'en', '{} managed to dig a small hole and hid in it until the blizzard passed. Some junk was collected on the way back.'),
    ('encounter.w.8.fail', 'en', '{} tried to run to the nearby forest to hide under a tree but was hit by one of the falling branches.'),
    ('encounter.w.9.desc', 'en', '{} discovered a closed heavy metal chest in one of the abandonded hideouts. It was locked with something that looked like some kind of strange home-made mechanism.'),
    ('encounter.w.9.succ', 'en', '{} spent couple of hours with the chest and managed to open the lock. An item! Nice...'),
    ('encounter.w.9.fail', 'en', '{} spent couple of hours trying to open or break the mechanism but was not able to get inside.'),
    ('encounter.c.1.desc', 'en', '{} was attacked by a mutated cat while roaming through the abandoned streets of the city ruins.'),
    ('encounter.c.1.succ', 'en', '{} managed to kill the feline collected some junk on the way back home.'),
    ('encounter.c.1.fail', 'en', '{} was wounded during the combat.'),
    ('encounter.c.2.desc', 'en', '{} was attacked by a mutated dog while roaming through the abandoned streets of the city ruins.'),
    ('encounter.c.2.succ', 'en', '{} managed to kill the beast and sell it in the city market for meat.'),
    ('encounter.c.2.fail', 'en', '{} was wounded during the combat. The wound got infected and required buing a desinfection in the city market.'),
    ('encounter.c.3.desc', 'en', '{} met a junkie while searching in the streets of the city. The junkie asked for some caps and, after being rejected, pulled out a knife.'),
    ('encounter.c.3.succ', 'en', '{} overcame the attacker and took the gear he was carrying.'),
    ('encounter.c.3.fail', 'en', '{} was wounded during the combat. The wound got infected and required buing a desinfection in the city market.'),
    ('encounter.c.4.desc', 'en', 'A priest in a torn cape was met while roaming the city streets. The priest asked if {} believed in the cult of the Atom.'),
    ('encounter.c.4.succ', 'en', '{} nodded, knowing that rejection might turn out to be dangerous when speaking with fanatics. The priest said a blessing and some some junk was collected on the way back.'),
    ('encounter.c.4.fail', 'en', '{} did not know anything about such cult so cursed and tried to leave the strange man behind. Maybe it was just a strange coincidence, but nothing was found that day.');

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

-- Random names
INSERT INTO name_prefixes (value) VALUES
    ('Quick'),
    ('Skinny'),
    ('Big'),
    ('Loud'),
    ('Dancing'),
    ('Smelly'),
    ('Strong'),
    ('Foxy'),
    ('Clever'),
    ('Silent'),
    ('Good'),
    ('Captain'),
    ('Acid'),
    ('Fierce'),
    ('Spotty'),
    ('Faithful'),
    ('Troubled'),
    ('Mad'),
    ('Lunatic'),
    ('Hungry'),
    ('Thirsty'),
    ('General'),
    ('Groovy'),
    ('Salty'),
    ('Mean'),
    ('Hysterical'),
    ('Cold'),
    ('Plastic'),
    ('Black'),
    ('Rotting');

INSERT INTO names (gender, value) VALUES
    ('MALE', 'Nick'),
    ('MALE', 'Kevin'),
    ('MALE', 'Joe'),
    ('MALE', 'Bob'),
    ('MALE', 'Petee'),
    ('MALE', 'John'),
    ('MALE', 'Sam'),
    ('MALE', 'Rufus'),
    ('MALE', 'Igor'),
    ('MALE', 'Dan'),
    ('MALE', 'Frank'),
    ('MALE', 'James'),
    ('MALE', 'Simon'),
    ('MALE', 'Hugo'),
    ('MALE', 'Dexter'),
    ('MALE', 'Tom'),
    ('MALE', 'Paul'),
    ('MALE', 'Martin'),
    ('MALE', 'Luke'),
    ('MALE', 'Zack'),
    ('MALE', 'Worm'),
    ('MALE', 'Atlas'),
    ('MALE', 'Zero'),
    ('MALE', 'Viper'),
    ('MALE', 'Exo'),
    ('MALE', 'Fiddles'),
    ('MALE', 'Patch'),
    ('MALE', 'Smuggy'),
    ('MALE', 'Patches'),
    ('MALE', 'Weeds'),
    ('MALE', 'Spot'),
    ('MALE', 'Grub'),
    ('MALE', 'Hog'),
    ('FEMALE', 'Angie'),
    ('FEMALE', 'Tamara'),
    ('FEMALE', 'Fox'),
    ('FEMALE', 'Misty'),
    ('FEMALE', 'Ember'),
    ('FEMALE', 'Vyolet'),
    ('FEMALE', 'Julia'),
    ('FEMALE', 'Carol'),
    ('FEMALE', 'Witch'),
    ('FEMALE', 'Enigma'),
    ('FEMALE', 'Moon'),
    ('FEMALE', 'Rascal'),
    ('FEMALE', 'Snow-white'),
    ('FEMALE', 'Toots'),
    ('FEMALE', 'Chance'),
    ('FEMALE', 'Muse'),
    ('FEMALE', 'Faythe'),
    ('FEMALE', 'Sunshine'),
    ('FEMALE', 'Grace'),
    ('FEMALE', 'Flower'),
    ('FEMALE', 'Honey'),
    ('FEMALE', 'Nemo'),
    ('FEMALE', 'Pixy'),
    ('FEMALE', 'Pepper'),
    ('FEMALE', 'Piper'),
    ('FEMALE', 'Kara');

INSERT INTO avatars (avatar_id, gender, image_url) VALUES
    (1, 'MALE', 'https://i.ibb.co/pnQ12b8/male01.jpg'),
    (2, 'FEMALE', 'https://i.ibb.co/kJYvvWK/female01.jpg');

