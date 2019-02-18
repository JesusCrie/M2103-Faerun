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

public class BoardLogic {

    private final Board board;

    public BoardLogic(@Nonnull final BoardSettings settings,
                      @Nonnull final List<Player> players) {
        board = new Board(settings, players);
    }

    @Nonnull
    public Board getBoard() {
        return board;
    }

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

    public void spawn(final int position, @Nonnull final Warrior... ws) {
        board.getCell(position).addWarriors(ws);
    }

    public void move(final int origin, final int destination) {
        board.getCell(destination).addWarriors(board.getCell(origin).popAll());
    }

    public void clear(final int position) {
        board.getCell(position).getWarriors().clear();
    }

    public void clear(final int position, @Nonnull final Predicate<Warrior> warriorPredicate) {
        board.getCell(position).removeWarriors(
                board.getCell(position).getWarriors().stream()
                        .filter(warriorPredicate)
                        .toArray(Warrior[]::new)
        );
    }

    public void slaveBoard(@Nonnull final Consumer<Board> action) {
        action.accept(board);
    }
}
