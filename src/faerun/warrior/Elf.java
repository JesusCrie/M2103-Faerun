package faerun.warrior;

public class Elf extends Warrior {

    public Elf() {
        strength *= 2;
    }

    @Override
    public int getCost() {
        return BASE_COST * 2;
    }
}
