package com.jesus_crie.faerun.model.warrior;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;

/**
 * A dwarf, take half less damages.
 */
public class Dwarf extends Warrior {

    private static final long serialVersionUID = 4350484448144554502L;

    public Dwarf(@Nonnull final Player player) {
        super(player);
    }

    @Override
    public int getCostFactor() {
        return 1;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage / 2);
    }
}
