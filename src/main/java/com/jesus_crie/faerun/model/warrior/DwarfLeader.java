package com.jesus_crie.faerun.model.warrior;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;

/**
 * A dwarf leader, take half less damage than a dwarf.
 */
public class DwarfLeader extends Dwarf {

    private static final long serialVersionUID = 5757681631927269472L;

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
