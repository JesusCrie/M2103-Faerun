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
        // Create the prompter and listeners
        // Keep the same prompter for both
        final ConsolePrompter prompter = new ConsolePrompter();
        final IOCombiner combiner1 = IOCombiner.from(prompter, new ConsoleListener());
        final IOCombiner combiner2 = IOCombiner.from(prompter, new ConsoleListenerNoDuplicates());

        // Create the 2 local players
        final Player p1 = new Player(prompter.onAskUsername(EventFactory.buildAskUsernameEvent()), Side.LEFT);
        final Player p2 = new Player(prompter.onAskUsername(EventFactory.buildAskUsernameEvent()), Side.RIGHT);

        // Create player data
        final Map<Player, IOCombiner> gameMeta = new HashMap<>();
        gameMeta.put(p1, combiner1);
        gameMeta.put(p2, combiner2);

        // Ask for the game settings
        final BoardSettings settings = prompter.onAskSettings(EventFactory.buildAskSettingsEvent());

        /* RDY */

        // Create the game
        final GameLogic game = new GameLogic(gameMeta, settings);

        // Start the game
        game.performFullGame();
    }
}
