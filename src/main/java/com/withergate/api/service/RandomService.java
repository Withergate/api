package com.withergate.api.service;

import com.withergate.api.model.character.Gender;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Random service.
 *
 * @author Martin Myslik
 */
@Service
public class RandomService implements IRandomService {

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
}
