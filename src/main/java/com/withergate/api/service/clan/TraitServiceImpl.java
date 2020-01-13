package com.withergate.api.service.clan;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.withergate.api.GameProperties;
import com.withergate.api.model.Clan;
import com.withergate.api.model.character.Character;
import com.withergate.api.model.character.CharacterState;
import com.withergate.api.model.character.Trait;
import com.withergate.api.model.character.TraitDetails;
import com.withergate.api.model.request.TraitRequest;
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

    private static final int TRAIT_LIMIT = 10;

    private final TraitDetailsRepository traitDetailsRepository;
    private final CharacterRepository characterRepository;
    private final GameProperties properties;

    @Override
    public void assignTraits(Character character) {
        List<TraitDetails> detailsList = traitDetailsRepository.findAll();
        Collections.shuffle(detailsList);

        for (int i = 0; i < Math.min(detailsList.size(), TRAIT_LIMIT); i++) {
            Trait trait = new Trait();
            trait.setDetails(detailsList.get(i));
            trait.setOrder(i);
            trait.setActive(false);

            character.getTraits().add(trait);
        }
    }

    @Transactional
    @Override
    public void activateTrait(TraitRequest request, int clanId) throws InvalidActionException {
        Character character = characterRepository.getOne(request.getCharacterId());
        Clan clan = character.getClan();

        if (clan.getId() != clanId) {
            throw new InvalidActionException("This character does not belong to your clan.");
        }

        if (character.getSkillPoints() < 1) {
            throw new InvalidActionException("This character has no available skill points!");
        }

        Optional<Trait> trait = character.getTraits().stream().filter(t ->
                t.getDetails().getIdentifier().equals(request.getTraitName())).findFirst();
        if (trait.isEmpty() || trait.get().isActive()) {
            throw new InvalidActionException("This trait does not exist or has been activated already.");
        }

        long maxOrder = character.getMaxOrder();
        if (trait.get().getOrder() > maxOrder) {
            throw new InvalidActionException("This trait is not available for the provided character.");
        }

        if (request.isImmediate()) {
            if (clan.getCaps() < properties.getTrainingPrice()) {
                throw new InvalidActionException("Not enough caps to perform training.");
            }
            // pay price for immediate training
            clan.changeCaps(- properties.getTrainingPrice());
        }

        trait.get().setActive(true);
        character.setSkillPoints(character.getSkillPoints() - 1);

        // mark character as resting unless immediate training
        if (!request.isImmediate()) {
            character.setState(CharacterState.RESTING);
        }
    }

}
