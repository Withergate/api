-- Placeholder texts notifications
INSERT INTO placeholder_texts(code, lang, text) VALUES
    ('location.tavern.hired', 'en', 'Character went to the tavern to hire someone for your clan. After spending the evening chatting with several people, the decision fell on {}'),
    ('location.tavern.hired', 'cs', 'Postava šla do hospody s nabídkou lehce vydělaných zátek. Po několika hodinách padlo rozhodnutí na {}.'),
    ('location.resources', 'en', 'Character found some resources.'),
    ('location.resources', 'cs', 'Postava  našla suroviny.'),
    ('location.loot', 'en', 'Character found loot.'),
    ('location.loot', 'cs', 'Postava našla předmět.'),
    ('location.information', 'en', 'Character found valuable information when scouting.'),
    ('location.information', 'cs', 'Postava zjistila při pátrání cenné informace.'),
    ('building.work', 'en', 'Character worked on a construction of [].'),
    ('building.work', 'cs', 'Postava  pracovala na stavbě [].'),
    ('building.crafting.weapon', 'en', 'Character was crafting a weapon.'),
    ('building.crafting.weapon', 'cs', 'Postava vyráběla zbraně.'),
    ('building.crafting.outfit', 'en', 'Character was crafting an outfit.'),
    ('building.crafting.outfit', 'cs', 'Postava vyráběla oděv.'),
    ('building.crafting.gear', 'en', 'Character was crafting a gear.'),
    ('building.crafting.gear', 'cs', 'Postava vyráběla vybavení.'),
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
    ('character.quest.success', 'en', 'Character successfully completed part of the quest.'),
    ('character.quest.success', 'cs', 'Postava úspěšně splnila část úkolu.'),
    ('character.quest.failure', 'en', 'Character did not manage to complete part of the quest.'),
    ('character.quest.failure', 'cs', 'Postavě se nepodařilo splnit část úkolu.'),
    ('character.trade.resourcesBuy', 'en', 'Character went to the markeplace and bought some resources.'),
    ('character.trade.resourcesBuy', 'cs', 'Postava na tržišti koupila nějaké zásoby.'),
    ('character.trade.resourcesSell', 'en', 'Character went to the markeplace and sold some resources.'),
    ('character.trade.resourcesSell', 'cs', 'Postava na tržišti prodala nějaké zásoby.'),
    ('character.trade.item', 'en', 'Character went to the markeplace and bought an item from {}.'),
    ('character.trade.item', 'cs', 'Postava na tržišti koupila předmět od {}.'),
    ('quest.completed', 'en', 'Quest was completed: [].'),
    ('quest.completed', 'cs', 'Úkol byl dokončen: [].'),
    ('clan.foodConsumption', 'en', 'Characters are consuming food.'),
    ('clan.foodConsumption', 'cs', 'Postavy konzumují jídlo.'),
    ('clan.trade.item', 'en', '{} from clan {} bought an item from you.'),
    ('clan.trade.item', 'cs', '{} z klanu {} koupil/a předmět od tvého klanu.'),
    ('clan.trade.item.computer', 'en', 'Your item [] was sold on the market.'),
    ('clan.trade.item.computer', 'cs', 'Tvůj předmět [] byl prodán na trhu.'),
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
    ('detail.character.crafting', 'en', '{} crafted [].'),
    ('detail.character.crafting', 'cs', '{} vyrobil/a [].'),
    ('detail.information.levelup', 'en', 'Your clan advanced to the next information level.'),
    ('detail.information.levelup', 'cs', 'Tvůj klan postoupil na další úroveň informací.'),
    ('detail.quest.assigned', 'en', 'New quest assigned: [].'),
    ('detail.quest.assigned', 'cs', 'Nový úkol: [].'),
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
    ('detail.item.found.storage', 'en', '{} found [] and took it to your clan storage.'),
    ('detail.item.found.storage', 'cs', '{} nalezl/a [] a vzal/a tento úlovek do klanového skladu.'),
    ('detail.item.bought', 'en', '[] was added to your clan storage.'),
    ('detail.item.bought', 'cs', 'Předmět [] byl přidán do klanového skladu.'),
    ('detail.building.levelup', 'en', '[] advanced to the next level.'),
    ('detail.building.levelup', 'cs', 'Tvé budově [] se zvýšila úroveň.'),
    ('detail.trait.builder', 'en', '{} used his/her Builder trait and increased the construction speed.'),
    ('detail.trait.builder', 'cs', '{} použil svou schopnost Stavitel a rychlost stavby se zvýšila.'),
    ('detail.trait.fighter', 'en', '{} got a combat bonus for having a trait Fighter and fighting with a melee weapon.'),
    ('detail.trait.fighter', 'cs', '{} získal/a bonus k bojové síly za schopnost Rváč při boji se zbraní nablízko.'),
    ('detail.trait.ascetic', 'en', '{} does not need to consume any food because of his Ascetic trait.'),
    ('detail.trait.ascetic', 'cs', '{} nemusí konzumovat žádné jídlo díky své schopnosti Asketa.'),
    ('detail.trait.scavenge', 'en', 'Additional resources were found because of character`s trait [].'),
    ('detail.trait.scavenge', 'cs', 'Bylo nalezeno více kořisti díky schopnosti [].'),
    ('gear.bonus.junk', 'en', 'Character found more junk thanks to the quipped gear: [].'),
    ('gear.bonus.junk', 'cs', 'Postava našla více šrotu díky předmětu: [].'),
    ('gear.bonus.food', 'en', 'Character found more food thanks to the quipped gear: [].'),
    ('gear.bonus.food', 'cs', 'Postava našla více jídla díky předmětu: [].'),
    ('gear.bonus.work', 'en', 'Character worked more efficiently thanks to the quipped gear: [].'),
    ('gear.bonus.work', 'cs', 'Postava pracovala efektivněji díky svému vybavení: [].');
