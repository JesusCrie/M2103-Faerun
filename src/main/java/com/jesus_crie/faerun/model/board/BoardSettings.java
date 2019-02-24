package com.jesus_crie.faerun.model.board;

import java.io.Serializable;

public class BoardSettings implements Serializable {

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

    public int getSize() {
        return size;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public int getDiceAmount() {
        return diceAmount;
    }

    public int getInitialResources() {
        return initialResources;
    }

    public int getResourcesPerRound() {
        return resourcesPerRound;
    }

    @Override
    public String toString() {
        return String.format("BoardSettings[size: %d, baseCost: %d, diceAmount: %d, initialResources: %d, resourcesPerRound: %d]",
                getSize(), getBaseCost(), getDiceAmount(), getInitialResources(), getResourcesPerRound());
    }
}
