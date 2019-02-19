package com.jesus_crie.faerun;

import com.jesus_crie.faerun.iohandler.ConsoleHandler;
import com.jesus_crie.faerun.iohandler.InputHandler;
import com.jesus_crie.faerun.iohandler.OutputHandler;
import com.jesus_crie.faerun.logic.GameLogic;
import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class MainTest {

    public static void main(String[] args) {
        final ConsoleHandler handler = new ConsoleHandler();

        final Map<Player, Pair<InputHandler, OutputHandler>> gameMeta = new HashMap<>();
        for (Player player : handler.providePlayers()) {
            gameMeta.put(player, Pair.of(handler, handler));
        }

        final BoardSettings settings = handler.provideSettings();

        final GameLogic game = new GameLogic(gameMeta, settings);

        // Start dat game
        game.performFullGame();

        System.out.println("Game finished !");
    }
}
