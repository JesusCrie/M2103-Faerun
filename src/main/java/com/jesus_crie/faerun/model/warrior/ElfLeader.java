package com.jesus_crie.faerun.model.warrior;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;

public class ElfLeader extends Elf {

    public ElfLeader(@Nonnull final Player player) {
        super(player);
        strength *= 2;
    }

    @Override
    public int getCost() {
        return 4;
    }
}
