package com.jesus_crie.faerun.event;

import javax.annotation.Nonnull;

/**
 * Triggered when the game is over and the program is going to close.
 */
public final class GoodbyeEvent implements Event {

    private static final long serialVersionUID = 3693412677403875493L;

    private final String winner;

    public GoodbyeEvent(@Nonnull final String winner) {
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }
}
