package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.event.*;

import javax.annotation.Nonnull;

public interface Listener {

    /**
     * When a event is received, it goes there and is dispatched to the correct handler.
     * @param e - The event that was triggered.
     */
    default void onEvent(@Nonnull final GameEvent e) {
        if (e instanceof WelcomeEvent)
            onWelcome((WelcomeEvent) e);
        else if (e instanceof SettingsReadyEvent)
            onSettingsReady((SettingsReadyEvent) e);
        else if (e instanceof NewRoundEvent)
            onNewRound((NewRoundEvent) e);
        else if (e instanceof FightEvent)
            onFight((FightEvent) e);
        else
            throw new IllegalArgumentException("Unknown event !");
    }

    default void onWelcome(@Nonnull final WelcomeEvent e) {}

    default void onSettingsReady(@Nonnull final SettingsReadyEvent e) {}

    default void onNewRound(@Nonnull final NewRoundEvent e) {}

    default void onFight(@Nonnull final FightEvent e) {}
}
