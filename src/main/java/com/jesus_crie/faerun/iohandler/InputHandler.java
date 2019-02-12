package com.jesus_crie.faerun.iohandler;

import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.warrior.Warrior;

import java.util.Map;

public interface InputHandler {

    Player[] providePlayers();

    BoardSettings provideSettings();

    Map<Class<? extends Warrior>, Integer> provideQueue();
}
