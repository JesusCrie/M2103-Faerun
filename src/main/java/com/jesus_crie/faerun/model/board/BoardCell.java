package com.jesus_crie.faerun.model.board;

import com.jesus_crie.faerun.model.Side;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Represent a cell on the board.
 */
public class BoardCell {

    private final int position;
    private final List<Warrior> warriors = new ArrayList<>();

    public BoardCell(int position) {
        this.position = position;
    }

    /**
     * @return The position of the cell on the board.
     */
    public int getPosition() {
        return position;
    }

    /**
     * @return The warriors currently on this cell.
     */
    public List<Warrior> getWarriors() {
        return warriors;
    }

    /**
     * @return The side of this cell with regard to the warriors on it.
     */
    @Nonnull
    public Side getSide() {
        if (getWarriors().size() == 0)
            return Side.EMPTY;
        else if (getWarriors().size() == countAllies(Side.RIGHT))
            return Side.RIGHT;
        else if (getWarriors().size() == countAllies(Side.LEFT))
            return Side.LEFT;
        else
            return Side.BOTH;
    }

    /**
     * Add some warriors on this cell.
     *
     * @param ws - The warriors to add.
     */
    public void addWarriors(@Nonnull final List<Warrior> ws) {
        warriors.addAll(ws);
    }

    /**
     * Remove some warriors from this cell.
     *
     * @param ws - The warriors to remove.
     */
    public void removeWarriors(@Nonnull final List<Warrior> ws) {
        warriors.removeAll(ws);
    }

    /**
     * Remove and return all warriors of this cell.
     * Useful to move everyone.
     *
     * @return All of the warriors of this cell.
     */
    @Nonnull
    public List<Warrior> popAll() {
        final List<Warrior> copy = new ArrayList<>(warriors);
        warriors.clear();

        return copy;
    }

    /**
     * @return The amount of warriors on this cell.
     */
    public int countAll() {
        return warriors.size();
    }

    /**
     * Count the amount of warriors of the same side as the owner.
     *
     * @param owner - The owner of the warriors.
     * @return The amount of warriors that belongs to the given owner.
     */
    public int countAllies(@Nonnull final Side owner) {
        return (int) warriors.stream()
                .filter(w -> w.getOwner().getSide() == owner)
                .count();
    }

    /**
     * Count the amount of warriors of the opposite side of the owner.
     *
     * @param owner - The owner of the warriors.
     * @return The amount of warriors that don't belongs to the given owner.
     */
    public int countEnemies(@Nonnull final Side owner) {
        return (int) warriors.stream()
                .filter(w -> w.getOwner().getSide() != owner)
                .count();
    }
}
