-- Disasters

-- Mutants
INSERT INTO disaster_details(identifier, final_disaster, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.mutants', false, 25, 'd.mutants.success', 'd.mutants.partialSuccess', 'd.mutants.failure', 'https://storage.googleapis.com/withergate-images/disasters/mutants.png');

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
    ('d.mutants.s3', 'd.mutants', 'COMBAT', 4, 12, 0, 0, 0),
    ('d.mutants.s4', 'd.mutants', 'AUTOMATIC', 0, 20, 0, 50, 0);

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
    ('d.inquisition', false, 25, 'd.inquisition.success', 'd.inquisition.partialSuccess', 'd.inquisition.failure', 'https://storage.googleapis.com/withergate-images/disasters/inquisition.png');

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

INSERT INTO disaster_solutions(identifier, disaster, solution_type, difficulty, bonus, junk_cost, caps_cost, food_cost) VALUES
    ('d.inquisition.s1', 'd.inquisition', 'AUTOMATIC', 0, 5, 0, 0, 0),
    ('d.inquisition.s2', 'd.inquisition', 'INTELLECT', 5, 10, 0, 0, 0),
    ('d.inquisition.s3', 'd.inquisition', 'CRAFTSMANSHIP', 6, 12, 10, 0, 0),
    ('d.inquisition.s4', 'd.inquisition', 'AUTOMATIC', 0, 20, 0, 50, 0);

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
    ('d.inquisition.s3', 'cs', 'Podstrčíme jim padělek svatých ostatků, kostí samotného Atoma. Po takovém daru je jasné, že mezi námi Ďábel žít nemůže. Výsledek této ake je ovlivněn zručností postavy.'),
    ('d.inquisition.s4', 'en', 'Even the faith can be bought.'),
    ('d.inquisition.s4', 'cs', 'I víra se dá koupit.');

-- Refugees
INSERT INTO disaster_details(identifier, final_disaster, fame_reward, success_text, partial_success_text, failure_text, image_url) VALUES
    ('d.refugees', false, 25, 'd.refugees.success', 'd.refugees.partialSuccess', 'd.refugees.failure', 'https://storage.googleapis.com/withergate-images/disasters/refugees.png');

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

INSERT INTO disaster_solutions(identifier, disaster, solution_type, difficulty, bonus, junk_cost, caps_cost, food_cost) VALUES
    ('d.refugees.s1', 'd.refugees', 'AUTOMATIC', 0, 5, 0, 0, 0),
    ('d.refugees.s2', 'd.refugees', 'INTELLECT', 7, 10, 0, 0, 0),
    ('d.refugees.s3', 'd.refugees', 'AUTOMATIC', 0, 12, 0, 0, 30),
    ('d.refugees.s4', 'd.refugees', 'COMBAT', 4, 12, 0, 0, 0);

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
    ('d.storm', false, 25, 'd.storm.success', 'd.storm.partialSuccess', 'd.storm.failure', 'https://storage.googleapis.com/withergate-images/disasters/storm.png');

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

INSERT INTO disaster_solutions(identifier, disaster, solution_type, difficulty, bonus, junk_cost, caps_cost, food_cost) VALUES
    ('d.storm.s1', 'd.storm', 'AUTOMATIC', 0, 5, 0, 0, 0),
    ('d.storm.s2', 'd.storm', 'CRAFTSMANSHIP', 7, 10, 0, 0, 0),
    ('d.storm.s3', 'd.storm', 'CRAFTSMANSHIP', 5, 12, 10, 0, 0),
    ('d.storm.s4', 'd.storm', 'AUTOMATIC', 4, 20, 0, 50, 0);

INSERT INTO localized_texts(disaster_solution_name, lang, text) VALUES
    ('d.storm.s1', 'en', 'Hide everything we have'),
    ('d.storm.s1', 'cs', 'Všechno schovat'),
    ('d.storm.s2', 'en', 'Anti-flood barriers'),
    ('d.storm.s2', 'cs', 'Protipovodňové zábrany'),
    ('d.storm.s3', 'en', 'Use some junk and fortify everything'),
    ('d.storm.s3', 'cs', 'Použít šrot a vše zpevnit'),
    ('d.storm.s4', 'en', 'Hire external workforce'),
    ('d.storm.s4', 'cs', 'Najmout námezdníky');

INSERT INTO localized_texts(disaster_solution_description, lang, text) VALUES
    ('d.storm.s1', 'en', 'Our belongings and provisions are scattered all around, in fragile shelters, where they can be hit by a raging element. Let’s hide it below the ground, and hammer it down.'),
    ('d.storm.s1', 'cs', 'Naše vybavení a zásoby se válí všude okolo, v křehkých přístřešcích, kde je může zasypat běsnící živel... Pojďme vše uschovat pod zem a daleko od vody, a ideálně ještě přibít. Jakkoliv.'),
    ('d.storm.s2', 'en', 'Even such a lovely stream which is right near us can flood everything. With a small practice with a shovel we can make it flood everything somewhere else.'),
    ('d.storm.s2', 'cs', 'I takový potůček, jaký teče hned u nás, se může vylít z břehů a všechno spláchnout. Po troše zápolení s lopatou a hlínou zajistíme, aby se z břehu vylil někde jinde. Úspěch akce závisí na zručnosti postavy.'),
    ('d.storm.s3', 'en', 'Some of our shelters are somewhat fragile. They can be damaged in a storm and hurt us inside. Provided that we know, we can fix them with a proper material. The success of this action depends on the character`s craftsmanship.'),
    ('d.storm.s3', 'cs', 'Některá naše stavení jsou spíše chatrná. Hrozí jejich zřícení či poškození a ztráty na majetku i na zdraví. Pokud víme, jak na to, můžeme je s použitím vhodného materiálu trochu vyspravit. Úspěch akce závisí na zručnosti postavy.'),
    ('d.storm.s4', 'en', 'Some enterprising persons from the vicinity are claiming they exactly know what do, and for a few caps they can help us. Afterwards, they will go with their stormcraft elsewhere.'),
    ('d.storm.s4', 'cs', 'Někteří podnikavci z okolí tvrdí, že přesně ví, co máme udělat, a za pár zátek to pro nás i provedou. Mohli by pro nás být užiteční, než půjdou se svým bouřným uměním zase o dům dál...');

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

INSERT INTO disaster_solutions(identifier, disaster, solution_type, difficulty, bonus, junk_cost, caps_cost, food_cost) VALUES
    ('d.clans.s1', 'd.clans', 'AUTOMATIC', 0, 3, 0, 0, 0),
    ('d.clans.s2', 'd.clans', 'INTELLECT', 7, 6, 0, 0, 0),
    ('d.clans.s3', 'd.clans', 'CRAFTSMANSHIP', 5, 6, 10, 0, 0),
    ('d.clans.s4', 'd.clans', 'COMBAT', 6, 8, 0, 0, 0),
    ('d.clans.s5', 'd.clans', 'AUTOMATIC', 0, 15, 0, 150, 0);

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
    ('d.clans.s4', 'en', 'V Pustině se šíří řeči, že náš klan je neschopný. Malý ptáček nám naštěstí pověděl, kdo je zdrojem těchto lží a pomluv. Pojďme mu ukázat, kdo je tu pánem. Akce vyvolá boj s rizikem zranění či smrti a rovněž neúspěchu.'),
    ('d.clans.s4', 'cs', 'There are rumors in the wasteland that our clan is incapable and weak. A little bird told us about the source of these lies. We should find them and teach them some manners! This action will trigger a combat and may lead to an injury, death and failure of the character.'),
    ('d.clans.s5', 'en', 'Zátky řeší vše... bude jich ale potřeba opravdu hodně, protože nejsme jediní, kdo platí.'),
    ('d.clans.s5', 'cs', 'Everything can be bought with caps. But we will need a lot of them since we are not the only ones paying.');