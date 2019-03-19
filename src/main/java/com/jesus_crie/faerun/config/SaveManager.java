package com.jesus_crie.faerun.config;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.json.JsonFormat;
import com.jesus_crie.faerun.logic.GameLogic;
import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.Side;
import com.jesus_crie.faerun.model.board.Board;

import javax.annotation.Nonnull;

public class SaveManager {

    private static final FileConfig CACHE = FileConfig.of("./save.json", JsonFormat.fancyInstance());

    private static Player cachedPlayerLeft;
    private static Player cachedPlayerRight;

    static {
        CACHE.load();
    }

    public static boolean isEmpty() {
        return CACHE.isEmpty();
    }

    public static void save(@Nonnull final Board board,
                            final int round,
                            @Nonnull final Player left,
                            @Nonnull final Player right) {
        final GameState state = new GameState(board, round, left, right);
        state.writeTo(CACHE);
        CACHE.save();
    }

    public static void save(@Nonnull final GameLogic logic) {
        if (cachedPlayerLeft == null)
            extractPlayers(logic);

        save(logic.getBoardLogic().getBoard(), logic.getRoundNumber(), cachedPlayerLeft, cachedPlayerRight);
    }

    private static void extractPlayers(@Nonnull final GameLogic logic) {
        final Player[] players = logic.getPlayers().keySet().toArray(new Player[2]);

        if (players[0].getSide() == Side.LEFT) {
            cachedPlayerLeft = players[0];
            cachedPlayerRight = players[1];
        } else {
            cachedPlayerLeft = players[1];
            cachedPlayerRight = players[0];
        }
    }

    public static GameState load() {
        // Already loaded
        return new GameState(CACHE);
    }
}
