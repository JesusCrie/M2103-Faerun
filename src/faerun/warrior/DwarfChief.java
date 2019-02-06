package faerun.warrior;

import faerun.game.Player;

public class DwarfChief extends Dwarf {

    public DwarfChief(final Player player) {
        super(player);
    }

    @Override
    public int getCost() {
        return BASE_COST * 3;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage / 2);
    }
}
