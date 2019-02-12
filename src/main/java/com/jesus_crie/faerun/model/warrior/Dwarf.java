package com.jesus_crie.faerun.model.warrior;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;

public class Dwarf extends Warrior {

    public Dwarf(@Nonnull final Player player) {
        super(player);
    }

    @Override
    public int getCost() {
        return 1;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage / 2);
    }
}
