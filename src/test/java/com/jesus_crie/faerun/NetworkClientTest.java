package com.jesus_crie.faerun;

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

public class NetworkClientTest {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
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

            // Auto close protocol
        }
    }
}
