package com.withergate.api.service;

import com.withergate.api.model.character.Avatar;
import com.withergate.api.model.character.Gender;
import com.withergate.api.model.character.Name;
import com.withergate.api.model.character.NamePrefix;
import com.withergate.api.repository.AvatarRepository;
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
public class NameServiceImpl implements NameService {

    private final NamePrefixRepository namePrefixRepository;
    private final NameRepository nameRepository;
    private final AvatarRepository avatarRepository;
    private final RandomService randomService;

    public NameServiceImpl(NamePrefixRepository namePrefixRepository, NameRepository nameRepository,
                           AvatarRepository avatarRepository, RandomService randomService) {
        this.namePrefixRepository = namePrefixRepository;
        this.nameRepository = nameRepository;
        this.avatarRepository = avatarRepository;
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

    @Override
    public String generateRandomAvatar(Gender gender) {
        log.debug("Generating {} avatar.", gender);

        List<Avatar> avatars = avatarRepository.findAllByGender(gender);

        int avatarIndex = randomService.getRandomInt(0, avatars.size() - 1);

        return avatars.get(avatarIndex).getImageUrl();
    }

}
