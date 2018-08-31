package com.withergate.api.service;

import com.withergate.api.model.character.Gender;
import com.withergate.api.model.character.Name;
import com.withergate.api.model.character.NamePrefix;
import com.withergate.api.repository.NamePrefixRepository;
import com.withergate.api.repository.NameRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Name service.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class NameService implements INameService {

    private final NamePrefixRepository namePrefixRepository;
    private final NameRepository nameRepository;
    private final RandomService randomService;

    /**
     * Constructor.
     *
     * @param namePrefixRepository namePrefix repository
     * @param nameRepository       name repository
     * @param randomService        random service
     */
    public NameService(NamePrefixRepository namePrefixRepository, NameRepository nameRepository,
                       RandomService randomService) {
        this.namePrefixRepository = namePrefixRepository;
        this.nameRepository = nameRepository;
        this.randomService = randomService;
    }

    @Override
    public String generateRandomName(Gender gender) {
        log.debug("Generating {} name.", gender);

        List<NamePrefix> prefixes = namePrefixRepository.findAll();
        List<Name> names = nameRepository.findAllByGender(gender);

        int prefixIndex = randomService.getRandomInt(0, prefixes.size() - 1);
        int nameIndex = randomService.getRandomInt(0, names.size() - 1);

        String name = prefixes.get(prefixIndex).getValue() + " " + names.get(nameIndex).getValue();
        log.debug("Generated name: {}", name);

        return name;
    }

}
