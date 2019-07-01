-- Disasters

-- Mutants
INSERT INTO disaster_details(identifier, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.mutants', 100, 'd.mutants.success', 'd.mutants.partialSuccess', 'd.mutants.failure', 'https://storage.googleapis.com/withergate-images/no-image.jpg');

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

INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('d.mutants.success', 'en', 'We managed to withstand the mutant attack without any damage. Long live our mighty clan!'),
    ('d.mutants.success', 'cs', 'Porazili jsme mutanty a nezaznamenali žádné vážnější škody. Můžeme si gratulovat!'),
    ('d.mutants.partialSuccess', 'en', 'We did our best to fight the mutants but they fought their way to our base. In the end, we managed to beat them but it came with a cost.'),
    ('d.mutants.partialSuccess', 'cs', 'Ačkoliv jsme se snažili, útočníci překonali naši obranu a pronikli do našeho úkrytu. Nakonec jsme je zahnali.'),
    ('d.mutants.failure', 'en', 'The mutants easily broke through our defenses and did huge amount of damage to our camp.'),
    ('d.mutants.failure', 'cs', 'Útočníci snadno pronikli do našeho úkrytu. Bez odporu loupili a škodili postavám i budovám. Ačkoliv odtáhli, z jejich nájezdu se budeme dlouho vzpamatovávat.');

INSERT INTO disaster_solutions(identifier, disaster, solution_type, difficulty, bonus, junk_cost, caps_cost, food_cost) VALUES
    ('d.mutants.s1', 'd.mutants', 'AUTOMATIC', 0, 5, 0, 0, 0),
    ('d.mutants.s2', 'd.mutants', 'CRAFTSMANSHIP', 5, 10, 5, 0, 0),
    ('d.mutants.s3', 'd.mutants', 'COMBAT', 4, 15, 0, 0, 0),
    ('d.mutants.s4', 'd.mutants', 'AUTOMATIC', 0, 25, 0, 50, 0);

INSERT INTO localized_texts(disaster_solution_name, lang, text) VALUES
    ('d.mutants.s1', 'en', 'Building palisades'),
    ('d.mutants.s1', 'cs', 'Stavět palisády'),
    ('d.mutants.s2', 'en', 'Traps preparation'),
    ('d.mutants.s2', 'cs', 'Připravit pasti'),
    ('d.mutants.s3', 'en', 'Surprise attack'),
    ('d.mutants.s3', 'cs', 'Preventivně zaútočit'),
    ('d.mutants.s4', 'en', 'Negotiation'),
    ('d.mutants.s4', 'cs', 'Vyjednávat');

INSERT INTO localized_texts(disaster_solution_description, lang, text) VALUES
    ('d.mutants.s1', 'en', 'Everybody can help with building palisades to increase our defences.'),
    ('d.mutants.s1', 'cs', 'Každý může přiložit ruku k dílu a ze všeho možného stavět opevnění.'),
    ('d.mutants.s2', 'en', 'Those skillful enough can prepare deadly traps around our base. The success of this action depends on the character`s craftsmanship.'),
    ('d.mutants.s2', 'cs', 'Ti zručnější z nás mohou okolo naší základny připravit smrtící pasti. Úspěch této akce závisí na zručnosti postavy.'),
    ('d.mutants.s3', 'en', 'They will never expect this. A bit risky, but if we manage to kill some of them in advance, they might avoid our base. This action will trigger a combat with the risk of injury, death and failure.'),
    ('d.mutants.s3', 'cs', 'To je určitě to, co nejméně čekají. Trochu riskantní, ale pokud jich pobijeme dostatek, třeba se nám vyhnou. Akce vyvolá boj s rizikem zranění či smrti a rovněž neúspěchu.'),
    ('d.mutants.s4', 'en', 'Everything can be solved with caps. Let`s talk with a leader of some of the mutant groups.'),
    ('d.mutants.s4', 'cs', 'Zátky řeší vše, stačí si promluvit s vůdcem některé z mutantích band.');

-- Inquisition
INSERT INTO disaster_details(identifier, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.inquisition', 100, 'd.inquisition.success', 'd.inquisition.partialSuccess', 'd.inquisition.failure', 'https://storage.googleapis.com/withergate-images/no-image.jpg');

INSERT INTO disaster_penalties(identifier, disaster, penalty_type) VALUES
    ('d.inquisition.p1', 'd.inquisition', 'ITEM_LOSS'),
    ('d.inquisition.p2', 'd.inquisition', 'RESOURCES_LOSS'),
    ('d.inquisition.p3', 'd.inquisition', 'CHARACTER_INJURY');

INSERT INTO localized_texts(disaster_name, lang, text) VALUES
    ('d.inquisition', 'en', 'Atom inquisition'),
    ('d.inquisition', 'cs', 'Ozbrojení fanatici vzývající Atoma');

INSERT INTO localized_texts(disaster_description, lang, text) VALUES
    ('d.inquisition', 'en', ''),
    ('d.inquisition', 'cs', 'Ve Withergate je populární nějaké nové božstvo. Jeho kněží poslední dobou často vyráží do okolí a šíří svou víru po dobrém i po zlém. Zřejmě není dobrý nápad si je pohněvat, protože tito fanatici mají velký vliv na dění ve městě. V některých klanech konfiskovali majetek, dokonce došlo i na inkviziční procesy a prý už i hořely hranice. Nemůžeme je poslat pryč, protože by se vrátili s mnohem větší silou. Za několik dní jsou i u nás.');

INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('d.inquisition.success', 'en', ''),
    ('d.inquisition.success', 'cs', 'S Atomem je lepší být zadobře, a s jeho veleknězem Bobligem jsme si dokonale rozuměli. Pochopil, že nám se Ďábel raději vyhýbá. Nebo pochopil, že v cizích klanech se mu bude lépe konfiskovat majetek?'),
    ('d.inquisition.partialSuccess', 'en', ''),
    ('d.inquisition.partialSuccess', 'cs', 'Prý je Atom s námi spokojen, někteří z nás ale prý sešli z cesty. Kněží zahájili vyšetřování a bolestivé výslechy. Nikdo se nepřiznal, tak zkonfiskovali trochu majetku a jeli šířit své bohulibé myšlenky jinam...'),
    ('d.inquisition.failure', 'en', ''),
    ('d.inquisition.failure', 'cs', 'To bylo horší, než jsme čekali. Nenašli u nás dostatečný zápal pro víru, zato zjistili, že někteří z nás obcují s Ďáblem. Zahájili vyšetřování a bolestivé výslechy. Nikdo se nepřiznal, ale stačilo jim, že někteří z nás raději přijali víru. Kněží pak zkonfiskovali trochu majetku a jeli šířit své bohulibé myšlenky jinam...');

INSERT INTO disaster_solutions(identifier, disaster, solution_type, difficulty, bonus, junk_cost, caps_cost, food_cost) VALUES
    ('d.inquisition.s1', 'd.inquisition', 'AUTOMATIC', 0, 5, 0, 0, 0),
    ('d.inquisition.s2', 'd.inquisition', 'INTELLECT', 5, 10, 0, 0, 0),
    ('d.inquisition.s3', 'd.inquisition', 'CRAFTSMANSHIP', 6, 15, 5, 0, 0),
    ('d.inquisition.s4', 'd.inquisition', 'AUTOMATIC', 0, 20, 0, 50, 0);

INSERT INTO localized_texts(disaster_solution_name, lang, text) VALUES
    ('d.inquisition.s1', 'en', ''),
    ('d.inquisition.s1', 'cs', 'Modlit se a pracovat'),
    ('d.inquisition.s2', 'en', ''),
    ('d.inquisition.s2', 'cs', 'Vymluvit se z toho'),
    ('d.inquisition.s3', 'en', ''),
    ('d.inquisition.s3', 'cs', 'Vyrobit zaručeně pravou relikvii'),
    ('d.inquisition.s4', 'en', ''),
    ('d.inquisition.s4', 'cs', 'Uplatit... aha, oni tomu říkají odpustek');

INSERT INTO localized_texts(disaster_solution_description, lang, text) VALUES
    ('d.inquisition.s1', 'en', ''),
    ('d.inquisition.s1', 'cs', 'Možná bude stačit dát najevo svou pravou víru. Pojďme si nacvičit nějaké modlitby a rituály, které zvládne každý. Až přijdou, sehrajeme jim malé divadlo - ať mír dál zůstává s touto pustinou... Tato akce nestojí žádné zdroje ani nemá žádné prerekvizity.'),
    ('d.inquisition.s2', 'en', ''),
    ('d.inquisition.s2', 'cs', 'U nás je všechno v pořádku, ať jdou radši k sousedům. Než přijdou, rozšíříme v našem klanu dokonalou lež o ďáblech žijících poblíž. O bohatých ďáblech, jejichž majetek lze snadno zkonfiskovat a nikdo proti tomu neřekne ani “popel z hranice”. Výsledek této akce je ovlivněn intelektem postavy.'),
    ('d.inquisition.s3', 'en', ''),
    ('d.inquisition.s3', 'cs', 'Podstrčíme jim padělek svatých ostatků, kostí samotného Atoma. Po takovém daru je jasné, že mezi námi Ďábel žít nemůže. Výsledek této ake je ovlivněn zručností postavy.'),
    ('d.inquisition.s4', 'en', ''),
    ('d.inquisition.s4', 'cs', 'I víra se dá koupit.');

-- Refugees
INSERT INTO disaster_details(identifier, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.refugees', 100, 'd.refugees.success', 'd.refugees.partialSuccess', 'd.refugees.failure', 'https://storage.googleapis.com/withergate-images/no-image.jpg');

INSERT INTO disaster_penalties(identifier, disaster, penalty_type) VALUES
    ('d.refugees.p1', 'd.refugees', 'ITEM_LOSS'),
    ('d.refugees.p2', 'd.refugees', 'RESOURCES_LOSS'),
    ('d.refugees.p3', 'd.refugees', 'CHARACTER_INJURY');

INSERT INTO localized_texts(disaster_name, lang, text) VALUES
    ('d.refugees', 'en', 'Refugees'),
    ('d.refugees', 'cs', 'Utečenci');

INSERT INTO localized_texts(disaster_description, lang, text) VALUES
    ('d.refugees', 'en', ''),
    ('d.refugees', 'cs', 'Část Withergate se v posledních dnech změnila a stala se neobyvatelnou. Krajem putují skupiny neužitečných hladových mužů, žen i dětí a žádají o pomoc. Měli bychom jim pomoci v nouzi, protože i když jsou slabí, je jich mnohem více než nás, a i když zatím nejsou agresivní, jsou zoufalí a my nezvládneme uhlídat celý náš klan. Už se utábořili kousek od našich bran - snad nám nevyžerou všechny zásoby ze sousedství, než půjdou zase dál.');

INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('d.refugees.success', 'en', ''),
    ('d.refugees.success', 'cs', 'Díky naší skvěle zorganizované reakci po pár dnech odešli, aniž by způsobili jakékoliv nečekané škody.'),
    ('d.refugees.partialSuccess', 'en', ''),
    ('d.refugees.partialSuccess', 'cs', 'Došlo k několika potyčkám a v noci nám zmizely ze skladů nějaké zásoby či předměty.'),
    ('d.refugees.failure', 'en', ''),
    ('d.refugees.failure', 'cs', 'Naše řešení nezapůsobilo. Zcela se vymkli naší kontrole a brali si, co chtěli. Měli hlad, prali se s námi a pak vzali vše, na co přišli. Nejen z našich zásob, ale i z celého sousedství...');

INSERT INTO disaster_solutions(identifier, disaster, solution_type, difficulty, bonus, junk_cost, caps_cost, food_cost) VALUES
    ('d.refugees.s1', 'd.refugees', 'AUTOMATIC', 0, 5, 0, 0, 0),
    ('d.refugees.s2', 'd.refugees', 'INTELLECT', 7, 10, 0, 0, 0),
    ('d.refugees.s3', 'd.refugees', 'AUTOMATIC', 0, 20, 0, 0, 30),
    ('d.refugees.s4', 'd.refugees', 'COMBAT', 4, 20, 0, 0, 0);

INSERT INTO localized_texts(disaster_solution_name, lang, text) VALUES
    ('d.refugees.s1', 'en', ''),
    ('d.refugees.s1', 'cs', 'Vpustit je mezi nás a hlídat je'),
    ('d.refugees.s2', 'en', ''),
    ('d.refugees.s2', 'cs', 'Léčit zraněné a vyrobit jim léky'),
    ('d.refugees.s3', 'en', ''),
    ('d.refugees.s3', 'cs', 'Nakrmit je dosyta'),
    ('d.refugees.s4', 'en', ''),
    ('d.refugees.s4', 'cs', 'Zahnat je');

INSERT INTO localized_texts(disaster_solution_description, lang, text) VALUES
    ('d.refugees.s1', 'en', ''),
    ('d.refugees.s1', 'cs', 'Seženeme jim trochu jídla a vody a necháme je u nás přenocovat, ale hlavně je budeme mít alespoň trochu pod dohledem. Akce nestojí žádné zdroje ani nemá žádné prerekvizity.'),
    ('d.refugees.s2', 'en', ''),
    ('d.refugees.s2', 'cs', 'Nemohou jít dál, protože někteří z nich jsou po cestě zranění či nemocní. Základní zdravotní péči bychom jim zvládli poskytnout - o to dříve odejdou. Výsledek akce je ovlivněn intelektem postavy.'),
    ('d.refugees.s3', 'en', ''),
    ('d.refugees.s3', 'cs', 'Pokud je zcela nasytím, nebudou se nám ho snažit ukrást. Tedy... říká se, že s jídlem roste chuť.'),
    ('d.refugees.s4', 'en', ''),
    ('d.refugees.s4', 'cs', 'Ať si nás nenávidí, jen když se nás bojí. Akce vyvolá boj s rizikem zranění či smrti a rovněž neúspěchu.');

-- Storm
INSERT INTO disaster_details(identifier, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.storm', 100, 'd.storm.success', 'd.storm.partialSuccess', 'd.storm.failure', 'https://storage.googleapis.com/withergate-images/no-image.jpg');

INSERT INTO disaster_penalties(identifier, disaster, penalty_type) VALUES
    ('d.storm.p1', 'd.storm', 'BUILDING_DESTRUCTION'),
    ('d.storm.p2', 'd.storm', 'CHARACTER_INJURY'),
    ('d.storm.p3', 'd.storm', 'RESOURCES_LOSS');

INSERT INTO localized_texts(disaster_name, lang, text) VALUES
    ('d.storm', 'en', 'Storm'),
    ('d.storm', 'cs', 'Bouře');

INSERT INTO localized_texts(disaster_description, lang, text) VALUES
    ('d.storm', 'en', ''),
    ('d.storm', 'cs', 'Počasí se neustále mění. Dle výpočtů nějakých chytrých lidí z Withergate má v nejbližších dnech nastat období bouří, a dle jiných lidí z hospody - možná už ne tolik chytrých, ale stále důvěryhodných - budou ty nastávající bouře jedny z nejsilnějších vůbec. Po zevrubné inspekci okolí našeho klanu bychom si měli dát pozor na povodně, padající stromy a vůbec raději přibít vše, co není přidělané.');

INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('d.storm.success', 'en', ''),
    ('d.storm.success', 'cs', 'Člověk opět jednou triumfoval nad běsnícími živly. Ačkoliv vichřice udeřila na naše domy a přílivová voda se prohnala okolím, byli jsme připraveni a vše jsme přečkali v bezpečí, ve zdraví a bez ztrát na majetku.'),
    ('d.storm.partialSuccess', 'en', ''),
    ('d.storm.partialSuccess', 'cs', 'Vichřice udeřila na naše domy. Padající stromy v našem klanu způsobily pořádnou spoušť, kterou poté zaplavila přívalová voda. Když opadla, nechala po sobě nánosy bláta. Boj s živlem a jeho následky byl náročný na naše zdraví i náš majetek.'),
    ('d.storm.failure', 'en', ''),
    ('d.storm.failure', 'cs', 'Několik bouřkových dní udeřilo v plné síle. Vichřice rvala střechy z našich domů a lámala stromy, které v našem klanu způsobily pořádnou spoušť. Trosky zaplavila přívalová voda. Když opadla, nechala po sobě nánosy bláta a naše nyní již bývalé zásoby, plesnivé a zrezivělé… V panice při útěku před živly také došlo k několika zraněním.');

INSERT INTO disaster_solutions(identifier, disaster, solution_type, difficulty, bonus, junk_cost, caps_cost, food_cost) VALUES
    ('d.storm.s1', 'd.storm', 'AUTOMATIC', 0, 5, 0, 0, 0),
    ('d.storm.s2', 'd.storm', 'CRAFTSMANSHIP', 7, 10, 0, 0, 0),
    ('d.storm.s3', 'd.storm', 'CRAFTSMANSHIP', 5, 15, 10, 0, 0),
    ('d.storm.s4', 'd.storm', 'AUTOMATIC', 4, 20, 0, 50, 0);

INSERT INTO localized_texts(disaster_solution_name, lang, text) VALUES
    ('d.storm.s1', 'en', ''),
    ('d.storm.s1', 'cs', 'Všechno schovat'),
    ('d.storm.s2', 'en', ''),
    ('d.storm.s2', 'cs', 'Protipovodňové zábrany'),
    ('d.storm.s3', 'en', ''),
    ('d.storm.s3', 'cs', 'Použít šrot a vše zpevnit'),
    ('d.storm.s4', 'en', ''),
    ('d.storm.s4', 'cs', 'Najmout námezdníky');

INSERT INTO localized_texts(disaster_solution_description, lang, text) VALUES
    ('d.storm.s1', 'en', ''),
    ('d.storm.s1', 'cs', 'Naše vybavení a zásoby se válí všude okolo, v křehkých přístřešcích, kde je může zasypat běsnící živel... Pojďme vše uschovat pod zem a daleko od vody, a ideálně ještě přibít. Jakkoliv. Akce nestojí žádné zdroje ani nemá žádné prerekvizity.'),
    ('d.storm.s2', 'en', ''),
    ('d.storm.s2', 'cs', 'I takový potůček, jaký teče hned u nás, se může vylít z břehů a všechno spláchnout. Po troše zápolení s lopatou a hlínou zajistíme, aby se z břehu vylil někde jinde. Úspěch akce závisí na zručnosti postavy.'),
    ('d.storm.s3', 'en', ''),
    ('d.storm.s3', 'cs', 'Některá naše stavení jsou spíše chatrná. Hrozí jejich zřícení či poškození a ztráty na majetku i na zdraví. Pokud víme, jak na to, můžeme je s použitím vhodného materiálu trochu vyspravit. Úspěch akce závisí na zručnosti postavy.'),
    ('d.storm.s4', 'en', ''),
    ('d.storm.s4', 'cs', 'Někteří podnikavci z okolí tvrdí, že přesně ví, co máme udělat, a za pár zátek to pro nás i provedou. Mohli by pro nás být užiteční, než půjdou se svým bouřným uměním zase o dům dál…');
