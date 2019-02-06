package faerun.logic;

import faerun.warrior.*;

import java.util.*;

public class Castle {

    // Constants
    public static final int RESOURCES_PER_TURN = 1;

    // Resouces
    private int novices;
    private int resources = 3;

    // Warriors
    private List<Warrior> warriorsReady = new ArrayList<>();
    private Queue<Warrior> trainingQueue = new LinkedList<>();

    /**
     * @return The amount of novices in this castle
     */
    public int getNovices() {
        return novices;
    }

    /**
     * @return The amount of resources in this castle
     */
    public int getResources() {
        return resources;
    }

    /**
     * @return A view of the available warriors in this castle
     */
    public List<Warrior> getWarriorsReady() {
        return Collections.unmodifiableList(warriorsReady);
    }

    /**
     * Automatically gather the predefined {@link #RESOURCES_PER_TURN} as resources
     */
    public void autoCollectResources() {
        resources += RESOURCES_PER_TURN;
    }

    /**
     * Add the given warriors to the training queue.
     */
    public void queueTraining(Warrior... warriors) {
        for (Warrior warrior : warriors)
            trainingQueue.offer(warrior);
    }

    /**
     * Consume as much of the training queue as possible.
     */
    public void train() {
        while (trainSingle(trainingQueue.poll())) {
            // Train
        }
    }

    /**
     * Try to train a single warrior.
     * @param w - The warrior to train, can be {@code null}.
     * @return True if the warrior was successfully trained
     */
    private boolean trainSingle(final Warrior w) {
        if (w == null)
            return false;

        // Calculate cost
        final int cost = w.getCost();

        // Check available resources
        if (cost > resources || novices <= 0)
            return false;

        // Use resources
        resources -= cost;
        --novices;

        // Train warrior
        warriorsReady.add(w);
        return true;
    }
}
