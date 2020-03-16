-- Disasters

-- Mutants
INSERT INTO disaster_details(identifier, final_disaster, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.mutants', false, 15, 'd.mutants.success', 'd.mutants.partialSuccess', 'd.mutants.failure', 'https://storage.googleapis.com/withergate-images/disasters/mutants.png');

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

INSERT INTO disaster_solutions(identifier, basic, disaster, solution_type, disaster_condition, difficulty, bonus, junk_cost, caps_cost, food_cost, item_cost) VALUES
    ('d.mutants.s1', true, 'd.mutants', 'AUTOMATIC', null, 0, 5, 0, 0, 0, null),
    ('d.mutants.s2', false, 'd.mutants', 'CRAFTSMANSHIP', null, 6, 10, 5, 0, 0, null),
    ('d.mutants.s3', false, 'd.mutants', 'COMBAT', null, 5, 12, 0, 0, 0, null),
    ('d.mutants.s4', false, 'd.mutants', 'AUTOMATIC', null, 0, 20, 0, 50, 0, null);

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
INSERT INTO disaster_details(identifier, final_disaster, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.inquisition', false, 15, 'd.inquisition.success', 'd.inquisition.partialSuccess', 'd.inquisition.failure', 'https://storage.googleapis.com/withergate-images/disasters/inquisition.png');

INSERT INTO disaster_penalties(identifier, disaster, penalty_type) VALUES
    ('d.inquisition.p1', 'd.inquisition', 'ITEM_LOSS'),
    ('d.inquisition.p2', 'd.inquisition', 'RESOURCES_LOSS'),
    ('d.inquisition.p3', 'd.inquisition', 'CHARACTER_INJURY');

INSERT INTO localized_texts(disaster_name, lang, text) VALUES
    ('d.inquisition', 'en', 'Atom inquisition'),
    ('d.inquisition', 'cs', 'Ozbrojení fanatici vzývající Atoma');

INSERT INTO localized_texts(disaster_description, lang, text) VALUES
    ('d.inquisition', 'en', 'There is some kind of new deity in Withergate. Recently, its priests are spreading their faith in the vicinity. Probably it’s a bad idea to make them angry, because these fanatics has a large influence in the city. We’ve heard that in some other clans they confiscated property, also they launched a few inquisitional trials and it is said that also stakes has burned. We can’t just send them out, because they will come back with even a larger force. In a few days they are by us.'),
    ('d.inquisition', 'cs', 'Ve Withergate je populární nějaké nové božstvo. Jeho kněží poslední dobou často vyráží do okolí a šíří svou víru po dobrém i po zlém. Zřejmě není dobrý nápad si je pohněvat, protože tito fanatici mají velký vliv na dění ve městě. V některých klanech konfiskovali majetek, dokonce došlo i na inkviziční procesy a prý už i hořely hranice. Nemůžeme je poslat pryč, protože by se vrátili s mnohem větší silou. Za několik dní jsou i u nás.');

INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('d.inquisition.success', 'en', 'It is better to be on friendly terms with Atom. With a High Priest Boblig we really understood each other. It’s obvious that the Devil avoids our clan. Or maybe it’s even more obvious that Boblig can confiscate property in other clans with more success and less effort?'),
    ('d.inquisition.success', 'cs', 'S Atomem je lepší být zadobře, a s jeho veleknězem Bobligem jsme si dokonale rozuměli. Pochopil, že nám se Ďábel raději vyhýbá. Nebo pochopil, že v cizích klanech se mu bude lépe konfiskovat majetek?'),
    ('d.inquisition.partialSuccess', 'en', 'Atom is satisfied with us, but some of us straied from the right path. The priests launched inquisition and a painful interrogation. Nobody confessed - so they confiscated some property and left our clan in order to spread their faith somewhere else.'),
    ('d.inquisition.partialSuccess', 'cs', 'Prý je Atom s námi spokojen, někteří z nás ale prý sešli z cesty. Kněží zahájili vyšetřování a bolestivé výslechy. Nikdo se nepřiznal, tak zkonfiskovali trochu majetku a jeli šířit své bohulibé myšlenky jinam...'),
    ('d.inquisition.failure', 'en', 'Well, that was just bad. The priests did not find a sufficient zeal in out clan. Instead, they did find out that some of us are lying with the Devil. The priests launched inquisition and a painful interrogation. Nobody confessed, but they were satisfied that we are believing in Atom now. The priests confiscated some property and left our clan in order to spread their faith somewhere else.'),
    ('d.inquisition.failure', 'cs', 'To bylo horší, než jsme čekali. Nenašli u nás dostatečný zápal pro víru, zato zjistili, že někteří z nás obcují s Ďáblem. Zahájili vyšetřování a bolestivé výslechy. Nikdo se nepřiznal, ale stačilo jim, že někteří z nás raději přijali víru. Kněží pak zkonfiskovali trochu majetku a jeli šířit své bohulibé myšlenky jinam...');

INSERT INTO disaster_solutions(identifier, basic, disaster, solution_type, disaster_condition, difficulty, bonus, junk_cost, caps_cost, food_cost, item_cost) VALUES
    ('d.inquisition.s1', true, 'd.inquisition', 'AUTOMATIC', null,  0, 5, 0, 0, 0, null),
    ('d.inquisition.s2', false, 'd.inquisition', 'INTELLECT', null, 7, 10, 0, 0, 0, null),
    ('d.inquisition.s3', false, 'd.inquisition', 'CRAFTSMANSHIP', null, 5, 12, 10, 0, 0, null),
    ('d.inquisition.s4', false, 'd.inquisition', 'AUTOMATIC', null, 0, 20, 0, 50, 0, null);

INSERT INTO localized_texts(disaster_solution_name, lang, text) VALUES
    ('d.inquisition.s1', 'en', 'Pray and labor'),
    ('d.inquisition.s1', 'cs', 'Modlit se a pracovat'),
    ('d.inquisition.s2', 'en', 'Talk your way out of this'),
    ('d.inquisition.s2', 'cs', 'Vymluvit se z toho'),
    ('d.inquisition.s3', 'en', 'Craft a `genuine` relic'),
    ('d.inquisition.s3', 'cs', 'Vyrobit zaručeně pravou relikvii'),
    ('d.inquisition.s4', 'en', 'Bribe... ah yes, it’s called an indulgence'),
    ('d.inquisition.s4', 'cs', 'Uplatit... aha, oni tomu říkají odpustek');

INSERT INTO localized_texts(disaster_solution_description, lang, text) VALUES
    ('d.inquisition.s1', 'en', 'Out true faith should be manifested. Let’s rehearse some prayers and rituals which everyone can handle.'),
    ('d.inquisition.s1', 'cs', 'Možná bude stačit dát najevo svou pravou víru. Pojďme si nacvičit nějaké modlitby a rituály, které zvládne každý. Až přijdou, sehrajeme jim malé divadlo - ať mír dál zůstává s touto pustinou... Tato akce nestojí žádné zdroje ani nemá žádné prerekvizity.'),
    ('d.inquisition.s2', 'en', 'There is everything in order in our clan, but they should inspect our neighbours. We will spread a perfect lie about a devils living nearby. About a rich devils, whose property can be easily confiscated. This action depends on an intellect of a character.'),
    ('d.inquisition.s2', 'cs', 'U nás je všechno v pořádku, ať jdou radši k sousedům. Než přijdou, rozšíříme v našem klanu dokonalou lež o ďáblech žijících poblíž. O bohatých ďáblech, jejichž majetek lze snadno zkonfiskovat a nikdo proti tomu neřekne ani “popel z hranice”. Výsledek této akce je ovlivněn intelektem postavy.'),
    ('d.inquisition.s3', 'en', 'Bones of Atom is a gift which sais that no devil can live among us. Let’s find a pile of old bones and a proper tools. This action depends on a craftsmanship of a character.'),
    ('d.inquisition.s3', 'cs', 'Podstrčíme jim padělek svatých ostatků, kostí samotného Atoma. Po takovém daru je jasné, že mezi námi Ďábel žít nemůže. Výsledek této akce je ovlivněn zručností postavy.'),
    ('d.inquisition.s4', 'en', 'Even the faith can be bought.'),
    ('d.inquisition.s4', 'cs', 'I víra se dá koupit.');

-- Refugees
INSERT INTO disaster_details(identifier, final_disaster, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.refugees', false, 15, 'd.refugees.success', 'd.refugees.partialSuccess', 'd.refugees.failure', 'https://storage.googleapis.com/withergate-images/disasters/refugees.png');

INSERT INTO disaster_penalties(identifier, disaster, penalty_type) VALUES
    ('d.refugees.p1', 'd.refugees', 'ITEM_LOSS'),
    ('d.refugees.p2', 'd.refugees', 'RESOURCES_LOSS'),
    ('d.refugees.p3', 'd.refugees', 'CHARACTER_INJURY');

INSERT INTO localized_texts(disaster_name, lang, text) VALUES
    ('d.refugees', 'en', 'Refugees'),
    ('d.refugees', 'cs', 'Utečenci');

INSERT INTO localized_texts(disaster_description, lang, text) VALUES
    ('d.refugees', 'en', 'A part of Withergate has changed in a last few days and became uninhabitable. Bands of useless hungry men, women and children are travelling through the region and asking for help. We should help them - because even though they are weak, there are lots of them; and even though they are not aggressive, they are desperate and we can’t keep an eye on every single piece of property of our clan. They made a camp nearby our gates. Let’s hope they are not going to eat out every food in neigbourhood before they will go somewhere else '),
    ('d.refugees', 'cs', 'Část Withergate se v posledních dnech změnila a stala se neobyvatelnou. Krajem putují skupiny neužitečných hladových mužů, žen i dětí a žádají o pomoc. Měli bychom jim pomoci v nouzi, protože i když jsou slabí, je jich mnohem více než nás, a i když zatím nejsou agresivní, jsou zoufalí a my nezvládneme uhlídat celý náš klan. Už se utábořili kousek od našich bran - snad nám nevyžerou všechny zásoby ze sousedství, než půjdou zase dál.');

INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('d.refugees.success', 'en', 'Thanks to our splendidly organized reaction they left after a few days. No serious harms has been reported. '),
    ('d.refugees.success', 'cs', 'Díky naší skvěle zorganizované reakci po pár dnech odešli, aniž by způsobili jakékoliv nečekané škody.'),
    ('d.refugees.partialSuccess', 'en', 'There were a few fights between the refugees and our clan and some provisions and maybe even items were stolen from our storage area.'),
    ('d.refugees.partialSuccess', 'cs', 'Došlo k několika potyčkám a v noci nám zmizely ze skladů nějaké zásoby či předměty.'),
    ('d.refugees.failure', 'en', 'Our solution was not successful. The refugess went totally out of control and took everything they wanted. Despair because of their hunger, they fought with us and took everything they were able to carry - not even from our storage area, but from the whole neighbourhood.'),
    ('d.refugees.failure', 'cs', 'Naše řešení nezapůsobilo. Zcela se vymkli naší kontrole a brali si, co chtěli. Měli hlad, prali se s námi a pak vzali vše, na co přišli. Nejen z našich zásob, ale i z celého sousedství...');

INSERT INTO disaster_solutions(identifier, basic, disaster, solution_type, disaster_condition, difficulty, bonus, junk_cost, caps_cost, food_cost, item_cost) VALUES
    ('d.refugees.s1', true, 'd.refugees', 'AUTOMATIC', null, 0, 5, 0, 0, 0, null),
    ('d.refugees.s2', false, 'd.refugees', 'INTELLECT', null, 7, 10, 0, 0, 0, null),
    ('d.refugees.s3', false, 'd.refugees', 'AUTOMATIC', null, 0, 12, 0, 0, 30, null),
    ('d.refugees.s4', false, 'd.refugees', 'COMBAT', null, 4, 10, 0, 0, 0, null);

INSERT INTO localized_texts(disaster_solution_name, lang, text) VALUES
    ('d.refugees.s1', 'en', 'Let them in... but keep an eye on them!'),
    ('d.refugees.s1', 'cs', 'Vpustit je mezi nás a hlídat je'),
    ('d.refugees.s2', 'en', 'Heal the wounded and make them some medicine'),
    ('d.refugees.s2', 'cs', 'Léčit zraněné a vyrobit jim léky'),
    ('d.refugees.s3', 'en', 'Feed them'),
    ('d.refugees.s3', 'cs', 'Nakrmit je dosyta'),
    ('d.refugees.s4', 'en', 'Chase them away!'),
    ('d.refugees.s4', 'cs', 'Zahnat je');

INSERT INTO localized_texts(disaster_solution_description, lang, text) VALUES
    ('d.refugees.s1', 'en', 'We will find some more food for them. They can stay for a few days, which means we can probably easily watch them.'),
    ('d.refugees.s1', 'cs', 'Seženeme jim trochu jídla a vody a necháme je u nás přenocovat, ale hlavně je budeme mít alespoň trochu pod dohledem.'),
    ('d.refugees.s2', 'en', 'They can’t continue in their journey because of injuries and diseases. If we will treat them, they will move away faster.'),
    ('d.refugees.s2', 'cs', 'Nemohou jít dál, protože někteří z nich jsou po cestě zranění či nemocní. Základní zdravotní péči bychom jim zvládli poskytnout - o to dříve odejdou. Výsledek akce je ovlivněn intelektem postavy.'),
    ('d.refugees.s3', 'en', 'When we fully feed them, they will not try to steal more food. Hopefully. '),
    ('d.refugees.s3', 'cs', 'Pokud je zcela nasytím, nebudou se nám ho snažit ukrást. Tedy... říká se, že s jídlem roste chuť.'),
    ('d.refugees.s4', 'en', 'And let them tremble in fear!'),
    ('d.refugees.s4', 'cs', 'Ať si nás nenávidí, jen když se nás bojí. Akce vyvolá boj s rizikem zranění či smrti a rovněž neúspěchu.');

-- Storm
INSERT INTO disaster_details(identifier, final_disaster, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.storm', false, 15, 'd.storm.success', 'd.storm.partialSuccess', 'd.storm.failure', 'https://storage.googleapis.com/withergate-images/disasters/storm.png');

INSERT INTO disaster_penalties(identifier, disaster, penalty_type) VALUES
    ('d.storm.p1', 'd.storm', 'BUILDING_DESTRUCTION'),
    ('d.storm.p2', 'd.storm', 'CHARACTER_INJURY'),
    ('d.storm.p3', 'd.storm', 'RESOURCES_LOSS');

INSERT INTO localized_texts(disaster_name, lang, text) VALUES
    ('d.storm', 'en', 'Storm'),
    ('d.storm', 'cs', 'Bouře');

INSERT INTO localized_texts(disaster_description, lang, text) VALUES
    ('d.storm', 'en', 'The weather still changes. According to some really clever guys from Withergate is going to start storm season in a few days. And this one should be one of the strongest storm seasons ever. Let’s focus on preventing the floods, windstorm and falling trees in time.'),
    ('d.storm', 'cs', 'Počasí se neustále mění. Dle výpočtů nějakých chytrých lidí z Withergate má v nejbližších dnech nastat období bouří, a dle jiných lidí z hospody - možná už ne tolik chytrých, ale stále důvěryhodných - budou ty nastávající bouře jedny z nejsilnějších vůbec. Po zevrubné inspekci okolí našeho klanu bychom si měli dát pozor na povodně, padající stromy a vůbec raději přibít vše, co není přidělané.');

INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('d.storm.success', 'en', 'Once again the mankind triumphed over the raging elements! The windstorm struck our shelters and torrential waters swept through the vicinity. But we were ready for this and no harm has been reported.'),
    ('d.storm.success', 'cs', 'Člověk opět jednou triumfoval nad běsnícími živly. Ačkoliv vichřice udeřila na naše domy a přílivová voda se prohnala okolím, byli jsme připraveni a vše jsme přečkali v bezpečí, ve zdraví a bez ztrát na majetku.'),
    ('d.storm.partialSuccess', 'en', 'The windstorm struck our shelters. Falling trees and torrential waters made a terribly mess in our clan. The fight with raging elements was difficult for our health and property.'),
    ('d.storm.partialSuccess', 'cs', 'Vichřice udeřila na naše domy. Padající stromy v našem klanu způsobily pořádnou spoušť, kterou poté zaplavila přívalová voda. Když opadla, nechala po sobě nánosy bláta. Boj s živlem a jeho následky byl náročný na naše zdraví i náš majetek.'),
    ('d.storm.failure', 'en', 'Several stormdays struck in a full force. The windstorm teared off the roofs from our houses. Falling trees and torrential waters made a terribly mess in our clan. The fight with raging elements was difficult for our health and property.'),
    ('d.storm.failure', 'cs', 'Několik bouřkových dní udeřilo v plné síle. Vichřice rvala střechy z našich domů a lámala stromy, které v našem klanu způsobily pořádnou spoušť. Trosky zaplavila přívalová voda. Když opadla, nechala po sobě nánosy bláta a naše nyní již bývalé zásoby, plesnivé a zrezivělé… V panice při útěku před živly také došlo k několika zraněním.');

INSERT INTO disaster_solutions(identifier, basic, disaster, solution_type, disaster_condition, difficulty, bonus, junk_cost, caps_cost, food_cost, item_cost) VALUES
    ('d.storm.s1', true, 'd.storm', 'AUTOMATIC', null, 0, 5, 0, 0, 0, null),
    ('d.storm.s2', false, 'd.storm', 'INTELLECT', null, 7, 10, 2, 0, 0, null),
    ('d.storm.s3', false, 'd.storm', 'CRAFTSMANSHIP', null, 5, 12, 10, 0, 0, null),
    ('d.storm.s4', false, 'd.storm', 'AUTOMATIC', null, 4, 20, 0, 50, 0, null);

INSERT INTO localized_texts(disaster_solution_name, lang, text) VALUES
    ('d.storm.s1', 'en', 'Hide everything we have'),
    ('d.storm.s1', 'cs', 'Všechno schovat'),
    ('d.storm.s2', 'en', 'Eureca! The lightning conductor!'),
    ('d.storm.s2', 'cs', 'Vymyslet hromosvody a další udělátka'),
    ('d.storm.s3', 'en', 'Use some junk and fortify everything'),
    ('d.storm.s3', 'cs', 'Použít šrot a vše zpevnit'),
    ('d.storm.s4', 'en', 'Hire external workforce'),
    ('d.storm.s4', 'cs', 'Najmout námezdníky');

INSERT INTO localized_texts(disaster_solution_description, lang, text) VALUES
    ('d.storm.s1', 'en', 'Our belongings and provisions are scattered all around, in fragile shelters, where they can be hit by a raging element. Let’s hide it below the ground, and hammer it down.'),
    ('d.storm.s1', 'cs', 'Naše vybavení a zásoby se válí všude okolo, v křehkých přístřešcích, kde je může zasypat běsnící živel... Pojďme vše uschovat pod zem a daleko od vody, a ideálně ještě přibít. Jakkoliv.'),
    ('d.storm.s2', 'en', 'Let’s hope that we are smart enough to install the lighting conductors to correct places.'),
    ('d.storm.s2', 'cs', 'Možná by bylo dobré naše budovy upravit tak, aby výše položené místo nebyla hořlavá střecha domu, ale třeba… kovový vodič, po kterém se blesky svezou na bezpečné místo a nezpůsobí žádnou škodu. Bohužel pouze ti nejchytřejší z nás vědí, jak a kam takové hromosvody nainstalovat.'),
    ('d.storm.s3', 'en', 'Some of our shelters are somewhat fragile. They can be damaged in a storm and hurt us inside. Provided that we know, we can fix them with a proper material. The success of this action depends on the character`s craftsmanship.'),
    ('d.storm.s3', 'cs', 'Některá naše stavení jsou spíše chatrná. Hrozí jejich zřícení či poškození a ztráty na majetku i na zdraví. Pokud víme, jak na to, můžeme je s použitím vhodného materiálu trochu vyspravit. Úspěch akce závisí na zručnosti postavy.'),
    ('d.storm.s4', 'en', 'Some enterprising persons from the vicinity are claiming they exactly know what do, and for a few caps they can help us. Afterwards, they will go with their stormcraft elsewhere.'),
    ('d.storm.s4', 'cs', 'Někteří podnikavci z okolí tvrdí, že přesně ví, co máme udělat, a za pár zátek to pro nás i provedou. Mohli by pro nás být užiteční, než půjdou se svým bouřným uměním zase o dům dál...');

-- Bandits
INSERT INTO disaster_details(identifier, final_disaster, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.bandits', false, 15, 'd.bandits.success', 'd.bandits.partialSuccess', 'd.bandits.failure', 'https://storage.googleapis.com/withergate-images/disasters/bandits.png');

INSERT INTO disaster_penalties(identifier, disaster, penalty_type) VALUES
    ('d.bandits.p1', 'd.bandits', 'INFORMATION_LOSS'),
    ('d.bandits.p2', 'd.bandits', 'CHARACTER_INJURY'),
    ('d.bandits.p3', 'd.bandits', 'BUILDING_DESTRUCTION');

INSERT INTO localized_texts(disaster_name, lang, text) VALUES
    ('d.bandits', 'en', 'Raiding scoundrels'),
    ('d.bandits', 'cs', 'Loupeživé bandy');

INSERT INTO localized_texts(disaster_description, lang, text) VALUES
    ('d.bandits', 'en', 'People living in the Wasteland are getting wider lately. One of the neighbouring clans was raided three times by various bandits just last week. It was fine till this madness was happening far away from us but we should do something before we meet the same destiny. These looting troops are hiding somewhere in the Wasteland. How do we locate them?'),
    ('d.bandits', 'cs', 'Lidé v Pustině jsou v poslední době stále divočejší. Jeden blízký klan prý bývá za týden znásilňován až sedmi různými loupeživými bandami. To u nás nehrozí, ale bandy v okolí nás odřízly od světa a samozřejmě nás v potyčce s námi mohou zranit nebo podpálit část našeho klanu. To prý jim jde nejlépe. Hlavně se ale umí výborně ukrývat v Pustině, jak je vůbec najdeme?');

INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('d.bandits.success', 'en', 'We successfully scared all the scoundrels away. They will surely avoid our neighborhood for a long time. And who knows, perhaps they will abandon their lifestyle and start a new honest one like us - searching the Wasteland and plundering the remnants.'),
    ('d.bandits.success', 'cs', 'S agresory jsme si poradili a zahnali je daleko do Pustiny. Našemu okolí se budou ještě dlouhá léta vyhýbat - a kdo ví, třeba zanechají svého řemesla a začnou se živit poctivě jako my, prohledáváním Pustiny a loupením zbytků. '),
    ('d.bandits.partialSuccess', 'en', 'We managed to scare some of the bandits but it was not without a fight. Our clan was completely cut off for a while, without any access to the information about Withergate. Is there still Withergate, after all?'),
    ('d.bandits.partialSuccess', 'cs', 'Snažili jsme se improvizovaně válčit, ale nakonec plenili i u nás. Po dlouhém boji jsme je zahnali, bohužel způsobili hodně škody. Navíc nás na několik dní zcela odřízli od okolí, takže vůbec nemáme aktuální informace o dění ve větě. Je tam vůbec ještě Withergate? '),
    ('d.bandits.failure', 'en', 'Our neighborhood got overwhelmed by bandits. We were not able to defend ourselves properly and they keps ambushing us on the roads, pillaging our base and raping our women. Our clan was completely cut off for a while, without any access to the information about Withergate. Is there still Withergate, after all?'),
    ('d.bandits.failure', 'cs', 'V našem okolí se jich nakonec objevilo mnohem víc, než jsme byli schopni zvládnout. Nejprve nás přepadávali jen na cestách, ale nakonec došlo k velkému nájezdu na náš klan. Po dlouhém boji jsme je zahnali, ale stálo nás to mnoho krve. I tak poškodili několik budov a na několik dní nás zcela odřízli od okolí, takže vůbec nemáme aktuální informace o dění ve světě. Je tam vůbec ještě Withergate?');

INSERT INTO disaster_solutions(identifier, basic, disaster, solution_type, disaster_condition, difficulty, bonus, junk_cost, caps_cost, food_cost, item_cost) VALUES
    ('d.bandits.s1', true, 'd.bandits', 'AUTOMATIC', null, 0, 5, 0, 0, 0, null),
    ('d.bandits.s2', false, 'd.bandits', 'AUTOMATIC', null, 0, 10, 0, 0, 0, 'WEAPON'),
    ('d.bandits.s3', false, 'd.bandits', 'INTELLECT', null, 5, 10, 0, 10, 0, null),
    ('d.bandits.s4', false, 'd.bandits', 'COMBAT', null, 6, 20, 0, 0, 0, null);

INSERT INTO localized_texts(disaster_solution_name, lang, text) VALUES
    ('d.bandits.s1', 'en', 'Search and report'),
    ('d.bandits.s1', 'cs', 'Pátrat a bonzovat'),
    ('d.bandits.s2', 'en', 'Help and defend'),
    ('d.bandits.s2', 'cs', 'Pomáhat a chránit'),
    ('d.bandits.s3', 'en', 'Promise and pay'),
    ('d.bandits.s3', 'cs', 'Slibovat a platit'),
    ('d.bandits.s4', 'en', 'Search and destroy'),
    ('d.bandits.s4', 'cs', 'Najít a zničit');

INSERT INTO localized_texts(disaster_solution_description, lang, text) VALUES
    ('d.bandits.s1', 'en', 'Let’s send our people looking for the bandits. We should take notes of everyone looking suspicious so we can describe them to the bounty hunters from Withergate.'),
    ('d.bandits.s1', 'cs', 'Pošleme své lidi po okolí a jakmile si všimneme podezřelých individuií, místo si zapamatujeme a přesně popíšeme lovcům odměn ve Withergate.'),
    ('d.bandits.s2', 'en', 'Let’s get everyone in our neighborhood heavily armed. That should keep those bastards away. We should distribute weapons to everyone passing by our base. Hopefully, we won’t arm the bandits.'),
    ('d.bandits.s2', 'cs', 'Pokud nejen my, ale všichni v okolí budou chodit ozbrojeni, bandity to z našeho území zažene. Každého, kdo půjde okolo, ozbrojíme. Snad neozbrojíme i ty bandity. Brzy se i k banditům donese informace o tom, jak již víme o jejich úkrytu a již organizujeme mohutný nájezd.Pokud nejen my, ale všichni v okolí budou chodit ozbrojeni, bandity to z našeho území zažene. Každého, kdo půjde okolo, ozbrojíme. Snad neozbrojíme i ty bandity. Brzy se i k banditům donese informace o tom, jak již víme o jejich úkrytu a již organizujeme mohutný nájezd.'),
    ('d.bandits.s3', 'en', 'We should spread gossip about our undying determination to eliminate all scoundrels in our surroundings. Furthermore, we will pay caps to everyone who brings us a bandit scalp. Sooner or later, these looting bands will fear us. Or perhaps, they will eliminate each other and collect the reward.'),
    ('d.bandits.s3', 'cs', 'Kudy půjdeme, tam budeme vykládat o našem bohulibém přesvědčení vyhnat všechny neřády z kraje. Také začneme vyplácet odměny za každý bandití skalp, aby lovci hlav hledali svou kořist blíže u nás. Navíc se třeba ta bandití verbež pomlátí navzájem s vidinou kořisti, akorát jim budeme muset zaplatit… Musíme být zkrátka jen dostatečně přesvědčiví, nebo alespoň hodně platit za skalpy.'),
    ('d.bandits.s4', 'en', 'The most vicious bandits are not hiding. I heard one of them boasting about his latest raid in the tavern. We should go after them and defeat them in combat.'),
    ('d.bandits.s4', 'cs', 'Ti největší hrdlořezové o sobě dávají vědět a proto nebude těžké je najít. Pokud je porazíme v boji a všichni uvidí jejich smutný osud, může to snížit počet banditů v okolí.');

-- Outbreak
INSERT INTO disaster_details(identifier, final_disaster, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.outbreak', false, 15, 'd.outbreak.success', 'd.outbreak.partialSuccess', 'd.outbreak.failure', 'https://storage.googleapis.com/withergate-images/disasters/outbreak.png');

INSERT INTO disaster_penalties(identifier, disaster, penalty_type) VALUES
    ('d.outbreak.p1', 'd.outbreak', 'RESOURCES_LOSS'),
    ('d.outbreak.p2', 'd.outbreak', 'INFORMATION_LOSS'),
    ('d.outbreak.p3', 'd.outbreak', 'CHARACTER_INJURY');

INSERT INTO localized_texts(disaster_name, lang, text) VALUES
    ('d.outbreak', 'en', 'Virus outbreak'),
    ('d.outbreak', 'cs', 'Nákaza');

INSERT INTO localized_texts(disaster_description, lang, text) VALUES
    ('d.outbreak', 'en', 'There are rumours that there was a virus outbreak in Withergate. We are not sure what it is but it is highly contagious and it is just a question of time before it spreads through the whole Wasteland. We should ready ourselves for the worst case, gather information and stock up. Or is it already too late?'),
    ('d.outbreak', 'cs', 'Donesly se k nám zprávy, že ve Withergate se začal šířit nějaký nebezpečný virus. Nevíme zatím, o co se jedná, ale rychle se šíří je jen otázkou času, kdy se rozšíří po celé Pustině. Měli bychom se připravit na nejhorší, zjistit si dostatek informací a udělat zásoby. Nebo už je příliš pozdě?');

INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('d.outbreak.success', 'en', 'We managed to avoid the outbreak in our clan and surroundings. Fortunately, nobody got infected, we are sufficiently stocked and others are well informed about hygiene and quarantine. Well done!'),
    ('d.outbreak.success', 'cs', 'Podařilo se nám zastavit šíření nákazy v našem okolí. Díky naší rychlé reakci se nikdo z nás nenakazil, máme dostatek zásob, abychom přečkali krátkou karanténu a ostatní jsme řádně poučili o nutnosti lepší hygieny. Neštěstí se nám zcela vyhnulo.'),
    ('d.outbreak.partialSuccess', 'en', 'We managed to soften the blow but dealing with the outbreak cost us a lot of resources. We had to cut ourselves off for some time as well. Fortunately, nobody died.'),
    ('d.outbreak.partialSuccess', 'cs', 'Podařilo se nám zmírnit dopady nákazy, ale řešení situace nás stálo nemalé investice. Také jsme museli absolvovat krátkou karanténu. Nemoc se naštěstí neobjevila u nás, takže nemáme ztráty na životech.'),
    ('d.outbreak.failure', 'en', 'We completely underestimated the situation and had to invest a lot of resources to deal with the consequences. Our whole clan had to be locked in quarantine and some of got infected. We need to take better care next time.'),
    ('d.outbreak.failure', 'cs', 'Tak tohle jsme hodně podcenili. Nejen, že se několik z nás nakazilo, ale ještě jsme museli vyhlásit dlouhou karanténu a věnovat většinu našich surovin na boj s nákazou. Příště si musíme dát větší pozor.');

INSERT INTO disaster_solutions(identifier, basic, disaster, solution_type, disaster_condition, difficulty, bonus, junk_cost, caps_cost, food_cost, item_cost) VALUES
    ('d.outbreak.s1', true, 'd.outbreak', 'AUTOMATIC', null, 0, 5, 0, 0, 0, null),
    ('d.outbreak.s2', false, 'd.outbreak', 'CRAFTSMANSHIP', null, 6, 10, 5, 0, 0, null),
    ('d.outbreak.s3', false, 'd.outbreak', 'AUTOMATIC', null, 0, 10, 0, 0, 12, null),
    ('d.outbreak.s4', false, 'd.outbreak', 'SCAVENGE', null, 8, 10, 0, 0, 0, null);

INSERT INTO localized_texts(disaster_solution_name, lang, text) VALUES
    ('d.outbreak.s1', 'en', 'Educate others'),
    ('d.outbreak.s1', 'cs', 'Šířit osvětu'),
    ('d.outbreak.s2', 'en', 'Prepare medical drapes'),
    ('d.outbreak.s2', 'cs', 'Připravit ochranné roušky'),
    ('d.outbreak.s3', 'en', 'Stock up'),
    ('d.outbreak.s3', 'cs', 'Udělat si zásoby'),
    ('d.outbreak.s4', 'en', 'Search for cure'),
    ('d.outbreak.s4', 'cs', 'Hledat lék');

INSERT INTO localized_texts(disaster_solution_description, lang, text) VALUES
    ('d.outbreak.s1', 'en', 'Let’s go from clan to clan, talk to wayfarers and organize happenings. We need to tell others that it is absolutely crucial to avoid all sorts of human contact, restrict travel and improve hygiene.'),
    ('d.outbreak.s1', 'cs', 'Pojďme chodit od klanu ke klanu, zastavovat pocestné a pořádat setkání a besedy, na kterých ostatní řádně poučíme o nutnosti omezení cestování a shlukování lidí a lepší hygieně.'),
    ('d.outbreak.s2', 'en', 'Ti šikovnější z nás by mohli vyrobit lékařské roušky a respirátory, abychom nedýchali stejný vzduch jako ti méně šťastní z nás.'),
    ('d.outbreak.s2', 'cs', 'Some of us can work on creating improvised medical drapes and respirators so we don’t breathe the same air as the sick ones.'),
    ('d.outbreak.s3', 'en', 'We should stock up in case of quarantine so we won’t die in case we need to cut ourselves off completely.'),
    ('d.outbreak.s3', 'cs', 'Možná, že nás čeká delší karanténa. Bude nutné vytvořit zásoby, abychom během ní nepomřeli hlady.'),
    ('d.outbreak.s4', 'en', 'A very rare herb grows in a Wasteland, which is said to be able to partially cure this disease. It might be just a hoax but we might want to check it out and look for it.'),
    ('d.outbreak.s4', 'cs', 'V Pustině roste velmi vzácná bylina, o které se říká, že dokáže částečně léčit tuto nákazu. Možná to jsou povídačky, ale mohli bychom se po ní podívat.');

-- Clans assembly
INSERT INTO disaster_details(identifier, final_disaster, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.clans', true, 0, 'd.clans.success', 'd.clans.partialSuccess', 'd.clans.failure', 'https://storage.googleapis.com/withergate-images/disasters/clans.png');

INSERT INTO disaster_penalties(identifier, disaster, penalty_type) VALUES
    ('d.clans.p1', 'd.clans', 'FAME_LOSS'),
    ('d.clans.p2', 'd.clans', 'FAME_LOSS'),
    ('d.clans.p3', 'd.clans', 'FAME_LOSS');

INSERT INTO localized_texts(disaster_name, lang, text) VALUES
    ('d.clans', 'en', 'Clans assembly'),
    ('d.clans', 'cs', 'Sněm klanů');

INSERT INTO localized_texts(disaster_description, lang, text) VALUES
    ('d.clans', 'en', 'It is time for our clan to enter high wasteland politics. All the mightly clans from the Withergate surroundings will gather to elect a new leader. We need strong hand to lead us and protect us from the Withergate bandits and raiding mutants from the Wasteland. We worked hard and fought bravely, so who else but us deserves this post? Others should support us, praise us and bring us food and junk to develop our glorious clan base! Everybody will benefit from us leading all the clans forward. Unfortunately, others do not share our vision so we will need to remind them of our achievements. Let`s make Wasteland great again!'),
    ('d.clans', 'cs', 'Nastal čas, aby náš klan konečně promluvil do velké politiky. Všechny mocné klany z okolí Withergate se za několik dní sejdou, aby domluvily budoucí uspořádání sil v Pustině. Pokud silná ruka nebude bránit Pustinu, všechny si nás zotročí obyvatelé Withergate nebo nás pobijí mutanti. Náš klan těžce bojoval ve městě i v okolí. Proslavili jsme se boji v aréně, inovativním přístupem při odvracení pohrom a při plnění úkolů. Naše sláva nás předchází. Kdo jiný než my tak má právo na největší území v Pustině? Komu jinému než nám by ostatní klany měly sloužit, vozit nám materiál a zásoby, abychom se mohli věnovat stavbě našeho monumentálního sídla? To přeci prospěje celému lidstvu. Bohužel ostatní klany tento názor zcela nesdílí, takže bude dobré jim naše zásluhy trochu připomenout.
');

INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('d.clans.success', 'en', 'Our breathtaking delegation is sure to receive an audition with the Assembly. We already secured several votes for the Lord of the Wasteland election. Not sure if it is enough but we could not prepare better.'),
    ('d.clans.success', 'cs', 'Naše dechberoucí delegace má jistotu, že ji sněm vyslechne. Už teď snad máme i zajištěných několik hlasů ve volbě pána Pustiny. Určitě ale nejsme jediní - uvidíme, jestli je to dost, ale lépe jsme se připravit nemohli.'),
    ('d.clans.partialSuccess', 'en', 'Other clans are still talking more about others than about us. That is not good for our reputation. Anyway, we made sure there are some that will listen to us, although we could have done a better job.'),
    ('d.clans.partialSuccess', 'cs', 'O ostatních klanech se v Pustině pořád mluví více než o nás, což poškozuje naši pověst. Každopádně jsme zajistili, že na sněmu budou tací, kteří nám budou naslouchat. Ale určitě jsme se mohli připravit lépe.'),
    ('d.clans.failure', 'en', 'Unfortunately, we have not managed to spread the fame of our clan at all. We are leaving for the assembly without any preparation. Will anybody support us or are we gonna leave as beggers?'),
    ('d.clans.failure', 'cs', 'Bohužel se nám vůbec nepodařilo rozšířit pověst našeho klanu. Na sněm odjíždíme bez jakékoliv přípravy, téměř s holým zadkem. Jsme tak dobří, že nám spadne vítězství samo do klína? Bude se s námi vůbec někdo bavit, když vypadáme jako žebráci?');

INSERT INTO disaster_solutions(identifier, basic, disaster, solution_type, disaster_condition, difficulty, bonus, junk_cost, caps_cost, food_cost, item_cost) VALUES
    ('d.clans.s1', true, 'd.clans', 'AUTOMATIC', null, 0, 3, 0, 0, 0, null),
    ('d.clans.s2', false, 'd.clans', 'INTELLECT', null, 8, 6, 0, 0, 0, null),
    ('d.clans.s3', false, 'd.clans', 'CRAFTSMANSHIP', null, 6, 6, 10, 0, 0, null),
    ('d.clans.s4', false, 'd.clans', 'COMBAT', null, 6, 8, 0, 0, 0, null),
    ('d.clans.s5', false, 'd.clans', 'AUTOMATIC', null, 0, 15, 0, 150, 0, null);

INSERT INTO localized_texts(disaster_solution_name, lang, text) VALUES
    ('d.clans.s1', 'en', 'Praise us'),
    ('d.clans.s1', 'cs', 'Vynachválit nás'),
    ('d.clans.s2', 'en', 'Prepare a speech'),
    ('d.clans.s2', 'cs', 'Připravit si oslavnou řeč'),
    ('d.clans.s3', 'en', 'Support us with glitters'),
    ('d.clans.s3', 'cs', 'Podpořit nás pozlátky'),
    ('d.clans.s4', 'en', 'Use the force'),
    ('d.clans.s4', 'cs', 'Podpořit nás silou'),
    ('d.clans.s5', 'en', 'Bribe them'),
    ('d.clans.s5', 'cs', 'Uplatit pár vlivných');

INSERT INTO localized_texts(disaster_solution_description, lang, text) VALUES
    ('d.clans.s1', 'en', 'Everyone is talking about the Assembly. It should be enough to spread a word about our clan and soon everyone will know about us.'),
    ('d.clans.s1', 'cs', 'O sněmu se teď mluví ve městě i v pustině. Stačí, když o nás bude ve světě slyšet. Povyprávějme ostatním jakoukoliv historku o naší úžasnosti, naše pověst se pomalu, ale jistě rozšíří.'),
    ('d.clans.s2', 'en', 'We will have a chance to speak to the others at the Assembly. Let`s prepare a glorifying speech about our clan. It could start with `I have a dream` and end with `I am a Wastelander!`. That should work... The success of this action is affected by the character`s intellect.'),
    ('d.clans.s2', 'cs', 'Na sněmu budeme mít možnost promluvit přede všemi. Připravme si brilantní oslavnou řeč o našem klanu. Mohlo by to třeba začínat “Mám sen” a končit “Já jsem Pustiňan!”, to zabere. Úspěch záleží na hodnotě inteligence postavy.'),
    ('d.clans.s3', 'en', 'Some of us can enhance our clothing so we look like other noblemen from the wasteland. The result of this action is affected by the character`s craftsmanship.'),
    ('d.clans.s3', 'cs', 'Ti zručnější z nás mohou naše oděvy upravit tak, abychom vypadali jako opravdoví pánové Pustiny. Úspěch záleží na hodnotě zručnosti postavy.'),
    ('d.clans.s4', 'en', 'There are rumors in the wasteland that our clan is incapable and weak. A little bird told us about the source of these lies. We should find them and teach them some manners! This action will trigger a combat and may lead to an injury, death and failure of the character.'),
    ('d.clans.s4', 'cs', 'V Pustině se šíří řeči, že náš klan je neschopný. Malý ptáček nám naštěstí pověděl, kdo je zdrojem těchto lží a pomluv. Pojďme mu ukázat, kdo je tu pánem. Akce vyvolá boj s rizikem zranění či smrti a rovněž neúspěchu.'),
    ('d.clans.s5', 'en', 'Everything can be bought with caps. But we will need a lot of them since we are not the only ones paying.'),
    ('d.clans.s5', 'cs', 'Zátky řeší vše... bude jich ale potřeba opravdu hodně, protože nejsme jediní, kdo platí.');

-- King beyond the Wasteland
INSERT INTO disaster_details(identifier, final_disaster, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.king', true, 0, 'd.king.success', 'd.king.partialSuccess', 'd.king.failure', 'https://storage.googleapis.com/withergate-images/disasters/king.png');

INSERT INTO disaster_penalties(identifier, disaster, penalty_type) VALUES
    ('d.king.p1', 'd.king', 'FAME_LOSS'),
    ('d.king.p2', 'd.king', 'FAME_LOSS'),
    ('d.king.p3', 'd.king', 'FAME_LOSS');

INSERT INTO localized_texts(disaster_name, lang, text) VALUES
    ('d.king', 'en', 'King beoyng the Wasteland'),
    ('d.king', 'cs', 'Král za Pustinou');

INSERT INTO localized_texts(disaster_description, lang, text) VALUES
    ('d.king', 'en', 'Wasteland seems to be endless. Our scouts heard rumors about a land, far away across the desert, which used to be plentiful not more than a couple of months ago. But now, it is apparently arid and more devastated than our surroundings. People are uniting under the rule of so called King beyond the Wasteland. They are marching towards Withergate to settle there and take control of the city. That might not be a huge problem from us, but what is going to happen to the clans that happen to be in their way? Are we gonna take the King’s side or hide from him?'),
    ('d.king', 'cs', 'Pustina se zdá být nekonečná. Naši zvědové ale hlásí potvrzené zvěsti o zemi za její hranicí, zemi ještě před pár měsíci úrodné, ale dnes již vyprahlé a ještě bídnější než naše okolí. A právě tamní lid se začíná sjednocovat pod Králem za Pustinou, aby se vydal na pochod a zmocnil se Withergate. To by nám ani tak nevadilo, ale otázkou je, co se stane s klany, které Králi stojí v cestě. Do pohybu se dává takové množství lidí, že tomu nelze vzdorovat. Několik klanů se již vlísalo do přízně Krále a zatím se zdá, že královské slovo platí. Umíme se také lísat? Budeme chtít zůstat zadobře s novým budoucím pánem celé oblasti, nebo se raději schováme a nebudeme na sebe nijak upozorňovat?');

INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('d.king.success', 'en', 'The King beyond the Wasteland appreciates us as a strong partner. Surely, we are not the only clan enjoying his favor, but we are sure that our voice will be heard in the new era that awaits us. Long live the king!'),
    ('d.king.success', 'cs', 'Král za Pustinou si nás váží jako silného partnera při svém tažení proti Withergate. Jistě nejsme jediný klan, který se může těšit jeho přízni, ale můžeme počítat s tím, že v novém uspořádání poměrů v Pustině budeme mít velice silné slovo. Ať žije Král Pustiny!'),
    ('d.king.partialSuccess', 'en', 'The king and his envoys know who we are, where we are, and what to expect from us. We do not have to worry about the King being hostile to us - but who knows what will happen when Withergate takes control of the region? Other clans, who have bent the knee to the King beyond the Wasteland, will apparently do better during the new era...'),
    ('d.king.partialSuccess', 'cs', 'Král a jeho vyslanci ví, kdo jsme, kde jsme a co od nás může čekat. Asi se nemusíme bát, že by se k nám choval otevřeně nepřátelsky - ale kdo ví, co se stane, až se zmocní Withergate a začne vládnout celé oblasti? Ostatní klany, které více pochlebovali Králi za Pustinou, zjevně čeká lepší zacházení v nové době… '),
    ('d.king.failure', 'en', 'Unfortunately, neither the King nor his ambassadors are interested. He might crave our possessions, and nothing would stop him from taking it. He would rule the whole region including Withergate, together with those who bent the knee. Our participation in the new government is unlikely.'),
    ('d.king.failure', 'cs', 'Bohužel Krále ani jeho vyslance vůbec nezajímáme. Možná jej bude zajímat náš majetek a nic mu nezabrání, aby nám jej vzal - stejně jako si brzy vezme Withergate a společně s těmi, kteří před ním poklekli, bude vládnout celé oblasti. Naše účast na nové vládě je nepravděpodobná.');

INSERT INTO disaster_solutions(identifier, basic, disaster, solution_type, disaster_condition, difficulty, bonus, junk_cost, caps_cost, food_cost, item_cost) VALUES
    ('d.king.s1', true, 'd.king', 'AUTOMATIC', null, 0, 3, 0, 0, 0, null),
    ('d.king.s2', false, 'd.king', 'SCAVENGE', null, 8, 6, 0, 0, 0, null),
    ('d.king.s3', false, 'd.king', 'INTELLECT', null, 6, 6, 0, 0, 0, 'ANY'),
    ('d.king.s4', false, 'd.king', 'CRAFTSMANSHIP', null, 6, 8, 15, 0, 0, null),
    ('d.king.s5', false, 'd.king', 'AUTOMATIC', null, 0, 15, 50, 50, 50, null);

INSERT INTO localized_texts(disaster_solution_name, lang, text) VALUES
    ('d.king.s1', 'en', 'Bend the knee'),
    ('d.king.s1', 'cs', 'Slíbit se dá vše, stačí jen pokleknout'),
    ('d.king.s2', 'en', 'Search the whole Wasteland'),
    ('d.king.s2', 'cs', 'Prohledáme celou Pustinu'),
    ('d.king.s3', 'en', 'Gifts from our storage'),
    ('d.king.s3', 'cs', 'Podíváme se do skladu'),
    ('d.king.s4', 'en', 'Hide behind a wall'),
    ('d.king.s4', 'cs', 'Schováme se za zdí'),
    ('d.king.s5', 'en', 'Provide resrouces for the march'),
    ('d.king.s5', 'cs', 'Poskytneme suroviny na podporu');

INSERT INTO localized_texts(disaster_solution_description, lang, text) VALUES
    ('d.king.s1', 'en', 'King`s envoys are all over the Wasteland. Let us send them our sincerest pledge of undying loyalty.'),
    ('d.king.s1', 'cs', 'Vyslanci nového krále jsou všude v Pustině, i nedaleko našeho klanu. Pošleme napohled vážný, ale jinak jistě odvolatelný slib věčné věrnosti.'),
    ('d.king.s2', 'en', 'We will search the whole Wasteland and bring the best pieces of junk to the king.'),
    ('d.king.s2', 'cs', 'Prohledáme celou Pustinu a Králi darujeme ty nejlepší kusy šrotu z nejlepších materiálů a jídla z nejlepších zmutovaných krav.'),
    ('d.king.s3', 'en', 'We might not need to search for an ideal gift for the king. Where is the chainsaw we had in our storage? The only thing left is to practice the correct etiquette for the official audience.'),
    ('d.king.s3', 'cs', 'Možná nemusíme chodit daleko, abychom našli vhodný královský dar. Kam jsme to dali tu motorovu pilu? Teď si už jen nastudovat, jak se vlastně dle etikety vhodně předává dar králi.'),
    ('d.king.s4', 'en', 'Why should the king waste his time with pillaging our clan if we are sufficiently defended? Let`s build a wall so high that we might as well call it... the Wall.'),
    ('d.king.s4', 'cs', 'Proč by se hordy měly valit zrovna na náš klan, když bude dostatečně opevněný? Postavme vysokou zeď. Tak vysokou, že jí budeme říkat třeba… třeba Zeď.'),
    ('d.king.s5', 'en', 'King will need a lot of resources for the march towards Withergate. If we provide him with sufficient amount of food, junk and caps, we might persuade them to leave our clan alone.'),
    ('d.king.s5', 'cs', 'Královští vyslanci nejsou diplomaté, ale zvědi. Určitě mají za úkol najít nejjednodušší cestu pro vedení invaze. Poskytnutím dostatečného množství surovin zajistíme, že Král za Pustinou povede svůj lid přes území jiných klanů. My jen v klidu počkáme, až se vlna přežene…');
