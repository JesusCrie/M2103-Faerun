package com.jesus_crie.faerun.model.board;

import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.util.*;

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

    @Nonnull
    public Player getOwner() {
        return owner;
    }

    public int getResources() {
        return resources;
    }

    public int getBaseCost() {
        return baseCost;
    }

    public Collection<Warrior> getTrainingQueue() {
        return Collections.unmodifiableCollection(trainingQueue);
    }

    public void addResources(final int amount) {
        resources += amount;
    }

    public void queueWarriors(@Nonnull final Warrior... ws) {
        for (Warrior w : ws) trainingQueue.offer(w);
    }

    public void queueWarrior(@Nonnull final Warrior w) {
        trainingQueue.offer(w);
    }

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

    private boolean trainSingle(@Nonnull final Warrior w) {
        final int cost = baseCost * w.getCostFactor();

        if (cost > resources)
            return false;

        resources -= cost;
        return true;
    }
}
