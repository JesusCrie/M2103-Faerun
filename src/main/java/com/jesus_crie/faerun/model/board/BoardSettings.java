package com.jesus_crie.faerun.model.board;

public class BoardSettings {

    private final int size;
    private final int baseCost;
    private final int diceAmount;
    private final int initialResources;

    public BoardSettings(int size, int baseCost, int diceAmount, int initialResources) {
        this.size = size;
        this.baseCost = baseCost;
        this.diceAmount = diceAmount;
        this.initialResources = initialResources;
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
}
