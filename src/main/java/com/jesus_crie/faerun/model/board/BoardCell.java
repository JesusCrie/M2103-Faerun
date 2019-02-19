package com.jesus_crie.faerun.model.board;

import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardCell {

    private final int position;
    private final List<Warrior> warriors = new ArrayList<>();

    public BoardCell(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public List<Warrior> getWarriors() {
        return warriors;
    }

    public Player.Side getSide() {
        if (getWarriors().size() == 0)
            return Player.Side.EMPTY;
        else if (getWarriors().size() == countAllies(Player.Side.RIGHT))
            return Player.Side.RIGHT;
        else if (getWarriors().size() == countAllies(Player.Side.LEFT))
            return Player.Side.LEFT;
        else
            return Player.Side.BOTH;
    }

    public void addWarriors(@Nonnull final Warrior... ws) {
        addWarriors(Arrays.asList(ws));
    }

    public void addWarriors(@Nonnull final List<Warrior> ws) {
        warriors.addAll(ws);
    }

    public void removeWarriors(@Nonnull final Warrior... ws) {
        warriors.removeAll(Arrays.asList(ws));
    }

    @Nonnull
    public List<Warrior> popAll() {
        final List<Warrior> copy = new ArrayList<>(warriors);
        warriors.clear();

        return copy;
    }

    public int countAll() {
        return warriors.size();
    }

    public int countAllies(@Nonnull final Player.Side owner) {
        return (int) warriors.stream()
                .filter(w -> w.getOwner().getSide() == owner)
                .count();
    }

    public int countEnemies(@Nonnull final Player.Side owner) {
        return (int) warriors.stream()
                .filter(w -> w.getOwner().getSide() != owner)
                .count();
    }
}
