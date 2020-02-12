package com.withergate.api.game.repository.action;

import com.withergate.api.game.model.action.BaseAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * BaseAction repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface BaseActionRepository extends JpaRepository<BaseAction, Integer> {}
