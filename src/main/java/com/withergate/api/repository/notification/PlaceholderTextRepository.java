package com.withergate.api.repository.notification;

import com.withergate.api.model.notification.PlaceholderText;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * PlaceholderText repository.
 *
 * @author Martin Myslik
 */
@Repository
public interface PlaceholderTextRepository extends JpaRepository<PlaceholderText, Integer> {

    List<PlaceholderText> findAllByCode(String code);

}
