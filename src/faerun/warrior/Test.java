package faerun.warrior;

import java.util.Random;

public class Test {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        fightDwarfElf();
        fightDwarfChiefElf();
    }

    private static void fightDwarfElf() {
        final Dwarf dwarf = new Dwarf();
        final Elf elf = new Elf();

        fight(dwarf, elf);
    }

    private static void fightDwarfChiefElf() {
        final DwarfChief dwarf = new DwarfChief();
        final Elf elf = new Elf();

        fight(dwarf, elf);
    }

    private static void fight(final Warrior w1, final Warrior w2) {
        while (w1.isAlive() && w2.isAlive()) {
            if (RANDOM.nextBoolean())
                w1.attack(w2);
            else w2.attack(w1);

            System.out.println(w1 + " VS " + w2);
        }

        if (w1.isAlive())
            System.out.println("W1 won ! (" + w1 + ")");
        else
            System.out.println("W2 won ! (" + w2 + ")");
    }
}
