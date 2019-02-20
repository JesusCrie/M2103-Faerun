package com.jesus_crie.faerun;

import com.jesus_crie.faerun.iohandler.ConsoleHandler;
import com.jesus_crie.faerun.iohandler.InputHandler;
import com.jesus_crie.faerun.iohandler.NopFightOutputHandler;
import com.jesus_crie.faerun.iohandler.OutputHandler;
import com.jesus_crie.faerun.logic.GameLogic;
import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class MainTest {

    public static void main(String[] args) {
        final ConsoleHandler handler1 = new ConsoleHandler(new ConsoleHandler.ConsoleFightOutputHandler());
        final ConsoleHandler handler2 = new ConsoleHandler(new NopFightOutputHandler());

        final Map<Player, Pair<InputHandler, OutputHandler>> gameMeta = new HashMap<>();

        final Player[] players = handler1.providePlayers();
        gameMeta.put(players[0], Pair.of(handler1, handler1));
        gameMeta.put(players[1], Pair.of(handler2, handler2));

        final BoardSettings settings = handler1.provideSettings();

        final GameLogic game = new GameLogic(gameMeta, settings);

        // Start dat game
        game.performFullGame();

        System.out.println("Game finished !");
    }
}
