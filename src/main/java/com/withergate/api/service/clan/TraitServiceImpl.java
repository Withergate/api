package com.withergate.api.service.clan;

import com.withergate.api.GameProperties;
import com.withergate.api.game.model.Clan;
import com.withergate.api.game.model.character.Character;
import com.withergate.api.game.model.character.Trait;
import com.withergate.api.game.model.character.TraitDetails;
import com.withergate.api.game.model.request.RestingRequest;
import com.withergate.api.game.model.request.TraitRequest;
import com.withergate.api.game.repository.clan.CharacterRepository;
import com.withergate.api.game.repository.clan.TraitDetailsRepository;
import com.withergate.api.service.exception.InvalidActionException;
import com.withergate.api.service.utils.ResourceUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Trait service implementation.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
@AllArgsConstructor
public class TraitServiceImpl implements TraitService {

    private static final int TRAIT_LIMIT = 10;

    private final TraitDetailsRepository traitDetailsRepository;
    private final CharacterRepository characterRepository;
    private final RestingService restingService;
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
        checkTraitRequirements(character, clan, clanId);

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
            ResourceUtils.changeCaps(- properties.getTrainingPrice(), clan, null);
        }

        trait.get().setActive(true);
        character.setSkillPoints(character.getSkillPoints() - 1);

        // mark character as resting unless immediate training
        if (!request.isImmediate()) {
            RestingRequest restingRequest = new RestingRequest();
            restingRequest.setCharacterId(character.getId());
            restingService.saveAction(restingRequest, character.getId());
        }
    }

    @Transactional
    @Override
    public void forgetTrait(TraitRequest request, int clanId) throws InvalidActionException {
        Character character = characterRepository.getOne(request.getCharacterId());
        Clan clan = character.getClan();
        checkTraitRequirements(character, clan, clanId);

        // check the price
        if (clan.getCaps() < properties.getTraitForgetPrice()) {
            throw new InvalidActionException("Not enough caps to perform training.");
        }

        // change trait order
        Trait trait = character.getTraits().stream()
                .filter(t -> t.getDetails().getIdentifier().equals(request.getTraitName()))
                .findFirst().orElseThrow();
        for (Trait t : character.getTraits()) {
            if (t.getOrder() > trait.getOrder()) {
                t.setOrder(t.getOrder() - 1);
            }
        }
        trait.setOrder(TRAIT_LIMIT - 1);

        // pay price
        ResourceUtils.changeCaps(- properties.getTraitForgetPrice(), clan, null);

    }

    private void checkTraitRequirements(Character character, Clan clan, int clanId) throws InvalidActionException {
        if (clan.getId() != clanId) {
            throw new InvalidActionException("This character does not belong to your clan.");
        }

        if (character.getSkillPoints() < 1) {
            throw new InvalidActionException("This character has no available skill points!");
        }
    }

}
