package com.jesus_crie.faerun.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Player {

    public enum Side {
        LEFT, RIGHT
    }

    private final String pseudo;
    private final Side side;

    /**
     * @param pseudo - The username of the player.
     */
    public Player(@Nonnull final String pseudo,
                  @Nonnull final Side side) {
        this.pseudo = pseudo;
        this.side = side;
    }

    /**
     * @return The username of the player.
     */
    @Nonnull
    public String getPseudo() {
        return pseudo;
    }

    /**
     * @return The side of the player on the board.
     */
    @Nonnull
    public Side getSide() {
        return side;
    }

    /**
     * 2 players are equals if they have the same username.
     *
     * @param obj - The object to compare with.
     * @return True if the 2 objects are the same player, otherwise false.
     */
    @Override
    public boolean equals(@Nullable final Object obj) {
        return obj instanceof Player && pseudo.equals(((Player) obj).pseudo);
    }
}
