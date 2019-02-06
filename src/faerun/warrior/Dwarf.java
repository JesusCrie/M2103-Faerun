package faerun.warrior;

import faerun.game.Player;

public class Dwarf extends Warrior {

    public Dwarf(final Player player) {
        super(player);
    }

    @Override
    public int getCost() {
        return BASE_COST;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage / 2);
    }
}
