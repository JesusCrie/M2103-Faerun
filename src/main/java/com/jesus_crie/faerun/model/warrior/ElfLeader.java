package com.jesus_crie.faerun.model.warrior;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;

/**
 * An elf leader, have twice as much strength as an elf.
 */
public class ElfLeader extends Elf {

    private static final long serialVersionUID = -8048309369075169846L;

    public ElfLeader(@Nonnull final Player player) {
        super(player);
        strength *= 2;
    }

    @Override
    public int getCostFactor() {
        return 4;
    }
}
