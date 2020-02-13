package com.withergate.api.profile.repository;

import java.util.List;

import com.withergate.api.profile.model.achievement.AchievementDetails;
import com.withergate.api.profile.model.achievement.AchievementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Achievement details repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface AchievementDetailsRepository extends JpaRepository<AchievementDetails, String> {

    List<AchievementDetails> findAllByType(AchievementType type);

}
