package com.withergate.api.repository.quest;

import com.withergate.api.model.quest.QuestDetails;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * QuestDetails repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface QuestDetailsRepository extends JpaRepository<QuestDetails, String> {

    List<QuestDetails> findAllByInformationLevel(int informationLevel);

}
