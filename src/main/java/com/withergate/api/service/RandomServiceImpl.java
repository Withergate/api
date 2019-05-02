package com.withergate.api.service;

import com.withergate.api.model.character.Gender;
import com.withergate.api.model.item.ItemType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Random service.
 *
 * @author Martin Myslik
 */
@Slf4j
@Service
public class RandomServiceImpl implements RandomService {

    // random dice rolls
    public static final int K6 = 6;
    public static final int K10 = 10;
    public static final int K100 = 100;

    @Override
    public Gender getRandomGender() {
        Random rand = new Random();
        double random = rand.nextDouble();

        if (random < 0.5) {
            return Gender.FEMALE;
        } else {
            return Gender.MALE;
        }
    }

    @Override
    public int getRandomInt(int min, int max) {
        Random rand = new Random();
        return min + rand.nextInt((max - min) + 1);
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
}
