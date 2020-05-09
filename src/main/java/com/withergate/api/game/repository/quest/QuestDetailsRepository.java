package com.withergate.api.game.repository.quest;

import com.withergate.api.game.model.quest.QuestDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * QuestDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface QuestDetailsRepository extends JpaRepository<QuestDetails, String> {
    List<QuestDetails> findAllByFaction(String faction);
    List<QuestDetails> findAllByTurnLessThan(int turn);
}
