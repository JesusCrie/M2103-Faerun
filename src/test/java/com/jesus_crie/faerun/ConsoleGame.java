package com.jesus_crie.faerun;

import com.jesus_crie.faerun.event.EventFactory;
import com.jesus_crie.faerun.io.*;
import com.jesus_crie.faerun.logic.GameLogic;
import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.Side;
import com.jesus_crie.faerun.model.board.BoardSettings;

import java.util.HashMap;
import java.util.Map;

public class ConsoleGame {

    public static void main(String[] args) {
        final ConsolePrompter prompter = new ConsolePrompter();

        final IOCombiner combiner1 = IOCombiner.from(prompter, new ConsoleListener());
        final IOCombiner combiner2 = IOCombiner.from(prompter, new ConsoleListenerNoDuplicates());

        final Map<Player, IOCombiner> gameMeta = new HashMap<>();

        final Player p1 = new Player(prompter.onAskUsername(EventFactory.buildAskUsernameEvent()), Side.LEFT);
        final Player p2 = new Player(prompter.onAskUsername(EventFactory.buildAskUsernameEvent()), Side.RIGHT);

        gameMeta.put(p1, combiner1);
        gameMeta.put(p2, combiner2);

        final BoardSettings settings = prompter.onAskSettings(EventFactory.buildAskSettingsEvent());

        final GameLogic game = new GameLogic(gameMeta, settings);

        // Start dat game
        game.performFullGame();

        System.out.println("Game finished !");
    }
}
