package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.event.*;

import javax.annotation.Nonnull;

public interface Listener {

    /**
     * When a event is received, it goes there and is dispatched to the correct handler.
     * @param e - The event that was triggered.
     */
    default void onEvent(@Nonnull final Event e) {
        if (e instanceof WelcomeEvent)
            onWelcome((WelcomeEvent) e);
        else if (e instanceof NewRoundEvent)
            onNewRound((NewRoundEvent) e);
        else if (e instanceof FightEvent)
            onFight((FightEvent) e);
        else if (e instanceof GoodbyeEvent)
            onGoodbye((GoodbyeEvent) e);
        else
            throw new IllegalArgumentException("Unknown event !");
    }

    void onWelcome(@Nonnull final WelcomeEvent e);

    void onNewRound(@Nonnull final NewRoundEvent e);

    void onFight(@Nonnull final FightEvent e);

    void onGoodbye(@Nonnull final GoodbyeEvent e);
}
