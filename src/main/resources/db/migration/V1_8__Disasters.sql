-- Disasters
INSERT INTO disaster_details(identifier, fame_reward, image_url) VALUES
    ('d.mutants', 200, 'https://storage.googleapis.com/withergate-images/no-image.jpg');

INSERT INTO disaster_penalties(identifier, disaster, penalty_type) VALUES
    ('d.mutants.p1', 'd.mutants', 'CHARACTER_INJURY'),
    ('d.mutants.p2', 'd.mutants', 'RESOURCES_LOSS'),
    ('d.mutants.p3', 'd.mutants', 'BUILDING_DESTRUCTION');

INSERT INTO localized_texts(disaster_name, lang, text) VALUES
    ('d.mutants', 'en', 'Mutant attack'),
    ('d.mutants', 'cs', 'Útok mutantů');

INSERT INTO localized_texts(disaster_description, lang, text) VALUES
    ('d.mutants', 'en', 'Hordes of mutants are roaming through the wastelands. If we don`t prepare, we will face danger of losing our resources and lives. '),
    ('d.mutants', 'cs', 'Bandy mutantů táhnou pustinou. Útočí na vše živé a loupí, kde se dá. Když se na ně dostatečně nepřipravíme, hrozí nám ztráta na životech i na majetku.');

INSERT INTO localized_texts(disaster_success_text, lang, text) VALUES
    ('d.mutants', 'en', 'We managed to withstand the mutant attack without any damage. Long live our mighty clan!'),
    ('d.mutants', 'cs', 'Porazili jsme mutanty a nezaznamenali žádné vážnější škody. Můžeme si gratulovat!');

INSERT INTO localized_texts(disaster_partial_success_text, lang, text) VALUES
    ('d.mutants', 'en', 'We did our best to fight the mutants but they fought their way to our base. In the end, we managed to beat them but it came with a cost.'),
    ('d.mutants', 'cs', 'Ačkoliv jsme se snažili, útočníci překonali naši obranu a pronikli do našeho úkrytu. Nakonec jsme je zahnali.');

INSERT INTO localized_texts(disaster_failure_text, lang, text) VALUES
    ('d.mutants', 'en', 'The mutants easily broke through our defenses and did huge amount of damage to our camp.'),
    ('d.mutants', 'cs', 'Útočníci snadno pronikli do našeho úkrytu. Bez odporu loupili a škodili postavám i budovám. Ačkoliv odtáhli, z jejich nájezdu se budeme dlouho vzpamatovávat.');

INSERT INTO disaster_solutions(identifier, disaster, solution_type, difficulty, bonus, junk_cost, caps_cost) VALUES
    ('d.mutants.s1', 'd.mutants', 'AUTOMATIC', 0, 5, 0, 0),
    ('d.mutants.s2', 'd.mutants', 'CRAFTSMANSHIP', 5, 10, 5, 0),
    ('d.mutants.s3', 'd.mutants', 'COMBAT', 4, 15, 0, 0),
    ('d.mutants.s4', 'd.mutants', 'AUTOMATIC', 0, 25, 0, 50);

INSERT INTO localized_texts(disaster_solution_name, lang, text) VALUES
    ('d.mutants.s1', 'en', 'Building palisades'),
    ('d.mutants.s1', 'cs', 'Stavba palisády'),
    ('d.mutants.s2', 'en', 'Traps preparation'),
    ('d.mutants.s2', 'cs', 'Příprava pastí'),
    ('d.mutants.s3', 'en', 'Surprise attack'),
    ('d.mutants.s3', 'cs', 'Preventivní útok'),
    ('d.mutants.s4', 'en', 'Negotiation'),
    ('d.mutants.s4', 'cs', 'Vyjednávání');

INSERT INTO localized_texts(disaster_solution_description, lang, text) VALUES
    ('d.mutants.s1', 'en', 'Everybody can help with building palisades to increase our defences.'),
    ('d.mutants.s1', 'cs', 'Každý může přiložit ruku k dílu a ze všeho možného stavět opevnění.'),
    ('d.mutants.s2', 'en', 'Those skillful enough can prepare deadly traps around our base. The success of this action depends on the character`s craftsmanship.'),
    ('d.mutants.s2', 'cs', 'Ti zručnější z nás mohou okolo naší základny připravit smrtící pasti. Úspěch této akce závisí na zručnosti postavy.'),
    ('d.mutants.s3', 'en', 'They will never expect this. A bit risky, but if we manage to kill some of them in advance, they might avoid our base. This action will trigger a combat with the risk of injury, death and failure.'),
    ('d.mutants.s3', 'cs', 'To je určitě to, co nejméně čekají. Trochu riskantní, ale pokud jich pobijeme dostatek, třeba se nám vyhnou. Akce vyvolá boj s rizikem zranění či smrti a rovněž neúspěchu.'),
    ('d.mutants.s4', 'cs', 'Everything can be solved with caps. Let`s talk with a leader of some of the mutant groups.'),
    ('d.mutants.s4', 'en', 'Zátky řeší vše, stačí si promluvit s vůdcem některé z mutantích band.');
