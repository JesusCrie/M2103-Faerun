package com.jesus_crie.faerun.model.board;

import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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

    public void addResources(final int amount) {
        resources += amount;
    }

    public void queueWarriors(@Nonnull final Warrior... ws) {
        for (Warrior w : ws) {
            trainingQueue.offer(w);
        }
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
        final int cost = baseCost * w.getCost();

        if (cost > resources)
            return false;

        resources -= cost;
        return true;
    }
}
