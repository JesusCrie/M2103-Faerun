package com.jesus_crie.faerun;

import com.jesus_crie.faerun.event.EventFactory;
import com.jesus_crie.faerun.io.*;
import com.jesus_crie.faerun.logic.GameLogic;
import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.Side;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.network.FaerunProtocol;

import java.util.HashMap;
import java.util.Map;

public class NetworkServerTest {

    public static void main(String[] args) {
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

        // Setup the protocol
        try (final FaerunProtocol.ProtocolServer protocol = FaerunProtocol.asServer()) {

            // Dispatch automatic port
            localCombiner.dispatch(EventFactory.buildServerReadyEvent(protocol.getSelectedPort()));

            // Wait for remote player
            final Player remotePlayer = new Player(protocol.waitClientAndSetup(), Side.RIGHT);
            final IOCombiner remoteCombiner = IOCombiner.from(
                    new NetPrompter(protocol),
                    new NetListener(protocol)
            );

            // Build game data
            final Map<Player, IOCombiner> gameData = new HashMap<>();
            gameData.put(localPlayer, localCombiner);
            gameData.put(remotePlayer, remoteCombiner);

            /* RDY */

            // Play the game
            final GameLogic logic = new GameLogic(gameData, settings);
            logic.performFullGame();

            // Teardown
            protocol.teardown();

            // Auto close protocol
        }
    }
}
