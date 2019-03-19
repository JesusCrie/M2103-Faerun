package com.jesus_crie.faerun.gamemode;

import com.jesus_crie.faerun.event.EventFactory;
import com.jesus_crie.faerun.io.*;
import com.jesus_crie.faerun.logic.GameLogic;
import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.Side;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.network.FaerunProtocol;
import com.jesus_crie.faerun.network.TeardownException;

import java.util.LinkedHashMap;
import java.util.Map;

public final class NetworkServer implements GameClient {

    public void start() {
        // Create local I/O
        final IOCombiner localCombiner = IOCombiner.from(
                new ConsolePrompter(),
                new ConsoleListener()
        );

        // Build local player (left)
        final Player localPlayer = new Player(
                localCombiner.asPrompter().onAskUsername(EventFactory.buildAskUsernameEvent()), Side.LEFT
        );

        // Ask the game settings
        final BoardSettings settings = localCombiner.asPrompter().onAskSettings(EventFactory.buildAskSettingsEvent());

        // Emergency hook, in case the program is closed
        Thread emergencyHook = null;

        // Setup the protocol
        try (final FaerunProtocol.ProtocolServer protocol = FaerunProtocol.asServer()) {

            // Add emergency hook
            emergencyHook = new Thread(protocol::teardown);
            Runtime.getRuntime().addShutdownHook(emergencyHook);

            // Dispatch automatic port
            localCombiner.dispatch(EventFactory.buildServerReadyEvent(protocol.getSelectedPort()));

            // Wait for remote player
            final Player remotePlayer = new Player(protocol.waitClientAndSetup(), Side.RIGHT);
            final IOCombiner remoteCombiner = IOCombiner.from(
                    new NetPrompter(protocol),
                    new NetListener(protocol)
            );

            // Build game data
            final Map<Player, IOCombiner> gameData = new LinkedHashMap<>(2);
            gameData.put(localPlayer, localCombiner);
            gameData.put(remotePlayer, remoteCombiner);

            /* RDY */

            // Play the game
            final GameLogic logic = new GameLogic(false, gameData, settings);
            logic.performFullGame();

            Runtime.getRuntime().removeShutdownHook(emergencyHook);

            // Teardown
            protocol.teardown();

            // Auto close protocol

        } catch (TeardownException e) {
            if (emergencyHook != null)
                Runtime.getRuntime().removeShutdownHook(emergencyHook);

            // Mean the client has forced the game to end.
            System.err.println("Fatal: The client has requested an unexpected teardown !");
        }
    }
}
