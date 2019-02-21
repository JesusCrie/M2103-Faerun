package com.jesus_crie.faerun.model.warrior;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;

public class DwarfLeader extends Dwarf {

    public DwarfLeader(@Nonnull final Player player) {
        super(player);
    }

    @Override
    public int getCostFactor() {
        return 3;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage / 2);
    }
}
