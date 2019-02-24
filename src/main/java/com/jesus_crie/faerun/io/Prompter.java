package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.event.AskBoardSettingsEvent;
import com.jesus_crie.faerun.event.AskGameEvent;
import com.jesus_crie.faerun.event.AskQueueEvent;
import com.jesus_crie.faerun.event.AskUsernameEvent;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.util.Map;

public interface Prompter {

    @SuppressWarnings("unchecked")
    @Nonnull
    default <T> T onAsk(@Nonnull final AskGameEvent<T> e) {
        if (e instanceof AskUsernameEvent)
            return (T) onAskUsername((AskUsernameEvent) e);
        else if (e instanceof AskBoardSettingsEvent)
            return (T) onAskBoardSettings((AskBoardSettingsEvent) e);
        else if (e instanceof AskQueueEvent)
            return (T) onAskQueue((AskQueueEvent) e);
        else
            throw new IllegalArgumentException("This event is unknown: " + e);
    }

    @Nonnull
    String onAskUsername(@Nonnull final AskUsernameEvent e);

    @Nonnull
    BoardSettings onAskBoardSettings(@Nonnull final AskBoardSettingsEvent e);

    @Nonnull
    Map<Class<? extends Warrior>, Integer> onAskQueue(@Nonnull final AskQueueEvent e);
}
