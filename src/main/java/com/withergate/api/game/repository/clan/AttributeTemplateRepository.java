package com.withergate.api.game.repository.clan;

import com.withergate.api.game.model.type.AttributeTemplate;
import com.withergate.api.game.model.type.AttributeTemplate.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Clan repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface AttributeTemplateRepository extends JpaRepository<AttributeTemplate, Integer> {

    AttributeTemplate findFirstByTypeAndSum(Type type, int sum);

}
