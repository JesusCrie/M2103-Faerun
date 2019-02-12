package com.jesus_crie.faerun.model.warrior;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;

public class ElfChief extends Elf {

    public ElfChief(@Nonnull final Player player) {
        super(player);
        strength *= 2;
    }

    @Override
    public int getCost() {
        return 4;
    }
}
