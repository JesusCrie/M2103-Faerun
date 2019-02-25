package com.jesus_crie.faerun.network;

import com.jesus_crie.faerun.network.internal.ClientNetHandler;
import com.jesus_crie.faerun.network.internal.ServerNetHandler;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

/**
 * Contains the protocol used to communicate between two instances of the game across the network.
 * The game is designed to have only one server and one client.
 * The server can't handle more than one client not matter what.
 * This protocol does not handle any reconnect mechanisms, if the connection is lost, nothing can be done at this level.
 */
public final class FaerunProtocol {

    /**
     * @return A new instance of the server protocol.
     */
    public static ProtocolServer asServer() {
        return new ProtocolServer();
    }

    /**
     * Attempt to connect to the remote server as a client.
     * This method will automatically tries to connect to the server so the server must be running.
     *
     * @param address - The address of the host to connect to.
     * @param port    - The port to connect to.
     * @return A new instance of the client protocol.
     */
    public static ProtocolClient asClient(@Nonnull final InetAddress address, final int port) {
        return new ProtocolClient(address, port);
    }

    /**
     * Protocol to use when running as the server.
     */
    public static final class ProtocolServer implements Closeable {

        private final ServerNetHandler handler;

        public ProtocolServer() {
            try {
                handler = new ServerNetHandler();
            } catch (IOException e) {
                throw new IllegalStateException("Can't open a server socket !", e);
            }
        }

        public int getSelectedPort() {
            return handler.getClient().getPort();
        }

        public void waitClientAndSetup() {
            // Wait socket
            handler.waitForClient();

            // Wait connect

            // Ask username

            // Wait username

            // RDY
        }

        public void dispatchEvent(@Nonnull final Serializable event) {
            // Serialize and write socket
        }

        @Nonnull
        public Object askEvent(@Nonnull final Serializable request) {
            // Serialize and write socket

            // Wait for response

            // Deserialize response
            return null;
        }

        public void teardown() {
            // Send teardown
        }

        @Override
        public void close() {
            // Close socket and streams
        }
    }

    /**
     * Protocol to use when running as the client.
     */
    public static final class ProtocolClient implements Closeable {

        private final ClientNetHandler handler;

        public ProtocolClient(@Nonnull final InetAddress address, final int port) {
            try {
                handler = new ClientNetHandler(address, port);
            } catch (IOException e) {
                throw new IllegalStateException("Can't open a client socket !", e);
            }
        }

        public void setup(@Nonnull final String username) {
            // Send connect

            // Wait ask username

            // Send username
        }

        @Nonnull
        public Object waitEvent() {
            // Read until an event arrive
            return null;
        }

        public void respondAskEvent(@Nonnull final Serializable responseEvent) {
            // Respond to an ask event
        }

        public void teardown() {
            // Send teardown
        }

        @Override
        public void close() {
            // Close socket and streams
        }
    }
}
