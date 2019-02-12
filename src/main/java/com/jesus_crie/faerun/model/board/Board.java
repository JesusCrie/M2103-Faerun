package com.jesus_crie.faerun.model.board;

import com.jesus_crie.faerun.model.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Board {

    private final BoardSettings settings;
    private final Map<Player, Castle> castles;
    private final List<BoardCell> cells;

    public Board(@Nonnull final BoardSettings settings,
                 @Nonnull final Map<Player, Castle> castles) {
        this.settings = settings;
        this.castles = castles;
        cells = new ArrayList<>();

    }

    @Nonnull
    public BoardSettings getSettings() {
        return settings;
    }
}
