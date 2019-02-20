package com.jesus_crie.faerun.iohandler;

import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.warrior.Warrior;

import java.util.Map;

public interface InputHandler {

    boolean provideGamemode();

    String provideUsername();

    BoardSettings provideSettings();

    Map<Class<? extends Warrior>, Integer> provideQueue();
}
