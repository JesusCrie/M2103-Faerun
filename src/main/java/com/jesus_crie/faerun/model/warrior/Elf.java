package com.jesus_crie.faerun.model.warrior;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;

public class Elf extends Warrior {

    public Elf(@Nonnull final Player player) {
        super(player);
        strength *= 2;
    }

    @Override
    public int getCost() {
        return 2;
    }
}
