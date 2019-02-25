package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.event.FightEvent;
import com.jesus_crie.faerun.event.GoodbyeEvent;
import com.jesus_crie.faerun.event.NewRoundEvent;
import com.jesus_crie.faerun.event.WelcomeEvent;

import javax.annotation.Nonnull;
import java.io.PrintStream;

/**
 * Suppress the output of the events that are dispatched to everyone to avoid duplicates in the console.
 */
public class ConsoleListenerNoDuplicates extends ConsoleListener {

    public ConsoleListenerNoDuplicates() {
        super();
    }

    public ConsoleListenerNoDuplicates(@Nonnull final PrintStream out) {
        super(out);
    }

    @Override
    public void onWelcome(@Nonnull WelcomeEvent e) {
        /* no-op */
    }

    @Override
    public void onNewRound(@Nonnull NewRoundEvent e) {
        /* no-op */
    }

    @Override
    public void onFight(@Nonnull FightEvent e) {
        /* no-op */
    }

    @Override
    public void onGoodbye(@Nonnull GoodbyeEvent e) {
        /* no-op */
    }
}
