package com.jesus_crie.faerun.utils;

import java.util.Random;

public class Dice {

    private static final Random RANDOM = new Random();

    /**
     * Roll a dice of a certain amount of faces, a certain amount of times and return
     * the sum of the rolls.
     *
     * @param faces  - The amount of faces of the dice, the range of the value is [1, faces].
     * @param amount - The amount of dices to roll.
     * @return The sum of the dices.
     */
    public static int diceRoll(final int faces, final int amount) {
        return RANDOM.ints(amount, 1, faces + 1).sum();
    }

    /**
     * Compute the max roll you can get from the {@link #diceRoll(int, int)} method.
     *
     * @param faces  - The amount of faces of the dice.
     * @param amount - The amount of dices to roll.
     * @return The maximum roll you can get with those parameters.
     */
    public static int maxRoll(final int faces, final int amount) {
        return faces * amount;
    }
}
