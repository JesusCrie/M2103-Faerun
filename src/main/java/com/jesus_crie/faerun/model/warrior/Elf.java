package com.jesus_crie.faerun.model.warrior;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;

/**
 * An elf, have twice as much strength.
 */
public class Elf extends Warrior {

    private static final long serialVersionUID = -5056854045988756215L;

    public Elf(@Nonnull final Player player) {
        super(player);
        strength *= 2;
    }

    @Override
    public int getCostFactor() {
        return 2;
    }
}
