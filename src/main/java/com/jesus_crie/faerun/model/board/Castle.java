package com.jesus_crie.faerun.model.board;

import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Represent the castle of a player.
 */
public class Castle {

    private final Player owner;
    private final int baseCost;
    private int resources;
    private final Queue<Warrior> trainingQueue = new LinkedList<>();

    public Castle(@Nonnull final Player owner,
                  final int baseCost,
                  final int initialResources) {
        this.owner = owner;
        this.baseCost = baseCost;
        resources = initialResources;
    }

    /**
     * @return The owner of this castle.
     */
    @Nonnull
    public Player getOwner() {
        return owner;
    }

    /**
     * @return The current resources available in this castle.
     */
    public int getResources() {
        return resources;
    }

    /**
     * @return The cost multiplier for this castle.
     */
    public int getBaseCost() {
        return baseCost;
    }

    /**
     * @return An unmodifiable view of the current training queue of this castle.
     */
    public Collection<Warrior> getTrainingQueue() {
        return Collections.unmodifiableCollection(trainingQueue);
    }

    /**
     * Add the given amount of resources to this castle.
     *
     * @param amount - The amount of resources to give.
     */
    public void addResources(final int amount) {
        resources += amount;
    }

    /**
     * Queue a bunch of warriors at once.
     *
     * @param ws - The warriors to queue.
     */
    public void queueWarriors(@Nonnull final Warrior... ws) {
        for (Warrior w : ws) trainingQueue.offer(w);
    }

    /**
     * Queue a single warrior in this castle.
     *
     * @param w - The warrior to queue.
     */
    public void queueWarrior(@Nonnull final Warrior w) {
        trainingQueue.offer(w);
    }

    /**
     * Train as much warriors of the queue as possible.
     * @return A list of the queued warriors.
     */
    @Nonnull
    public List<Warrior> train() {
        final List<Warrior> trained = new LinkedList<>();

        Warrior w;
        while ((w = trainingQueue.peek()) != null && trainSingle(w)) {
            trainingQueue.poll();
            trained.add(w);
        }

        return trained;
    }

    /**
     * Try to train a single warrior using this castle's resources.
     * @param w - The warrior to train.
     * @return True if the warrior has been successfully trained, otherwise False.
     */
    private boolean trainSingle(@Nonnull final Warrior w) {
        final int cost = baseCost * w.getCostFactor();

        if (cost > resources)
            return false;

        resources -= cost;
        return true;
    }
}
