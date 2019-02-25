package com.jesus_crie.faerun.logic;

import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.board.Board;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Handles the logic related to the management of the board.
 */
public class BoardLogic {

    private final Board board;

    public BoardLogic(@Nonnull final BoardSettings settings,
                      @Nonnull final List<Player> players) {
        board = new Board(settings, players);
    }

    /**
     * @return The associated board.
     */
    @Nonnull
    public Board getBoard() {
        return board;
    }

    /**
     * Build a new instance of a Warrior from its class and its owner.
     *
     * @param owner        - The owner of the newly created Warrior.
     * @param warriorClass - The class of the warrior to instantiate.
     * @param amount       - The amount of warrior to instantiate.
     * @return The built warriors.
     */
    @Nonnull
    public List<Warrior> buildWarrior(@Nonnull final Player owner,
                                      @Nonnull final Class<? extends Warrior> warriorClass,
                                      int amount) {
        final List<Warrior> w = new ArrayList<>(amount);

        for (int i = 0; i < amount; i++) {
            try {
                w.add(warriorClass.getConstructor(Player.class).newInstance(owner));
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ignore) {
            } // Would never occur
        }

        return w;
    }

    /**
     * Add some warriors at the given cell position.
     *
     * @param position - The position of the target cell.
     * @param ws       - The warriors to spawn.
     */
    public void spawn(final int position, @Nonnull final List<Warrior> ws) {
        board.getCell(position).addWarriors(ws);
    }

    /**
     * Move every warriors on the origin cell to the destination cell regardless of their side.
     *
     * @param origin      - The position of the origin cell.
     * @param destination - The position of the destination cell.
     */
    public void move(final int origin, final int destination) {
        board.getCell(destination).addWarriors(board.getCell(origin).popAll());
    }

    /**
     * Clear a cell.
     *
     * @param position - The position of the cell to clear.
     */
    public void clear(final int position) {
        board.getCell(position).getWarriors().clear();
    }

    /**
     * Clear only certain warriors from a cell.
     *
     * @param position         - The position of the cell to clear.
     * @param warriorPredicate - The warriors that satisfy this predicate will be cleared.
     */
    public void clear(final int position, @Nonnull final Predicate<Warrior> warriorPredicate) {
        board.getCell(position).removeWarriors(
                board.getCell(position).getWarriors().stream()
                        .filter(warriorPredicate)
                        .collect(Collectors.toList())
        );
    }
}
