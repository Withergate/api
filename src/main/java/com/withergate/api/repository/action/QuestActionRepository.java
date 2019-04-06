package com.withergate.api.repository.action;

import com.withergate.api.model.action.ActionState;
import com.withergate.api.model.action.QuestAction;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * QuestAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface QuestActionRepository extends JpaRepository<QuestAction, Integer> {

    List<QuestAction> findAllByState(ActionState state);

}
