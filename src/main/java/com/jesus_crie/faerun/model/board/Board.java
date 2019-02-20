package com.jesus_crie.faerun.model.board;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private final BoardSettings settings;
    private final Map<Player, Castle> castles;
    private final List<BoardCell> cells;

    public Board(@Nonnull final BoardSettings settings,
                 @Nonnull final List<Player> players) {
        this.settings = settings;

        // Init castles
        castles = new HashMap<>();
        for (Player player : players)
            castles.put(player, new Castle(player, settings.getBaseCost(), settings.getInitialResources()));

        // Init cells
        cells = new ArrayList<>();
        for (int i = 0; i < settings.getSize(); i++)
            cells.add(new BoardCell(i));
    }

    @Nonnull
    public BoardSettings getSettings() {
        return settings;
    }

    @Nonnull
    public BoardCell getCell(int position) {
        return cells.get(position);
    }

    @Nonnull
    public Castle getCastle(@Nonnull final Player p) {
        if (!castles.containsKey(p))
            throw new IllegalArgumentException("Incorrect player !");

        return castles.get(p);
    }
}
