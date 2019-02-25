package com.jesus_crie.faerun.event;

import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * Ask the player to choose what he want to train.
 */
public final class AskQueueEvent implements AskEvent<Map<Class<? extends Warrior>, Integer>> {

    private static final long serialVersionUID = -945873319683978188L;

    private final int availableResources;
    private final String[] currentQueue;

    public AskQueueEvent(final int availableResources,
                         @Nonnull final Class<? extends Warrior>[] currentQueue) {
        this.availableResources = availableResources;
        this.currentQueue = Arrays.stream(currentQueue)
                .map(Class::getName)
                .toArray(String[]::new);
    }

    /**
     * @return The available resources of the castle.
     */
    public int getAvailableResources() {
        return availableResources;
    }

    /**
     * @return The current queue, converted back into a Class[].
     */
    @SuppressWarnings("unchecked")
    public Class<? extends Warrior>[] getCurrentQueue() {
        return Arrays.stream(currentQueue)
                .map(className -> {
                    try {
                        return Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(Class[]::new);
    }
}
