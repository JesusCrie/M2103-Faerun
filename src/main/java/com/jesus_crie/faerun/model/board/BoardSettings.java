package com.jesus_crie.faerun.model.board;

import java.io.Serializable;

/**
 * Contains the global settings of the board.
 */
public class BoardSettings implements Serializable {

    private static final long serialVersionUID = 8536695707603265014L;

    private final int size;
    private final int baseCost;
    private final int diceAmount;
    private final int initialResources;
    private final int resourcesPerRound;

    public BoardSettings(final int size,
                         final int baseCost,
                         final int diceAmount,
                         final int initialResources,
                         final int resourcesPerRound) {
        this.size = size;
        this.baseCost = baseCost;
        this.diceAmount = diceAmount;
        this.initialResources = initialResources;
        this.resourcesPerRound = resourcesPerRound;
    }

    /**
     * @return The size of the board.
     */
    public int getSize() {
        return size;
    }

    /**
     * @return The cost multiplier.
     */
    public int getBaseCost() {
        return baseCost;
    }

    /**
     * @return The amount of dices to roll.
     */
    public int getDiceAmount() {
        return diceAmount;
    }

    /**
     * @return The initial amount of resources of each player.
     */
    public int getInitialResources() {
        return initialResources;
    }

    /**
     * @return The amount of resources gained per round.
     */
    public int getResourcesPerRound() {
        return resourcesPerRound;
    }

    @Override
    public String toString() {
        return String.format("BoardSettings[size: %d, baseCost: %d, diceAmount: %d, initialResources: %d, resourcesPerRound: %d]",
                getSize(), getBaseCost(), getDiceAmount(), getInitialResources(), getResourcesPerRound());
    }
}
