package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.event.*;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.warrior.Warrior;
import com.jesus_crie.faerun.utils.Pair;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Map;

/**
 * Handles the input part of the program, any value that need to be supplied by the user passes through here.
 */
public interface Prompter {

    @SuppressWarnings("unchecked")
    @Nonnull
    default <T extends Serializable> T onAsk(@Nonnull final AskEvent<T> e) {
        if (e instanceof AskUsernameEvent)
            return (T) onAskUsername((AskUsernameEvent) e);
        else if (e instanceof AskSettingsEvent)
            return (T) onAskSettings((AskSettingsEvent) e);
        else if (e instanceof AskQueueEvent)
            return (T) onAskQueue((AskQueueEvent) e);
        else if (e instanceof AskRemoteAddress)
            return (T) onAskRemoteAddress((AskRemoteAddress) e);
        else
            throw new IllegalArgumentException("This event is unknown: " + e);
    }

    @Nonnull
    String onAskUsername(@Nonnull final AskUsernameEvent e);

    @Nonnull
    BoardSettings onAskSettings(@Nonnull final AskSettingsEvent e);

    @Nonnull
    Map<Class<? extends Warrior>, Integer> onAskQueue(@Nonnull final AskQueueEvent e);

    @Nonnull
    Pair<String, Integer> onAskRemoteAddress(@Nonnull final AskRemoteAddress e);
}
