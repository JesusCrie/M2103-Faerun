package faerun.warrior;

public class Dwarf extends Warrior {

    @Override
    public int getCost() {
        return BASE_COST;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage / 2);
    }
}
