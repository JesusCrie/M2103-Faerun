package com.jesus_crie.faerun;

import com.jesus_crie.faerun.io.ConsoleHandler;
import com.jesus_crie.faerun.io.InputHandler;
import com.jesus_crie.faerun.io.NopFightOutputHandler;
import com.jesus_crie.faerun.io.OutputHandler;
import com.jesus_crie.faerun.logic.GameLogic;
import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.Side;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class ConsoleGame {

    public static void main(String[] args) {
        final ConsoleHandler handler1 = new ConsoleHandler(new ConsoleHandler.ConsoleFightOutputHandler());
        final ConsoleHandler handler2 = new ConsoleHandler(new NopFightOutputHandler());

        final Map<Player, Pair<InputHandler, OutputHandler>> gameMeta = new HashMap<>();

        handler1.displayPromptUsername();
        final Player p1 = new Player(handler1.provideUsername(), Side.LEFT);
        handler2.displayPromptUsername();
        final Player p2 = new Player(handler2.provideUsername(), Side.RIGHT);

        gameMeta.put(p1, Pair.of(handler1, handler1));
        gameMeta.put(p2, Pair.of(handler2, handler2));

        handler1.displayPromptSettings();
        final BoardSettings settings = handler1.provideSettings();

        final GameLogic game = new GameLogic(gameMeta, settings);

        // Start dat game
        game.performFullGame();

        System.out.println("Game finished !");
    }
}
