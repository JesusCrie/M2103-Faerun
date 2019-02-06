package faerun.warrior;

public class ElfChief extends Elf {

    public ElfChief() {
        strength *= 2;
    }

    @Override
    public int getCost() {
        return BASE_COST * 4;
    }
}
