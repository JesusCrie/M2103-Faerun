package com.jesus_crie.faerun.logic;

import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.warrior.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.*;

public class GameLogicTest {

    @Test
    public void test_fightLogic_attack() {
        final Player p = new Player("lucas", Player.Side.LEFT);
        final Player p2 = new Player("lucas2", Player.Side.RIGHT);

        final Queue<Warrior> allies = new LinkedList<>(Arrays.asList(
                new Dwarf(p),
                new Elf(p)
        ));

        final Queue<Warrior> enemies = new LinkedList<>(Arrays.asList(
                new DwarfChief(p2),
                new ElfChief(p2)
        ));


    }

}