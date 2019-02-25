package com.jesus_crie.faerun.event;

import com.jesus_crie.faerun.model.Side;

import javax.annotation.Nonnull;

/**
 * Triggered when a new round starts.
 * Contains the current cells owned by each players and the round number.
 */
public final class NewRoundEvent implements Event {

    private static final long serialVersionUID = 4839576512516627686L;

    /**
     * Round number.
     */
    private final int round;

    /**
     * The name of the active player.
     */
    private final String playerName;

    /**
     * The side of all cells of the board.
     */
    private final Side[] cells;

    public NewRoundEvent(final int round,
                         @Nonnull final String playerName,
                         @Nonnull final Side[] cells) {
        this.round = round;
        this.playerName = playerName;
        this.cells = cells;
    }

    public int getRound() {
        return round;
    }

    @Nonnull
    public String getPlayerName() {
        return playerName;
    }

    @Nonnull
    public Side[] getCells() {
        return cells;
    }
}
