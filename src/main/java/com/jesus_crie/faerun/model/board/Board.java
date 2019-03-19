package com.jesus_crie.faerun.model.board;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represent the board where the game take place.
 * Handle all most of the "static" part of the game (not the logic).
 * Contains the castles and cells.
 */
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

    public Board(@Nonnull final BoardSettings settings,
                 @Nonnull final List<Castle> castles,
                 @Nonnull final List<BoardCell> cells) {
        this.settings = settings;
        this.castles = new HashMap<>();
        for (Castle castle : castles)
            this.castles.put(castle.getOwner(), castle);

        this.cells = new ArrayList<>();
        this.cells.addAll(cells);
    }

    /**
     * @return The settings associated with this board.
     */
    @Nonnull
    public BoardSettings getSettings() {
        return settings;
    }

    /**
     * Get the cell at the given position
     *
     * @param position - The position of the cell to get.
     * @return The corresponding cell on the board.
     */
    @Nonnull
    public BoardCell getCell(final int position) {
        return cells.get(position);
    }

    /**
     * @param p - The owner of the castle.
     * @return The castle of the given player.
     */
    @Nonnull
    public Castle getCastle(@Nonnull final Player p) {
        if (!castles.containsKey(p))
            throw new IllegalArgumentException("Incorrect player !");

        return castles.get(p);
    }
}
