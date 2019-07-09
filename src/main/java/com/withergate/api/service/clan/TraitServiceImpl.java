package com.withergate.api.service.clan;

import java.util.List;
import java.util.stream.Collectors;

import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.repository.clan.TraitDetailsRepository;
import com.withergate.api.service.RandomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Trait service implementation.
 *
 * @author Martin Myslik
 */
@Service
@AllArgsConstructor
public class TraitServiceImpl implements TraitService {

    private final TraitDetailsRepository traitDetailsRepository;
    private final RandomService randomService;

    @Override
    public Trait getRandomTrait(Character character) {
        List<TraitDetails> detailsList = traitDetailsRepository.findAll().stream()
                .filter(trait -> !character.getTraits().containsKey(trait.getIdentifier())).collect(
                        Collectors.toList());
        TraitDetails details = detailsList.get(randomService.getRandomInt(0, detailsList.size() - 1));

        Trait trait = new Trait();
        trait.setDetails(details);

        return trait;
    }
}
