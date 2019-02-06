package faerun.warrior;

import faerun.game.Player;

public class ElfChief extends Elf {

    public ElfChief(final Player player) {
        super(player);
        strength *= 2;
    }

    @Override
    public int getCost() {
        return BASE_COST * 4;
    }
}
