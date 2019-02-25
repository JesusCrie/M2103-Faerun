package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.model.board.BoardCell;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.util.List;

@Deprecated
public class NopFightOutputHandler implements FightOutputHandler {

    @Override
    public void displayLogStart(@Nonnull BoardCell cell) {
        /* no-op */
    }

    @Override
    public void displayLogEnd(@Nonnull BoardCell cell) {
        /* no-op */
    }

    @Override
    public void displayWarriors(@Nonnull List<Warrior> left, @Nonnull List<Warrior> right) {
        /* no-op */
    }

    @Override
    public void displayLogHit(@Nonnull Warrior attacker, @Nonnull Warrior defender, int damage) {
        /* no-op */
    }

    @Override
    public void displayLogDead(@Nonnull Warrior warrior) {
        /* no-op */
    }
}
