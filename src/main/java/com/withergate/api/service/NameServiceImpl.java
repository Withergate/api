package com.withergate.api.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.withergate.api.model.character.Avatar;
import com.withergate.api.model.character.Gender;
import com.withergate.api.model.character.Name;
import com.withergate.api.model.character.NamePrefix;
import com.withergate.api.repository.AvatarRepository;
import com.withergate.api.repository.NamePrefixRepository;
import com.withergate.api.repository.NameRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Name service.
 *
 * @author Martin Myslik
 */
@Slf4j
@AllArgsConstructor
@Service
public class NameServiceImpl implements NameService {

    private final NamePrefixRepository namePrefixRepository;
    private final NameRepository nameRepository;
    private final AvatarRepository avatarRepository;
    private final RandomService randomService;

    @Override
    public String generateRandomName(Gender gender, Set<String> nameFilter) {
        log.debug("Generating {} name.", gender);

        // fetch and filter name parts
        List<NamePrefix> prefixes = namePrefixRepository.findAll()
                .stream()
                .filter(prefix -> !containsNamePart(prefix.getValue(), nameFilter))
                .collect(Collectors.toList());
        List<Name> names = nameRepository.findAllByGender(gender)
                .stream()
                .filter(name -> !containsNamePart(name.getValue(), nameFilter))
                .collect(Collectors.toList());;

        int prefixIndex = randomService.getRandomInt(0, prefixes.size() - 1);
        int nameIndex = randomService.getRandomInt(0, names.size() - 1);

        String name = prefixes.get(prefixIndex).getValue() + " " + names.get(nameIndex).getValue();
        log.debug("Generated name: {}", name);

        return name;
    }

    @Override
    public String generateRandomAvatar(Gender gender, Set<String> avatarFilter) {
        log.debug("Generating {} avatar.", gender);

        // get all unused avatars
        List<Avatar> avatars = avatarRepository.findAllByGender(gender)
                .stream()
                .filter(avatar -> !avatarFilter.contains(avatar.getImageUrl()))
                .collect(Collectors.toList());

        int avatarIndex = randomService.getRandomInt(0, avatars.size() - 1);

        return avatars.get(avatarIndex).getImageUrl();
    }

    private boolean containsNamePart(String candidate, Set<String> names) {
        for (String name : names) {
            if (name.contains(candidate)) {
                return true;
            }
        }

        return false;
    }

}
