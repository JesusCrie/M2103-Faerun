package com.jesus_crie.faerun.gamemode;

import com.jesus_crie.faerun.event.AskEvent;
import com.jesus_crie.faerun.event.Event;
import com.jesus_crie.faerun.event.EventFactory;
import com.jesus_crie.faerun.event.TeardownEvent;
import com.jesus_crie.faerun.io.ConsoleListener;
import com.jesus_crie.faerun.io.ConsolePrompter;
import com.jesus_crie.faerun.io.IOCombiner;
import com.jesus_crie.faerun.network.FaerunProtocol;
import com.jesus_crie.faerun.utils.Pair;

import java.io.Serializable;

public final class NetworkClient implements GameClient {

    @SuppressWarnings("unchecked")
    public void start() {
        // Create local I/O
        final IOCombiner localCombiner = IOCombiner.from(
                new ConsolePrompter(),
                new ConsoleListener()
        );

        final String localUsername = localCombiner.asPrompter().onAskUsername(EventFactory.buildAskUsernameEvent());

        // Ask the server address
        final Pair<String, Integer> address = localCombiner.asPrompter().onAskRemoteAddress(EventFactory.buildAskRemoteAddress());

        // Setup the protocol
        try (final FaerunProtocol.ProtocolClient protocol = FaerunProtocol.asClient(address.getLeft(), address.getRight())) {
            protocol.setup(localUsername);

            // Add emergency hook, send teardown on force close
            final Thread emergencyHook = new Thread(protocol::teardown);
            Runtime.getRuntime().addShutdownHook(emergencyHook);

            // Wait for an event until the TeardownEvent
            Event event;
            while (!((event = protocol.waitEvent()) instanceof TeardownEvent)) {

                // Process the event
                if (event instanceof AskEvent) {
                    // Dispatch to prompter and send response
                    final Serializable obj = localCombiner.asPrompter().onAsk((AskEvent) event);
                    protocol.sendResponsePayload(obj);
                } else {
                    // Dispatch to listener
                    localCombiner.asListener().onEvent(event);
                }
            }

            System.out.println("Teardown event received, closing...");

            // Remove emergency hook
            Runtime.getRuntime().removeShutdownHook(emergencyHook);

            // Auto close protocol
        }
    }
}
