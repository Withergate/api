package com.withergate.api.service.clan;

import java.util.Collections;
import java.util.List;

import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.character.TraitDetails.TraitName;
import com.withergate.api.repository.clan.CharacterRepository;
import com.withergate.api.repository.clan.TraitDetailsRepository;
import com.withergate.api.service.exception.InvalidActionException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Trait service implementation.
 *
 * @author Martin Myslik
 */
@Service
@AllArgsConstructor
public class TraitServiceImpl implements TraitService {

    private final TraitDetailsRepository traitDetailsRepository;
    private final CharacterRepository characterRepository;

    @Override
    public void assignTraits(Character character) {
        List<TraitDetails> detailsList = traitDetailsRepository.findAll();
        Collections.shuffle(detailsList);

        for (int i = 0; i < detailsList.size(); i++) {
            Trait trait = new Trait();
            trait.setDetails(detailsList.get(i));
            trait.setOrder(i);
            trait.setActive(false);

            character.getTraits().put(detailsList.get(i).getIdentifier(), trait);
        }
    }

    @Transactional
    @Override
    public void activateTrait(int characterId, int clanId, TraitName traitName) throws InvalidActionException {
        Character character = characterRepository.getOne(characterId);
        Clan clan = character.getClan();

        if (clan.getId() != clanId) {
            throw new InvalidActionException("This character does not belong to your clan.");
        }

        if (character.getSkillPoints() < 1) {
            throw new InvalidActionException("This character has no available skill points!");
        }

        if (character.getTraits().get(traitName).isActive()) {
            throw new InvalidActionException("This trait has been activated already.");
        }

        long maxOrder = character.getMaxOrder();
        if (character.getTraits().get(traitName).getOrder() > maxOrder) {
            throw new InvalidActionException("This trait is not available for the provided character.");
        }

        character.getTraits().get(traitName).setActive(true);
        character.setSkillPoints(character.getSkillPoints() - 1);

        // mark character as resting
        character.setState(CharacterState.RESTING);
    }

}
