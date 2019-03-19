package com.jesus_crie.faerun.logic;

import com.jesus_crie.faerun.model.Side;

import javax.annotation.Nonnull;

public class DivineHitException extends Exception {

    private static final long serialVersionUID = -8381159263964000978L;
    private final Side side;
    private final int attackerIndex;
    private final int damage;

    public DivineHitException(@Nonnull final Side side, final int attackerIndex, final int damage) {
        this.side = side;
        this.attackerIndex = attackerIndex;
        this.damage = damage;
    }

    @Nonnull
    public Side getSide() {
        return side;
    }

    public int getAttackerIndex() {
        return attackerIndex;
    }

    public int getDamage() {
        return damage;
    }
}
