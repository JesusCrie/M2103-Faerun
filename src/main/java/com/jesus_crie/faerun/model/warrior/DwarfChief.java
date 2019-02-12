package com.jesus_crie.faerun.model.warrior;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;

public class DwarfChief extends Dwarf {

    public DwarfChief(@Nonnull final Player player) {
        super(player);
    }

    @Override
    public int getCost() {
        return 3;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage / 2);
    }
}
