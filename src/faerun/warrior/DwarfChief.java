package faerun.warrior;

public class DwarfChief extends Dwarf {

    @Override
    public int getCost() {
        return BASE_COST * 3;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage / 2);
    }
}
