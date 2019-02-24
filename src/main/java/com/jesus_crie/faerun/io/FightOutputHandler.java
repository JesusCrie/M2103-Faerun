package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.model.board.BoardCell;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.util.List;

public interface FightOutputHandler {

    void displayLogStart(@Nonnull final BoardCell cell);

    void displayLogEnd(@Nonnull final BoardCell cell);

    void displayWarriors(@Nonnull final List<Warrior> left, @Nonnull final List<Warrior> right);

    void displayLogHit(@Nonnull final Warrior attacker, @Nonnull final Warrior defender, final int damage);

    void displayLogDead(@Nonnull final Warrior warrior);
}
