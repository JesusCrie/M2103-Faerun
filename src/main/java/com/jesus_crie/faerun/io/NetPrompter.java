package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.event.*;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.warrior.Warrior;
import com.jesus_crie.faerun.network.FaerunProtocol;
import com.jesus_crie.faerun.utils.Pair;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * Receive the ask events and send them to the client over the network.
 */
public class NetPrompter implements Prompter {

    private final FaerunProtocol.ProtocolServer protocol;

    public NetPrompter(@Nonnull final FaerunProtocol.ProtocolServer protocol) {
        this.protocol = protocol;
    }

    @Nonnull
    @Override
    public Boolean onAskResumeGame(@Nonnull final AskResumeGame e) {
        return protocol.askEvent(e);
    }

    @Nonnull
    @Override
    public String onAskUsername(@Nonnull final AskUsernameEvent e) {
        return protocol.askEvent(e);
    }

    @Nonnull
    @Override
    public BoardSettings onAskSettings(@Nonnull final AskSettingsEvent e) {
        return protocol.askEvent(e);
    }

    @Nonnull
    @Override
    public Map<Class<? extends Warrior>, Integer> onAskQueue(@Nonnull final AskQueueEvent e) {
        return protocol.askEvent(e);
    }

    @Nonnull
    @Override
    public Pair<String, Integer> onAskRemoteAddress(@Nonnull final AskRemoteAddress e) {
        // Well, will never happen
        return Pair.of("127.0.0.1", 0);
    }
}
