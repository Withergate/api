-- Placeholder texts notifications
INSERT INTO disaster_details(identifier, fame_reward, image_url) VALUES
    ('MUTANT_ATTACK', 200, 'https://storage.googleapis.com/withergate-images/no-image.jpg');

INSERT INTO disaster_penalties(identifier, disaster, penalty_type) VALUES
    ('MUTANT_ATTACK_P1', 'MUTANT_ATTACK', 'CHARACTER_INJURY'),
    ('MUTANT_ATTACK_P2', 'MUTANT_ATTACK', 'RESOURCES_LOSS'),
    ('MUTANT_ATTACK_P3', 'MUTANT_ATTACK', 'BUILDING_DESTRUCTION');

INSERT INTO localized_texts(disaster_name, lang, text) VALUES
    ('MUTANT_ATTACK', 'en', 'Mutant attack'),
    ('MUTANT_ATTACK', 'cs', 'Útok mutantů');

INSERT INTO localized_texts(disaster_description, lang, text) VALUES
    ('MUTANT_ATTACK', 'en', 'Hordes of mutants are roaming through the wastelands. If we don`t prepare, we will face danger of losing our resources and lives. '),
    ('MUTANT_ATTACK', 'cs', 'Bandy mutantů táhnou pustinou. Útočí na vše živé a loupí, kde se dá. Když se na ně dostatečně nepřipravíme, hrozí nám ztráta na životech i na majetku.');

INSERT INTO localized_texts(disaster_success_text, lang, text) VALUES
    ('MUTANT_ATTACK', 'en', 'We managed to withstand the mutant attack without any damage. Long live our mighty clan!'),
    ('MUTANT_ATTACK', 'cs', 'Porazili jsme mutanty a nezaznamenali žádné vážnější škody. Můžeme si gratulovat!');

INSERT INTO localized_texts(disaster_partial_success_text, lang, text) VALUES
    ('MUTANT_ATTACK', 'en', 'We did our best to fight the mutants but they fought their way to our base. In the end, we managed to beat them but it came with a cost.'),
    ('MUTANT_ATTACK', 'cs', 'Ačkoliv jsme se snažili, útočníci překonali naši obranu a pronikli do našeho úkrytu. Nakonec jsme je zahnali.');

INSERT INTO localized_texts(disaster_failure_text, lang, text) VALUES
    ('MUTANT_ATTACK', 'en', 'The mutants easily broke through our defenses and did huge amount of damage to our camp.'),
    ('MUTANT_ATTACK', 'cs', 'Útočníci snadno pronikli do našeho úkrytu. Bez odporu loupili a škodili postavám i budovám. Ačkoliv odtáhli, z jejich nájezdu se budeme dlouho vzpamatovávat.');
