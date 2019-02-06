package faerun.logic;

import faerun.game.Player;
import faerun.warrior.Warrior;

import java.util.ArrayList;
import java.util.List;

public class BoardCell {

    private List<Warrior> waitingList = new ArrayList<>();
    private List<Warrior> warriors = new ArrayList<>();

    /**
     * @return True if the cell is empty.
     */
    public boolean isEmpty() {
        return warriors.isEmpty();
    }

    /**
     * Check whether there are enemies on this cell.
     *
     * @param source - The actual player.
     * @return True if there is one or more warrior that don't belongs to source.
     */
    public boolean isThereEnemiesOf(final Player source) {
        return warriors.stream().anyMatch(w -> !w.getOwner().equals(source));
    }

    /**
     * @return The list of the warriors on this cell.
     */
    public List<Warrior> getWarriors() {
        return warriors;
    }

    /**
     * Add some warriors on this cell.
     * Call {@link #flush()} to actually add them.
     *
     * @param warriors - The warriors to put on this cell.
     */
    public void addWarriors(final List<Warrior> warriors) {
        this.waitingList.addAll(warriors);
    }

    /**
     * Merge the warriors waiting to be moved here.
     */
    public void flush() {
        warriors.addAll(waitingList);
        waitingList.clear();
    }
}
