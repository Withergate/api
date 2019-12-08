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
    ('research.work', 'en', 'Character was working on a research of [].'),
    ('research.work', 'cs', 'Postava  pracovala na výzkumu [].'),
    ('research.new', 'en', 'New research is available: [].'),
    ('research.new', 'cs', 'K dispozici je nový výzkum: [].'),
    ('research.complete', 'en', 'The research of [] was completed.'),
    ('research.complete', 'cs', 'Výzkum [] byl dokončen.'),
    ('research.culinary', 'en', 'Your clan received bonus fame thanks to culinary.'),
    ('research.culinary', 'cs', 'Tvůj klan obdržel slávu navíc díky kulinářství.'),
    ('research.decoration', 'en', 'Your clan received bonus fame thanks to decoration of your clan base.'),
    ('research.decoration', 'cs', 'Tvůj klan obdržel slávu navíc díky dekoraci klanové báze.'),
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
    ('building.study.income', 'en', 'Your Study generated information for your clan.'),
    ('building.study.income', 'cs', 'Ve Studovně se podařilo získat informace pro tvůj klan.'),
    ('character.healing', 'en', 'Character has recovered some health when resting.'),
    ('character.healing', 'cs', 'Postavě se zvedlo zdraví zdraví při odpočinku.'),
    ('character.levelup', 'en', 'Character has advanced to a higher level.'),
    ('character.levelup', 'cs', 'Postava postoupila na další úroveň.'),
    ('character.quest.success', 'en', 'Character successfully completed part of a quest: []'),
    ('character.quest.success', 'cs', 'Postava úspěšně splnila část úkolu: []'),
    ('character.quest.failure', 'en', 'Character did not manage to complete part of the quest: []'),
    ('character.quest.failure', 'cs', 'Postavě se nepodařilo splnit část úkolu: []'),
    ('character.trade.resourcesBuy', 'en', 'Character went to the markeplace and bought some resources.'),
    ('character.trade.resourcesBuy', 'cs', 'Postava na tržišti koupila nějaké zásoby.'),
    ('character.trade.resourcesSell', 'en', 'Character went to the markeplace and sold some resources.'),
    ('character.trade.resourcesSell', 'cs', 'Postava na tržišti prodala nějaké zásoby.'),
    ('quest.completed', 'en', 'Quest was completed: [].'),
    ('quest.completed', 'cs', 'Úkol byl dokončen: [].'),
    ('clan.foodConsumption', 'en', 'Characters are consuming food.'),
    ('clan.foodConsumption', 'cs', 'Postavy konzumují jídlo.'),
    ('clan.trade.item.bought', 'en', 'You bough an item [] from clan {} on the markeplace.'),
    ('clan.trade.item.bought', 'cs', 'Byl zakoupen předmět [] od klanu {} na tržišti.'),
    ('clan.trade.item.sold', 'en', 'Clan {} bought an item [] from you.'),
    ('clan.trade.item.sold', 'cs', 'Klan {} od Tebe koupil předmět [].'),
    ('clan.trade.item.computer', 'en', 'Your item [] was sold on the market.'),
    ('clan.trade.item.computer', 'cs', 'Tvůj předmět [] byl prodán na trhu.'),
    ('clan.information.levelup', 'en', 'Your clan advanced to the next information level.'),
    ('clan.information.levelup', 'cs', 'Tvůj klan postoupil na další úroveň informací.'),
    ('combat.arena.description', 'en', '{} ({}) faced {} ({}) in the arena.'),
    ('combat.arena.description', 'cs', '{} ({}) se utkal/a s {} ({}) v aréně.'),
    ('combat.arena.win', 'en', '{} won the fight.'),
    ('combat.arena.win', 'cs', '{} vyhrál/a souboj.'),
    ('combat.arena.lose', 'en', '{} lost the fight.'),
    ('combat.arena.lose', 'cs', '{} prohrál/a souboj.'),
    ('combat.death', 'en', 'Unfortunately, {} died during the combat.'),
    ('combat.death', 'cs', 'Bohužel, {} zemřel/a během souboje.'),
    ('disaster.success', 'en', 'We successfully handled the disaster [].'),
    ('disaster.success', 'cs', 'Úspěšně jsme odvrátili pohromu [].'),
    ('disaster.partialSuccess', 'en', 'We were partially successful when handling the disaster [].'),
    ('disaster.partialSuccess', 'cs', 'Byli jsme částečně úspěšní při řešení pohromy [].'),
    ('disaster.failure', 'en', 'Unfortunately, we were unsuccessful when handling the disaster [].'),
    ('disaster.failure', 'cs', 'Bohužel jsme byli neúspěšní při řešení pohromy [].'),
    ('disaster.action', 'en', 'Character worked on preventing the disaster. The chosen action was: [].'),
    ('disaster.action', 'cs', 'Postava pracovala na odvrácení pohromy. Zvolila řešení: [].'),
    ('disaster.action.success', 'en', 'The action was successful.'),
    ('disaster.action.success', 'cs', 'Akce dopadla úspěšně.'),
    ('disaster.action.failure', 'en', 'Unfortunately, the action failed.'),
    ('disaster.action.failure', 'cs', 'Bohužel se akci nepodařilo úspěšně dokončit.');

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
    ('detail.character.levelup.trait', 'en', '{} got a new skill point. Do not forget to train the character to receive a trait.'),
    ('detail.character.levelup.trait', 'cs', '{} získal/a dovednostní bod. Nezapomeň postavu vytrénovat, aby získala novou schopnost.'),
    ('detail.character.starving', 'en', '{} is starving.'),
    ('detail.character.starving', 'cs', '{} hladoví.'),
    ('detail.character.crafting', 'en', '{} crafted [].'),
    ('detail.character.crafting', 'cs', '{} vyrobil/a [].'),
    ('detail.quest.assigned', 'en', 'New quest assigned: [].'),
    ('detail.quest.assigned', 'cs', 'Nový úkol: [].'),
    ('detail.healing.building', 'en', 'Sick bay increased healing by {}.'),
    ('detail.healing.building', 'cs', 'Marodka zlepšila léčbu o {}.'),
    ('detail.combat.rolls', 'en', '{} rolled {} on combat dice. {} rolled {}.'),
    ('detail.combat.rolls', 'cs', '{} hodil/a při souboji {}. {} hodil/a {}.'),
    ('detail.combat.roundresult', 'en', '{} won the round with combat value {}. {} had combat value {}. {} lost {} health'),
    ('detail.combat.roundresult', 'cs', '{} vyhrál/a kolo souboje s bojovým číslem {}. {} měl bojové číslo {}. {} ztrácí {} životy.'),
    ('detail.combat.flee', 'en', '{} fleed the combat with {}% flee chance.'),
    ('detail.combat.flee', 'cs', '{} uprchl/a ze souboje při šanci {}% na útěk.'),
    ('detail.arena.unequip', 'en', '{} left his/her ranged weapon in the clan storage since it is not allowed in the arena.'),
    ('detail.arena.unequip', 'cs', '{} nechal/a svou střelnou zbraň v kempu, jelikož tyto zbraně nejsou v aréně povoleny.'),
    ('detail.item.found.storage', 'en', '{} found [] and took it to your clan storage.'),
    ('detail.item.found.storage', 'cs', '{} nalezl/a [] a vzal/a tento úlovek do klanového skladu.'),
    ('detail.item.bought', 'en', '[] was added to your clan storage.'),
    ('detail.item.bought', 'cs', 'Předmět [] byl přidán do klanového skladu.'),
    ('detail.building.levelup', 'en', '[] advanced to the next level.'),
    ('detail.building.levelup', 'cs', 'Tvé budově [] se zvýšila úroveň.'),
    ('detail.building.study', 'en', 'The building [] increased your research speed.'),
    ('detail.building.study', 'cs', 'Rychlost výzkumu se zvýšila díky budově [].'),
    ('detail.research.begging', 'en', 'Character managed to receive some food when begging.'),
    ('detail.research.begging', 'cs', 'Postavě se podařilo při výzvědách vyžebrat trochu jídla.'),
    ('detail.research.forgery', 'en', 'Character managed to forge some caps when crafting.'),
    ('detail.research.forgery', 'cs', 'Postavě se podařilo při výrobě vytvořit pár falešných zátek.'),
    ('detail.research.cultivation', 'en', 'Character managed to grow some food when resting.'),
    ('detail.research.cultivation', 'cs', 'Postavě se podařilo při odpočinku vypěstovat trochu jídla.'),
    ('detail.research.architecture', 'en', 'Your clan received bonus fame due to the use of Wasteland architecture.'),
    ('detail.research.architecture', 'cs', 'Tvůj klan obdržel slávu navíc díky stavně za použití architektury Pustiny.'),
    ('detail.research.collecting', 'en', 'Your clan paid extra caps when purchasing an item and received bonus fame.'),
    ('detail.research.collecting', 'cs', 'Tvůj klan zaplatil za předmět více zátek a obdržel slávu navíc.'),
    ('detail.trait.builder', 'en', '{} used his/her Builder trait and increased the construction speed.'),
    ('detail.trait.builder', 'cs', '{} použil/a svou schopnost Stavitel a rychlost stavby se zvýšila.'),
    ('detail.trait.boffin', 'en', '{} used his/her [] trait and increased the research speed.'),
    ('detail.trait.boffin', 'cs', '{} použil/a svou schopnost [] a rychlost výzkumu se zvýšila.'),
    ('detail.trait.fighter', 'en', '{} got a combat bonus for having a trait Fighter and fighting with a melee weapon.'),
    ('detail.trait.fighter', 'cs', '{} získal/a bonus k bojové síly za schopnost Rváč při boji se zbraní nablízko.'),
    ('detail.trait.sharpshooter', 'en', '{} got a combat bonus for having a trait Sharpshooter and fighting with a ranged weapon.'),
    ('detail.trait.sharpshooter', 'cs', '{} získal/a bonus k bojové síly za schopnost Trefa do černého při boji se střelnou zbraní.'),
    ('detail.trait.ascetic', 'en', '{} consumes less food because of his Ascetic trait.'),
    ('detail.trait.ascetic', 'cs', '{} konzumuje méně jídla díky své schopnosti Asketa.'),
    ('detail.trait.scavenge', 'en', 'Additional resources were found because of character`s trait [].'),
    ('detail.trait.scavenge', 'cs', 'Bylo nalezeno více kořisti díky schopnosti [].'),
    ('detail.trait.lizard', 'en', '{} increased healed hitpoints due to his/her trait [].'),
    ('detail.trait.lizard', 'cs', '{} použil/a svou schopnost [] a rychlost léčby se zvýšila.'),
    ('detail.trait.contacts', 'en', 'Bonus information received thanks to the trait [].'),
    ('detail.trait.contacts', 'cs', 'Bylo zjištěno více informací díky schopnosti [].'),
    ('detail.trait.sparta', 'en', 'Bonus progress received thanks to the trait [].'),
    ('detail.trait.sparta', 'cs', 'Postup odvracení pohromy byl navýšen díky schopnosti [].'),
    ('detail.gear.bonus.junk', 'en', 'Character found more junk thanks to the quipped gear: [].'),
    ('detail.gear.bonus.junk', 'cs', 'Postava našla více šrotu díky předmětu: [].'),
    ('detail.gear.bonus.food', 'en', 'Character found more food thanks to the quipped gear: [].'),
    ('detail.gear.bonus.food', 'cs', 'Postava našla více jídla díky předmětu: [].'),
    ('detail.gear.bonus.work', 'en', 'Character worked more efficiently thanks to the quipped gear: [].'),
    ('detail.gear.bonus.work', 'cs', 'Postava pracovala efektivněji díky svému vybavení: [].'),
    ('detail.gear.bonus.information', 'en', 'Character gained bonus information thanks to the quipped gear: [].'),
    ('detail.gear.bonus.information', 'cs', 'Postava získala více informací díky svému vybavení: [].'),
    ('detail.action.roll', 'en', 'Action difficulty was {}. Character rolled {} and the total action result was {}.'),
    ('detail.action.roll', 'cs', 'Náročnost akce byla {}. Postava hodila {} a celkový výsledek byl {}.'),
    ('detail.disaster.action.success', 'en', 'Character successfully completed the task and added {}% to the disaster resolution.'),
    ('detail.disaster.action.success', 'cs', 'Postava úspěšně dokončila úkol a přidala {}% do vyřešení pohromy.'),
    ('detail.disaster.character.injury', 'en', '{} lost {} health as a result of the disaster.'),
    ('detail.disaster.character.injury', 'cs', '{} utrpěl/a {} zranění v důsledku pohromy.'),
    ('detail.disaster.resource.loss', 'en', 'Your clan lost some resources as a result of the disaster.'),
    ('detail.disaster.resource.loss', 'cs', 'Tvůj klan přišel v důsledku pohromy o suroviny.'),
    ('detail.disaster.fame.loss', 'en', 'Your clan lost some fame as a result of the disaster.'),
    ('detail.disaster.fame.loss', 'cs', 'Tvůj klan přišel v důsledku pohromy o slávu.'),
    ('detail.disaster.item.loss', 'en', 'You lost an item [] as a result of the disaster.'),
    ('detail.disaster.item.loss', 'cs', 'V důsledku pohromy tvůj klan přišel o předmět [].'),
    ('detail.disaster.building.destruction', 'en', 'Your building [] lost some progress as a result of the disaster.'),
    ('detail.disaster.building.destruction', 'cs', 'V důsledku pohromy tvá budova [] utrpěla škody.');
