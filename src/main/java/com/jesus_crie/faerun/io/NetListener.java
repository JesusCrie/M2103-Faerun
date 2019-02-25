package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.event.*;
import com.jesus_crie.faerun.network.FaerunProtocol;

import javax.annotation.Nonnull;

/**
 * Receive the events and send them to the client over the network.
 */
public class NetListener implements Listener {

    private final FaerunProtocol.ProtocolServer protocol;

    public NetListener(@Nonnull final FaerunProtocol.ProtocolServer protocol) {
        this.protocol = protocol;
    }

    @Override
    public void onWelcome(@Nonnull final WelcomeEvent e) {
        protocol.dispatchEvent(e);
    }

    @Override
    public void onServerReady(@Nonnull final ServerReadyEvent e) {
        // Will never happen
        /* no-op */
    }

    @Override
    public void onNewRound(@Nonnull final NewRoundEvent e) {
        protocol.dispatchEvent(e);
    }

    @Override
    public void onFight(@Nonnull final FightEvent e) {
        protocol.dispatchEvent(e);
    }

    @Override
    public void onGoodbye(@Nonnull final GoodbyeEvent e) {
        protocol.dispatchEvent(e);
    }
}
