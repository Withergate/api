-- First turn
INSERT INTO turns (turn_id) VALUES
    (1);

-- Location descriptions
INSERT INTO location_descriptions(location, scouting, food_bonus, junk_bonus, information_bonus, encounter_chance, item_chance, description, info, image_url) VALUES
    ('NEIGHBORHOOD', false, 0, 0, 0, 0, 0, 'locations.neighborhood.description', 'locations.neighborhood.info',
    'https://storage.googleapis.com/withergate-images/locations/neighborhood.png'),
    ('WASTELAND', true, 1, 2, 0, 25, 10, 'locations.wasteland.description', 'locations.wasteland.info',
    'https://storage.googleapis.com/withergate-images/locations/wasteland.png'),
    ('CITY_CENTER', true, 2, 2, 2, 50, 25, 'locations.citycenter.description', 'locations.citycenter.info',
    'https://storage.googleapis.com/withergate-images/locations/city.png');

INSERT INTO localized_texts(location_name, lang, text) VALUES
    ('NEIGHBORHOOD', 'en', 'Neighborhood'),
    ('NEIGHBORHOOD', 'cs', 'Sousedství'),
    ('WASTELAND', 'en', 'Wasteland'),
    ('WASTELAND', 'cs', 'Pustina'),
    ('CITY_CENTER', 'en', 'City center'),
    ('CITY_CENTER', 'cs', 'Centrum města');

INSERT INTO localized_texts(location_description, lang, text) VALUES
    ('NEIGHBORHOOD', 'en', 'Neighborhood is the area around your camp. It is a relatively safe place since you have been living there for quite some time. It is a safe location to search for junk and food. Do not expect to find anything too valuable, though.'),
    ('NEIGHBORHOOD', 'cs', 'Sousedství je území v okolí tvého kempu. Je to relativně klidné místo, už tady přeci jen chvíli žijete. Ideální kratochvílí v této oblasti je sbírání jídla a šrotu. Nečekejte, že tady narazíte na nějaké zázraky.'),
    ('WASTELAND', 'en', 'Wasteland is the desolated area all around you. It might seem abandonded but do not be mistaken. Other characters roam this area so searching this place can sometimes be dangerous.'),
    ('WASTELAND', 'cs', 'Pustina je vyprahlá oblast všude okolo. Může se zdát opuštěná, ale věz, že se zde nachází mnoho nebezpečí.'),
    ('CITY_CENTER', 'en', 'The ruins of the center of Withergate hides the most valuable treasures. Unfortunately, run by local gangs and roamed by scavengers, this place can sometimes prove to be very dangerous. On the other hand, if you are lucky, you can find some useful items here.'),
    ('CITY_CENTER', 'cs', 'Ruiny centra Withergate ukrývají ty největší poklady. Jedná o velmi nebezpečnou oblast obývanou lokálními gangy, mutanty a tím nejhorším, co pustina vyplivla. Pokud budete mít štěstí, můžete zde nalézt opravdové poklady.');

INSERT INTO localized_texts(location_info, lang, text) VALUES
    ('NEIGHBORHOOD', 'en', 'Neighborhood is a relatively safe location. The most probable outcome is finding some junk or food.'),
    ('NEIGHBORHOOD', 'cs', 'Sousedství je relativně klidná oblast. Nejpravděpodobnější výsledek průzkumu bude jídlo a šrot.'),
    ('WASTELAND', 'en', 'Wasteland has an increased chance for encountering some potentially dangerous events. However, handling such event well might lead to an interesting reward.'),
    ('WASTELAND', 'cs', 'V pustině je vyšší šance na nebezpečné události. Na druhou stranu, při úspěchu je zde také šance na vyšší odměnu. Je možné zde zjistit zajímavé informace.'),
    ('CITY_CENTER', 'en', 'City has the highest risk of encoutering dangerous events. On the other hand, it also provides higher chances for finding valuable loot.'),
    ('CITY_CENTER', 'cs', 'Centrum města má nejvyšší riziko na nebezpečné události. Pokud se vám podaří přežít, tak je zde ale i šance na vysoké odměny. Také se zde postavy mohou dozvědět hodně zajímavých informací při pátrání.');

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

INSERT INTO avatars (gender, image_url) VALUES
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male01.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male02.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male03.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male04.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male05.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male06.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male07.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male08.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male09.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male10.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male11.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male12.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male13.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male14.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male15.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male16.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male17.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male18.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male19.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male20.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male21.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male22.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male23.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male24.png'),
    ('MALE', 'https://storage.googleapis.com/withergate-images/avatars/male24.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female01.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female02.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female03.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female04.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female05.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female06.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female07.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female08.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female09.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female10.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female11.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female12.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female13.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female14.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female15.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female16.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female17.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female18.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female19.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female20.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female21.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female22.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female23.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female24.png'),
    ('FEMALE', 'https://storage.googleapis.com/withergate-images/avatars/female25.png');

