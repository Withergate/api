package com.withergate.api.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import com.google.common.primitives.Ints;
import com.withergate.api.game.model.type.AttributeTemplate;
import com.withergate.api.game.model.type.AttributeTemplate.Type;
import com.withergate.api.game.model.character.Gender;
import com.withergate.api.game.model.item.ItemType;
import com.withergate.api.game.repository.clan.AttributeTemplateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Random service.
 *
 * @author Martin Myslik
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class RandomServiceImpl implements RandomService {

    // random dice rolls
    public static final int K6 = 6;
    public static final int K4 = 4;
    public static final int K10 = 10;
    public static final int K100 = 100;

    private static final int ATTRIBUTES = 4;

    private final AttributeTemplateRepository templateRepository;

    private Random rand = new Random();

    @Override
    public Gender getRandomGender() {
        double random = rand.nextDouble();

        if (random < 0.5) {
            return Gender.FEMALE;
        } else {
            return Gender.MALE;
        }
    }

    @Override
    public int getRandomInt(int min, int max) {
        return min + rand.nextInt((max - min) + 1);
    }

    @Override
    public boolean getRandomBoolean() {
        double random = rand.nextDouble();

        if (random < 0.5) {
            return true;
        }
        return false;
    }

    @Override
    public ItemType getRandomItemType() {
        int diceRoll = getRandomInt(1, 4);
        switch (diceRoll) {
            case 1:
                return ItemType.WEAPON;
            case 2:
                return ItemType.CONSUMABLE;
            case 3:
                return ItemType.GEAR;
            case 4:
                return ItemType.OUTFIT;
            default:
                log.error("Invalid dice roll.");
        }

        // default return type
        return ItemType.WEAPON;
    }

    @Override
    public AttributeTemplate getRandomAttributeCombination(int n, Type type) {
        if (type.equals(Type.RANDOM)) {
            return generateRandomCombination(n);
        }

        // load preset
        AttributeTemplate template = templateRepository.findFirstByTypeAndSum(type, n);
        if (template == null) {
            log.error("Could not find attribute template {} for type {}. Fallback to random.", n, type);
            return generateRandomCombination(n);
        }
        return template;
    }

    private AttributeTemplate generateRandomCombination(int n) {
        int[] attr = new int[ATTRIBUTES];

        for (int i = 1; i < attr.length; i++) {
            attr[i] = rand.nextInt(K6 - 1);
        }

        Arrays.sort(attr, 1, attr.length);
        for (int i = 1; i < attr.length; i++) {
            attr[i - 1] = attr[i] - attr[i - 1] + 1;
        }
        attr[attr.length - 1] = n - ATTRIBUTES - attr[attr.length - 1] + 1;

        // Redistribute attributes if too large
        int i = 0;
        while (attr[ATTRIBUTES - 1] >= K6) {
            // small chance to keep 6
            if (attr[ATTRIBUTES - 1] == K6 && getRandomInt(1, 100) < 10) continue;

            if (i >= ATTRIBUTES - 1) {
                i = 0;
            }

            if (attr[i] < K6) {
                attr[i]++;
                attr[ATTRIBUTES - 1]--;
            }

            i++;
        }

        // shuffle
        Collections.shuffle(Ints.asList(attr));
        AttributeTemplate template = new AttributeTemplate();
        template.setCombat(attr[0]);
        template.setScavenge(attr[1]);
        template.setCraftsmanship(attr[2]);
        template.setIntellect(attr[3]);
        return template;
    }

}
