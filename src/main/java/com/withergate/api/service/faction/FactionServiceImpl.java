package com.withergate.api.service.faction;

import com.withergate.api.model.faction.Faction;
import com.withergate.api.repository.faction.FactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Faction service implementation.
 *
 * @author Martin Myslik
 */
@Service
@RequiredArgsConstructor
public class FactionServiceImpl implements FactionService {

    private final FactionRepository factionRepository;

    @Override
    public List<Faction> getFactions() {
        return factionRepository.findAll();
    }
}
