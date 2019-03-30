-- First turn
INSERT INTO turns (turn_id) VALUES
    (1);

-- Weapon details
INSERT INTO weapon_details(identifier, rarity, weapon_type, combat, image_url) VALUES
    ('KNIFE', 'COMMON', 'MELEE', 1, 'https://storage.googleapis.com/withergate-images/items/knife.jpg'),
    ('AXE', 'RARE', 'MELEE', 2, 'https://storage.googleapis.com/withergate-images/items/axe.jpg'),
    ('BAT', 'COMMON', 'MELEE', 1, 'https://storage.googleapis.com/withergate-images/items/bat.jpg'),
    ('CHAINSAW', 'RARE', 'MELEE', 3, 'https://storage.googleapis.com/withergate-images/items/chainsaw.jpg'),
    ('BOOMERANG', 'COMMON', 'RANGED', 1, 'https://storage.googleapis.com/withergate-images/items/boomerang.jpg'),
    ('THROWING_KNIFES', 'COMMON', 'RANGED', 2, 'https://storage.googleapis.com/withergate-images/items/throwing-knifes.jpg'),
    ('CROSSBOW', 'RARE', 'RANGED', 3, 'https://storage.googleapis.com/withergate-images/items/crossbow.jpg'),
    ('HAND_GUN', 'RARE', 'RANGED', 4, 'https://storage.googleapis.com/withergate-images/items/handgun.jpg');

INSERT INTO localized_texts(weapon_name, lang, text) VALUES
    ('KNIFE', 'en', 'Knife'),
    ('KNIFE', 'cs', 'Nůž'),
    ('AXE', 'en', 'Axe'),
    ('AXE', 'cs', 'Sekera'),
    ('BAT', 'en', 'Baseball bat'),
    ('BAT', 'cs', 'Palice'),
    ('CHAINSAW', 'en', 'Chainsaw'),
    ('CHAINSAW', 'cs', 'Motorová pila'),
    ('BOOMERANG', 'en', 'Boomerang'),
    ('BOOMERANG', 'cs', 'Boomerang'),
    ('THROWING_KNIFES', 'en', 'Throwing knifes'),
    ('THROWING_KNIFES', 'cs', 'Vrhací nože'),
    ('CROSSBOW', 'en', 'Crossbow'),
    ('CROSSBOW', 'cs', 'Kuše'),
    ('HAND_GUN', 'en', 'Hand gun'),
    ('HAND_GUN', 'cs', 'Pistole');

INSERT INTO localized_texts(weapon_description, lang, text) VALUES
    ('KNIFE', 'en', 'A dull edged, rusty kitchen knife stained with blood.'),
    ('KNIFE', 'cs', 'Orezlý kuchyňský nůž od krve.'),
    ('AXE', 'en', 'One-handed axe.'),
    ('AXE', 'cs', 'Jednoruční sekera.'),
    ('BAT', 'en', 'Baseball bat full of splinters enhanced with some nails.'),
    ('BAT', 'cs', 'Baseballová pálka vylepšená několika hřebíky.'),
    ('CHAINSAW', 'en', 'Gas powered chainsaw with some fuel still left in the tank.'),
    ('CHAINSAW', 'cs', 'Benzinová motorová pila s palivem v nádrži.'),
    ('BOOMERANG', 'en', 'Wooden boomerang. With some razors attached to it.'),
    ('BOOMERANG', 'cs', 'Dřevěný boomerang s přidělanými žiletkami.'),
    ('THROWING_KNIFES', 'en', 'A set of well balanced throwing knives.'),
    ('THROWING_KNIFES', 'cs', 'Několik dobře vybalancovaných vrhacích nožů.'),
    ('CROSSBOW', 'en', 'Modern crossbows can be precise and silent at the same time.'),
    ('CROSSBOW', 'cs', 'Moderní kuše. Přesná a tichá.'),
    ('HAND_GUN', 'en', 'Hand gun. Light, practical, deadly.'),
    ('HAND_GUN', 'cs', 'Pistole. Lehká, praktická, smrtící.');

-- Consumable details
INSERT INTO consumable_details(identifier, rarity, effect, effect_type, image_url) VALUES
    ('SMALL_MEDKIT', 'COMMON', 2, 'HEALING', 'https://storage.googleapis.com/withergate-images/items/small-medkit.jpg'),
    ('LARGE_MEDKIT', 'RARE', 4, 'HEALING', 'https://storage.googleapis.com/withergate-images/items/large-medkit.jpg');

INSERT INTO localized_texts(consumable_name, lang, text) VALUES
    ('SMALL_MEDKIT', 'en', 'Small medkit'),
    ('SMALL_MEDKIT', 'cs', 'Malá lékárnička'),
    ('LARGE_MEDKIT', 'en', 'Large medkit'),
    ('LARGE_MEDKIT', 'cs', 'Velká lékárnička');

INSERT INTO localized_texts(consumable_description, lang, text) VALUES
    ('SMALL_MEDKIT', 'en', 'Basic medical equipment for providing first aid and treating minor wounds.'),
    ('SMALL_MEDKIT', 'cs', 'Malá lékárnička se základními potřebami pro první pomoc.'),
    ('LARGE_MEDKIT', 'en', 'Large bag containing all sorts of medical equipment. Useful for treating more dangerous wounds.'),
    ('LARGE_MEDKIT', 'cs', 'Velká lékárnička s vybavením na ošetření težších zranění.');

-- Building details
INSERT INTO building_details(identifier, cost, visitable, image_url) VALUES
    ('SICK_BAY', 15, false, 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('GMO_FARM', 10, false, 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('TRAINING_GROUNDS', 20, false, 'https://storage.googleapis.com/withergate-images/no-image.jpg'),
    ('MONUMENT', 20, false, 'https://storage.googleapis.com/withergate-images/no-image.jpg');

INSERT INTO localized_texts(building_name, lang, text) VALUES
    ('SICK_BAY', 'en', 'Sick bay'),
    ('SICK_BAY', 'cs', 'Marodka'),
    ('GMO_FARM', 'en', 'GMO farm'),
    ('GMO_FARM', 'cs', 'GMO farma'),
    ('TRAINING_GROUNDS', 'en', 'Training grounds'),
    ('TRAINING_GROUNDS', 'cs', 'Cvičiště'),
    ('MONUMENT', 'en', 'Monument'),
    ('MONUMENT', 'cs', 'Monument');

INSERT INTO localized_texts(building_description, lang, text) VALUES
    ('SICK_BAY', 'en', 'Feeling under the weather? Grab a bed and pull yourself together!'),
    ('SICK_BAY', 'cs', 'Je vám pod psa? Pojďte se u nás natáhnout!'),
    ('GMO_FARM', 'en', 'We finally realized how to genetically modify our animals without them growing a third head!'),
    ('GMO_FARM', 'cs', 'Teď už víme, jak těch našich pár zvířat geneticky vylepšit, aniž by jim narostla třetí hlava!'),
    ('TRAINING_GROUNDS', 'en', 'There is nothing we cant learn!'),
    ('TRAINING_GROUNDS', 'cs', 'Není nic, co by se tvůj velkolepý klan nemohl naučit!'),
    ('MONUMENT', 'en', 'Let everybody know the glory of your mighty clan by building a monument near your campsite!'),
    ('MONUMENT', 'cs', 'Ať všichni vidí, jak je náš klan úžasný!');

INSERT INTO localized_texts(building_info, lang, text) VALUES
    ('SICK_BAY', 'en', 'Each level of this building increases the hitpoints healed when resting.'),
    ('SICK_BAY', 'cs', 'Každá úroveň této budovy zvyšuje počet vyléčených životů postav, které odpočívají.'),
    ('GMO_FARM', 'en', 'Each level of this building grants free food every turn.'),
    ('GMO_FARM', 'cs', 'Každá úroveň této budovy poskytuje jídlo zdarma každé kolo.'),
    ('TRAINING_GROUNDS', 'en', 'Each level of this building grants free experience to all resting characters every turn.'),
    ('TRAINING_GROUNDS', 'cs', 'Odpočívající postavy dostanou každé kolo zkušenosti zdarma.'),
    ('MONUMENT', 'en', 'Each level of this building grants free fame every turn.'),
    ('MONUMENT', 'cs', 'Každá úroveň této budovy poskutuje slávu každé kolo.');

--- Trait details
INSERT INTO trait_details(identifier, image_url) VALUES
    ('FIGHTER', 'https://storage.googleapis.com/withergate-images/traits/fighter-trait.png'),
    ('ASCETIC', 'https://storage.googleapis.com/withergate-images/traits/ascetic-trait.png'),
    ('STRONG', 'https://storage.googleapis.com/withergate-images/traits/strong-trait.png'),
    ('BUILDER', 'https://storage.googleapis.com/withergate-images/traits/builder-trait.png');

INSERT INTO localized_texts(trait_name, lang, text) VALUES
    ('FIGHTER', 'en', 'Fighter'),
    ('FIGHTER', 'cs', 'Rváč'),
    ('ASCETIC', 'en', 'Ascetic'),
    ('ASCETIC', 'cs', 'Asketa'),
    ('STRONG', 'en', 'Strong'),
    ('STRONG', 'cs', 'Silák'),
    ('BUILDER', 'en', 'Builder'),
    ('BUILDER', 'cs', 'Stavitel');

INSERT INTO localized_texts(trait_description, lang, text) VALUES
    ('FIGHTER', 'en', 'Character with this trait has a chance to gain a combat bonus when fighting with a melee weapon.'),
    ('FIGHTER', 'cs', 'Postava s touto schopností má šanci získat bonus k bojové síle při souboji se zbraní nablízko.'),
    ('ASCETIC', 'en', 'Character with this trait does not need to consume any food at the end of each turn.'),
    ('ASCETIC', 'cs', 'Postava s touto schopností nepotřebuje žádné jídlo na konci kola.'),
    ('STRONG', 'en', 'Character with this trait can carry additional junk and food when scavenging.'),
    ('STRONG', 'cs', 'Postava s touto schopností unese více šrotu a jídla při prohledávání lokací.'),
    ('BUILDER', 'en', 'Character with this trait constructs building faster without paying extra junk for the construction.'),
    ('BUILDER', 'cs', 'Postava s touto schopností staví budovy rychleji, aniž musela platit šrot navíc.');

-- Location descriptions
INSERT INTO location_descriptions(location, scouting, description, info, image_url) VALUES
    ('NEIGHBORHOOD', false, 'locations.neighborhood.description', 'locations.neighborhood.info',
    'https://storage.googleapis.com/withergate-images/locations/neighborhood.jpg'),
    ('WASTELAND', true, 'locations.wasteland.description', 'locations.wasteland.info',
    'https://storage.googleapis.com/withergate-images/locations/wasteland.jpg'),
    ('CITY_CENTER', true, 'locations.citycenter.description', 'locations.citycenter.info',
    'https://storage.googleapis.com/withergate-images/locations/city.jpg'),
    ('TAVERN', false, 'locations.tavern.description', 'locations.tavern.info',
    'https://storage.googleapis.com/withergate-images/locations/tavern.jpg'),
    ('ARENA', false, 'locations.arena.description', 'locations.arena.info',
    'https://storage.googleapis.com/withergate-images/locations/arena.jpg');

INSERT INTO localized_texts(location_name, lang, text) VALUES
    ('NEIGHBORHOOD', 'en', 'Neighborhood'),
    ('NEIGHBORHOOD', 'cs', 'Sousedství'),
    ('WASTELAND', 'en', 'Wasteland'),
    ('WASTELAND', 'cs', 'Pustina'),
    ('CITY_CENTER', 'en', 'City center'),
    ('CITY_CENTER', 'cs', 'Centrum města'),
    ('TAVERN', 'en', 'Tavern'),
    ('TAVERN', 'cs', 'Hospoda'),
    ('ARENA', 'en', 'Arena'),
    ('ARENA', 'cs', 'Aréna');

INSERT INTO localized_texts(location_description, lang, text) VALUES
    ('NEIGHBORHOOD', 'en', 'Neighborhood is the area around your camp. It is a relatively safe place since you have been living there for quite some time. It is a safe location to search for junk and food. Do not expect to find anything too valuable, though.'),
    ('NEIGHBORHOOD', 'cs', 'Sousedství je území v okolí tvého kempu. Je to relativně klidné místo, už tady přeci jen chvíli žijete. Ideální kratochvílí v této oblasti je sbírání jídla a šrotu. Nečekejte, že tady narazíte na nějaké zázraky.'),
    ('WASTELAND', 'en', 'Wasteland is the desolated area all around you. It might seem abandonded but do not be mistaken. Other characters roam this area so searching this place can sometimes be dangerous.'),
    ('WASTELAND', 'cs', 'Pustina je vyprahlá oblast všude okolo. Může se zdát opuštěná, ale věz, že se zde nachází mnoho nebezpečí.'),
    ('CITY_CENTER', 'en', 'The ruins of the center of Withergate hides the most valuable treasures. Unofrtunately, run by local gangs and roamed by scavengers, this place can sometimes prove to be very dangerous. On the other hand, if you are lucky, you can find some useful items here.'),
    ('CITY_CENTER', 'cs', 'Ruiny centra Withergate ukrývají ty největěí poklady. Bohužel se jedná o velmi nebezpečnou oblast obývající lokálními gangy, mutanty a tím nejhorším, co pustina vyplivla. Pokud budete mít štěstí, můžete zde nalézt opravdové poklady.'),
    ('TAVERN', 'en', 'On the edge of Withergate, there is a little establishment where many wastelanders go to spend their hard-earned caps in exhange for home-made booze of discutable quality. Here, you can find scavengers that might be interested in joining your clan for certain cash. '),
    ('TAVERN', 'cs', 'Na kraji Withergate je malá hospoda, kam se stahují všechny pochybné existence utrácet své pracně vydsělané zátky. Třeba zde narazíš na někoho, kdo by se ti mohl hodit.'),
    ('ARENA', 'en', 'Glory for your best warriors and caps for your clan. Show your strenght in the arena and fight against other gladiators. Be warned, though, that this is no place for weaklings and safety is not guaranteed.'),
    ('ARENA', 'cs', 'Sláva pro vítěze a zátky pro tvůj klan. Ukaž svou sílu a utkej se s ostatními gladiátory. Měj ale na paměti, že tohle není místo pro slabochy a snadno může dojít i ke smrtelným zraněním.');

INSERT INTO localized_texts(location_info, lang, text) VALUES
    ('NEIGHBORHOOD', 'en', 'Neighborhood is a relatively safe location. The most probable outcome is finding some junk or food.'),
    ('NEIGHBORHOOD', 'cs', 'Sousedství je relativně klidná oblast. Nejpravděpodobnější výsledek průzkumu bude jídlo a šrot.'),
    ('WASTELAND', 'en', 'Wasteland has an increased chance for encountering some potentially dangerous events. However, handling such event well might lead to an interesting reward.'),
    ('WASTELAND', 'cs', 'V pustině je vyšší šance na nebezpečné události. Na druhou stranu, při úspěchu je zde také šance na vyšší odměnu. Je možné zde zjistit zajímavé informace.'),
    ('CITY_CENTER', 'en', 'City has the highest risk of encoutering dangerous events. On the other hand, it also provides higher chances for finding valuable loot.'),
    ('CITY_CENTER', 'cs', 'Centrum města má nejvyšší riziko na nebezpečné události. Pokud se vám podaří přežít, tak je zde ale i šance na vysoké odměny. Také se zde postavy mohou dozvědět hodně zajímavých informací při pátrání.'),
    ('TAVERN', 'en', 'Tavern is used for hiring new characters. Each character costs [100] caps so be prepared to have this amount ready when going to this location.'),
    ('TAVERN', 'cs', 'V hospodě máš možnost najmout nové postavu. Každá postava stojí [100] zátek, tak měj tuto sumu připravenou, než sem vyrazíš.'),
    ('ARENA', 'en', 'Only one character per day can be sent to the arena. This character will be matched with random opponent and fight. Only melee weapons are allowed. Fame and glory awarded in case of victory but characters can get injured.'),
    ('ARENA', 'cs', 'Každý den můžeš do arény poslat jednu postavu ze svého klanu. Utkáš se s náhodným protivníkem a vítěz vyhraje slávu a zátky. Jsou dovolené pouze kontaktní zbraně.');

-- Placeholder texts notifications
INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('location.tavern.hired', 'en', 'Character went to the tavern to hire someone for your clan. After spending the evening chatting with several people, the decision fell on {}'),
    ('location.tavern.hired', 'cs', 'Postava šla do hospody s nabídkou lehce vydělaných zátek. Po několika hodinách padlo rozhodnutí na {}.'),
    ('location.junk', 'en', 'Character found some junk.'),
    ('location.junk', 'cs', 'Postava  našla šrot.'),
    ('location.food', 'en', 'Character found some food.'),
    ('location.food', 'cs', 'Postava  našla jídlo.'),
    ('location.loot', 'en', 'Character found loot.'),
    ('location.loot', 'cs', 'Postava našla předmět.'),
    ('location.information', 'en', 'Character found valuable information when scouting.'),
    ('location.information', 'cs', 'Postava zjistila při pátrání cenné informace.'),
    ('building.work', 'en', 'Character worked on a construction of [].'),
    ('building.work', 'cs', 'Postava  pracovala na stavbě [].'),
    ('building.monument.income', 'en', 'Your Monument generated fame for your clan.'),
    ('building.monument.income', 'cs', 'Tvůj Monument získal slávu pro tvůj klan.'),
    ('building.gmofarm.income', 'en', 'Your GMO farm generated food for your clan.'),
    ('building.gmofarm.income', 'cs', 'Na GMO farmě se podařilo vyprodukovat jídlo pro tvůj klan.'),
    ('building.traininggrounds.income', 'en', 'Character gained experience when training at the Training grounds.'),
    ('building.traininggrounds.income', 'cs', 'Postava získala zkušenosti tréninkem na cvičišti.'),
    ('character.healing', 'en', 'Character has recovered some health when resting.'),
    ('character.healing', 'cs', 'Postavě se zvedlo zdraví zdraví při odpočinku.'),
    ('character.levelup', 'en', 'Character has advanced to a higher level.'),
    ('character.levelup', 'cs', 'Postava postoupila na další úroveň.'),
    ('clan.foodConsumption', 'en', 'Characters are consuming food.'),
    ('clan.foodConsumption', 'cs', 'Postavy konzumují jídlo.'),
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
    ('detail.character.foodConsumption', 'en', '{} consumes food.'),
    ('detail.character.foodConsumption', 'cs', '{} konzumuje jídlo.'),
    ('detail.character.injurydeath', 'en', '{} died from suffering the injury.'),
    ('detail.character.injurydeath', 'cs', '{} podlehl/a zraněním.'),
    ('detail.character.levelup.trait', 'en', '{} got a new trait.'),
    ('detail.character.levelup.trait', 'cs', '{} získal/a novou schopnost.'),
    ('detail.character.starving', 'en', '{} is starving.'),
    ('detail.character.starving', 'cs', '{} hladoví.'),
    ('detail.information.levelup', 'en', 'Your clan advanced to the next information level.'),
    ('detail.information.levelup', 'cs', 'Tvůj klan postoupil na další úroveň informací.'),
    ('detail.healing.roll', 'en', 'Rolled {} when computing healing.'),
    ('detail.healing.roll', 'cs', 'Na kostce padlo {} při hodu na léčení.'),
    ('detail.healing.building', 'en', 'Sick bay increased healing by {}.'),
    ('detail.healing.building', 'cs', 'Marodka zlepšila léčbu o {}.'),
    ('detail.combat.rolls', 'en', '{} rolled {} on combat dice. {} rolled {}.'),
    ('detail.combat.rolls', 'cs', '{} hodil/a při souboji {}. {} hodil/a {}.'),
    ('detail.combat.roundresult', 'en', '{} won the round with combat value {}. {} had combat value {}. {} lost {} health'),
    ('detail.combat.roundresult', 'cs', '{} vyhrál/a kolo souboje s bojovým číslem {}. {} měl bojové číslo {}. {} ztrácí {} životy.'),
    ('detail.combat.flee', 'en', '{} fleed the combat with {} dice roll and {}% flee chance.'),
    ('detail.combat.flee', 'cs', '{} uprchl/a ze souboje s hodem {} při šanci {}% na útěk.'),
    ('detail.item.found.equipped', 'en', '{} found [] and equipped it.'),
    ('detail.item.found.equipped', 'cs', '{} přidal/a [] do svého inventáře.'),
    ('detail.item.found.storage', 'en', '{} found [] and took it to your clan storage.'),
    ('detail.item.found.storage', 'cs', '{} nalezl/a [] a vzal/a tento úlovek do klanového skladu.'),
    ('detail.building.levelup', 'en', '[] advanced to the next level.'),
    ('detail.building.levelup', 'cs', 'Tvé budově [] se zvýšila úroveň.'),
    ('detail.trait.builder', 'en', '{} used his/her Builder trait and increased the construction speed.'),
    ('detail.trait.builder', 'cs', '{} použil svou schopnost Stavitel a rychlost stavby se zvýšila.'),
    ('detail.trait.fighter', 'en', '{} got a combat bonus for having a trait Fighter and fighting with a melee weapon.'),
    ('detail.trait.fighter', 'cs', '{} získal/a bonus k bojové síly za schopnost Rváč při boji se zbraní nablízko.'),
    ('detail.trait.ascetic', 'en', '{} does not need to consume any food because of his Ascetic trait.'),
    ('detail.trait.ascetic', 'cs', '{} nemusí konzumovat žádné jídlo díky své schopnosti Asketa.'),
    ('detail.trait.strong', 'en', 'The amount of the carried loot was increased since {} has a trait Strong.'),
    ('detail.trait.strong', 'cs', '{} je Silák a proto se mu podařilo odnést více kořisti.');

-- Placeholder texts encounters
INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('encounter.w.1.desc', 'en', 'Character was attacked by mutants while scavenging in the ruins of a wasteland village.'),
    ('encounter.w.1.succ', 'en', 'Character managed to repel the attackers and collect some of their dropped belongigs.'),
    ('encounter.w.1.fail', 'en', 'Character was wounded during the combat and had to flee without having collected anything.'),
    ('encounter.w.2.desc', 'en', 'Character was attacked by bandits while scavenging in wasteland.'),
    ('encounter.w.2.succ', 'en', 'Character managed to overcome the opponents and stole some caps from one of the fleeing bandits.'),
    ('encounter.w.2.fail', 'en', 'Character was wounded during the combat and had to flee without having collected anything.'),
    ('encounter.w.3.desc', 'en', 'Character was suddenly surprised by a wandering mutated wolf during the exploration of wasteland.'),
    ('encounter.w.3.succ', 'en', 'Character overcame the beast after a short combat and managed to collect some junk on the way back.'),
    ('encounter.w.3.fail', 'en', 'Character was wounded during the combat and had run back home without having collected anything.'),
    ('encounter.w.4.desc', 'en', 'Character was attacked by a mutated cow after passing through an open field.'),
    ('encounter.w.4.succ', 'en', 'Character overcame the beast during the combat and managed to find some junk in the junkyard next to the field.'),
    ('encounter.w.4.fail', 'en', 'Character was wounded during the combat and had run back home without having collected anything.'),
    ('encounter.w.5.desc', 'en', 'Character saw a wounded woman in the middle of the road, crying for help.'),
    ('encounter.w.5.succ', 'en', 'After stepping a bit closer, the blood on the road did not seem very real and something was moving in the shadows. Our hero avoided this trap and went to collect some junk elsewhere.'),
    ('encounter.w.5.fail', 'en', 'After kneeling next to the woman, something stepped out from the nearby shadows and hit our hero in the head. After regaining consciousness, some caps were found missing from the carried bag.'),
    ('encounter.w.6.desc', 'en', 'A hidden shelter was found and it seemed abandoned on the first glance. Our character started exploring the hideout but was suddenly attacked by an old man who was apparently living there.'),
    ('encounter.w.6.succ', 'en', 'Character overcame the inhabitant and took some of his belongings. This is no land for the weak.'),
    ('encounter.w.6.fail', 'en', 'The man turned out to be quiet dangerous and our character was wounded during the combat.'),
    ('encounter.w.7.desc', 'en', 'Character encountered a group of merchants and was invited by them for a game of dice poker.'),
    ('encounter.w.7.succ', 'en', 'After playing couple of games, our hero managed to outsmart the other players and left with some additional caps.'),
    ('encounter.w.7.fail', 'en', 'Character lost couple of games and returned home with nothing.'),
    ('encounter.w.8.desc', 'en', 'The wind picked up speed during the exploration of wasteland. Soon enough, blizzard broke out and forced our hero to run for a shelter.'),
    ('encounter.w.8.succ', 'en', 'Character managed to dig a small hole and hid in it until the blizzard passed. Some junk was collected on the way back.'),
    ('encounter.w.8.fail', 'en', 'Character tried to run to the nearby forest to hide under a tree but was hit by one of the falling branches.'),
    ('encounter.w.9.desc', 'en', 'Character discovered a closed heavy metal chest in one of the abandonded hideouts. It was locked with something that looked like some kind of strange home-made mechanism.'),
    ('encounter.w.9.succ', 'en', 'Character spent couple of hours with the chest and managed to open the lock. An item! Nice...'),
    ('encounter.w.9.fail', 'en', 'Character spent couple of hours trying to open or break the mechanism but was not able to get inside.'),
    ('encounter.c.1.desc', 'en', 'Character was attacked by a mutated cat while roaming through the abandoned streets of the city ruins.'),
    ('encounter.c.1.succ', 'en', 'Character managed to kill the feline collected some junk on the way back home.'),
    ('encounter.c.1.fail', 'en', 'Character was wounded during the combat.'),
    ('encounter.c.2.desc', 'en', 'Character was attacked by a mutated dog while roaming through the abandoned streets of the city ruins.'),
    ('encounter.c.2.succ', 'en', 'Character managed to kill the beast and sell it in the city market for meat.'),
    ('encounter.c.2.fail', 'en', 'Character was wounded during the combat. The wound got infected and required buing a desinfection in the city market.'),
    ('encounter.c.3.desc', 'en', 'Character met a junkie while searching in the streets of the city. The junkie asked for some caps and, after being rejected, pulled out a knife.'),
    ('encounter.c.3.succ', 'en', 'Character overcame the attacker and took the gear he was carrying.'),
    ('encounter.c.3.fail', 'en', 'Character was wounded during the combat. The wound got infected and required buing a desinfection in the city market.'),
    ('encounter.c.4.desc', 'en', 'A priest in a torn cape was met while roaming the city streets. The priest asked if our hero believed in the cult of the Atom.'),
    ('encounter.c.4.succ', 'en', 'Character nodded, knowing that rejection might turn out to be dangerous when speaking with fanatics. The priest said a blessing and some some junk was collected on the way back.'),
    ('encounter.c.4.fail', 'en', 'Character did not know anything about such cult so cursed and tried to leave the strange man behind. Maybe it was just a strange coincidence, but nothing was found that day.');

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
    (1, 'MALE', 'https://storage.googleapis.com/withergate-images/characters/male01.jpg'),
    (2, 'FEMALE', 'https://storage.googleapis.com/withergate-images/characters/female01.jpg');

