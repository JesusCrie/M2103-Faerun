package faerun.logic;

import faerun.warrior.Warrior;

import java.util.*;

public class Castle {

    // Constants
    public static final int RESOURCES_PER_TURN = 1;

    // Resources
    private int resources = 3;

    // Warriors
    private Deque<Warrior> trainingQueue = new LinkedList<>();

    // Position
    private final int position;

    /**
     * @param position - The position of the castle on the board.
     */
    public Castle(final int position) {
        this.position = position;
    }

    /**
     * @return The amount of resources in this castle.
     */
    public int getResources() {
        return resources;
    }

    /**
     * @return A view of the queued warriors.
     */
    public List<Warrior> getWarriorsQueue() {
        return Collections.unmodifiableList((LinkedList<Warrior>) trainingQueue);
    }

    /**
     * @return The position of the castle on the board.
     */
    public int getPosition() {
        return position;
    }

    /**
     * Automatically gather the predefined {@link #RESOURCES_PER_TURN} as resources.
     */
    public void collectResources() {
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
     * Consume as much of the training queue as possible to train warriors.
     *
     * @return A list of the warriors successfully trained.
     */
    public List<Warrior> train() {
        final List<Warrior> trained = new LinkedList<>();

        Warrior w;
        while ((w = trainingQueue.poll()) != null && trainSingle(w))
            trained.add(w);

        return trained;
    }

    /**
     * Try to train a single warrior.
     *
     * @param w - The warrior to train, can be.
     * @return True if the warrior was successfully trained.
     */
    private boolean trainSingle(final Warrior w) {
        // Calculate cost
        final int cost = w.getCost();

        // Check available resources
        if (cost > resources)
            return false;

        // Use resources
        resources -= cost;

        return true;
    }
}
