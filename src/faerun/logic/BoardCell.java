package faerun.logic;

import faerun.warrior.Warrior;

import java.lang.ref.WeakReference;

public class BoardCell {

    private WeakReference<? extends Warrior> warrior = new WeakReference<>(null);

    /**
     * @return True if the cell is empty.
     */
    public boolean isEmpty() {
        return warrior.get() == null;
    }

    /**
     * @return The warrior of this cell or {@code null}.
     */
    public Warrior getWarrior() {
        return warrior.get();
    }

    /**
     * Set the active warrior of this cell.
     *
     * @param warrior - The warrior to put on this cell.
     */
    public void setWarrior(Warrior warrior) {
        this.warrior = new WeakReference<>(warrior);
    }
}
