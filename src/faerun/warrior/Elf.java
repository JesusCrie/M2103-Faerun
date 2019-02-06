package faerun.warrior;

import faerun.game.Player;

public class Elf extends Warrior {

    public Elf(final Player player) {
        super(player);
        strength *= 2;
    }

    @Override
    public int getCost() {
        return BASE_COST * 2;
    }
}
