package com.jesus_crie.faerun.gamemode;

import com.jesus_crie.faerun.config.GameState;
import com.jesus_crie.faerun.config.SaveManager;
import com.jesus_crie.faerun.event.EventFactory;
import com.jesus_crie.faerun.io.*;
import com.jesus_crie.faerun.logic.GameLogic;
import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.Side;
import com.jesus_crie.faerun.model.board.BoardSettings;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ConsoleGame implements GameClient {

    public void start() {

        // Create the prompter and listeners
        // Keep the same prompter for both
        final ConsolePrompter prompter = new ConsolePrompter();
        final IOCombiner combiner1 = IOCombiner.from(prompter, new ConsoleListener());
        final IOCombiner combiner2 = IOCombiner.from(prompter, new ConsoleListenerNoDuplicates());

        // Create game logic depending on the saved game state
        final GameLogic game;

        if (SaveManager.isEmpty()) {
            game = createNewGame(prompter, combiner1, combiner2);

        } else if (prompter.onAskResumeGame(EventFactory.buildAskResumeGame())) {
            game = createFromSavedState(combiner1, combiner2);

        } else {
            game = createNewGame(prompter, combiner1, combiner2);
        }

        // Start the game
        game.performFullGame();
    }

    private GameLogic createNewGame(@Nonnull final Prompter prompter,
                                           @Nonnull final IOCombiner combiner1,
                                           @Nonnull final IOCombiner combiner2) {

        // Create the 2 local players
        final Player p1 = new Player(prompter.onAskUsername(EventFactory.buildAskUsernameEvent()), Side.LEFT);
        final Player p2 = new Player(prompter.onAskUsername(EventFactory.buildAskUsernameEvent()), Side.RIGHT);

        // Create player data
        final Map<Player, IOCombiner> gameMeta = new LinkedHashMap<>(2);
        gameMeta.put(p1, combiner1);
        gameMeta.put(p2, combiner2);

        // Ask for the game settings
        final BoardSettings settings = prompter.onAskSettings(EventFactory.buildAskSettingsEvent());

        /* RDY */

        // Create the game
        return new GameLogic(true, gameMeta, settings);
    }

    private GameLogic createFromSavedState(@Nonnull final IOCombiner combiner1, @Nonnull final IOCombiner combiner2) {
        // Load state
        final GameState state = SaveManager.load();

        // Retrieve players from state
        final Player p1 = state.getPlayerLeft();
        final Player p2 = state.getPlayerRight();

        // Build game meta
        final Map<Player, IOCombiner> gameMeta = new LinkedHashMap<>(2);
        gameMeta.put(p1, combiner1);
        gameMeta.put(p2, combiner2);

        return new GameLogic(true, gameMeta, state.getBoard(), state.getRound());
    }
}
