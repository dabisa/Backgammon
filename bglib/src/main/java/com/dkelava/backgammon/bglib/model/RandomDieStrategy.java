package com.dkelava.backgammon.bglib.model;

import java.util.Random;

/**
 * Strategy for generating randon dice
 */
public class RandomDieStrategy implements Die.Strategy {

    private Random rand = new Random();

    @Override
    public Die.Face roll() {
        // TODO: create better random numbers
        return Die.Face.valueOf(rand.nextInt(6) + 1);
    }
}
