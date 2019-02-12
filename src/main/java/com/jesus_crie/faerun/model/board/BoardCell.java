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

    public int countAllies(@Nonnull final Player owner) {
        return (int) warriors.stream()
                .filter(w -> w.getOwner().equals(owner))
                .count();
    }

    public int countEnnemis(@Nonnull final Player owner) {
        return warriors.size() - countAllies(owner);
    }
}
