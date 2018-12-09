package com.withergate.api.repository;

import com.withergate.api.model.character.Avatar;
import com.withergate.api.model.character.Gender;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Avatar repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface AvatarRepository extends JpaRepository<Avatar, String> {

    List<Avatar> findAllByGender(Gender gender);

}
