package com.jesus_crie.faerun.event;

/**
 * Ask the player to choose what he want to train.
 */
public final class AskQueueEvent implements AskEvent {

    private static final long serialVersionUID = -945873319683978188L;

    private final int availableResources;

    public AskQueueEvent(final int availableResources) {
        this.availableResources = availableResources;
    }

    public int getAvailableResources() {
        return availableResources;
    }
}
