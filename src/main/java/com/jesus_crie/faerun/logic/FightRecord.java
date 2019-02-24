package com.jesus_crie.faerun.logic;

import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.io.Serializable;

/**
 * Reports the details of a fight.
 */
public final class FightRecord implements Serializable {

    private static final long serialVersionUID = 4074856688688116992L;

    private final int cellIndex;
    private final Player.Side attackerSide;
    private final Warrior[] attackers;
    private final Warrior[] defenders;
    private final FightEntry[] entries;

    public FightRecord(final int cellIndex,
                       @Nonnull Player.Side attackerSide,
                       @Nonnull final Warrior[] attackers,
                       @Nonnull final Warrior[] defenders,
                       @Nonnull final FightEntry[] entries) {
        this.cellIndex = cellIndex;
        this.attackerSide = attackerSide;
        this.defenders = defenders;
        this.attackers = attackers;
        this.entries = entries;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    @Nonnull
    public Player.Side getAttackerSide() {
        return attackerSide;
    }

    @Nonnull
    public Warrior[] getAttackers() {
        return attackers;
    }

    @Nonnull
    public Warrior[] getDefenders() {
        return defenders;
    }

    @Nonnull
    public Player.Side getDefenderSide() {
        return attackerSide == Player.Side.LEFT ?
                Player.Side.RIGHT : Player.Side.LEFT;
    }

    @Nonnull
    public FightEntry[] getFightEntries() {
        return entries;
    }

    public boolean isAttacker(@Nonnull final Player.Side side) {
        return side == attackerSide;
    }
}
