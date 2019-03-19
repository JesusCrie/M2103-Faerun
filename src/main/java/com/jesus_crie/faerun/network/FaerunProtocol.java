package com.jesus_crie.faerun.network;

import com.jesus_crie.faerun.event.*;
import com.jesus_crie.faerun.network.internal.ClientNetHandler;
import com.jesus_crie.faerun.network.internal.ServerNetHandler;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;

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
    public static ProtocolClient asClient(@Nonnull final String address, final int port) {
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
            return handler.getPort();
        }

        /**
         * Perform the initial setup of the connection setup.
         * Wait for a client to connect and ask for its username and returns it.
         *
         * @return The username of the remote client.
         * @throws IllegalStateException If the received event is not a connect event.
         */
        @Nonnull
        public String waitClientAndSetup() {
            // Wait socket
            if (!handler.waitForClient()) {
                throw new IllegalStateException("Server socket crashed while waiting for a client !");
            }

            // Wait connect
            try {
                handler.receiveEvent();
            } catch (TeardownException e) {
                throw new IllegalStateException("Protocol error: unexpected teardown event during setup !");
            } catch (ClassCastException e) {
                throw new IllegalStateException("Protocol error: not a ConnectEvent !", e);
            }

            // Ask username & RDY
            return askEvent(EventFactory.buildAskUsernameEvent());
        }

        /**
         * Used to dispatch an event to the client.
         *
         * @param event - The event to dispatch.
         */
        public void dispatchEvent(@Nonnull final Event event) {
            if (!handler.sendEvent(event))
                throw new IllegalStateException("Failed to send event through the socket: " + event);
        }

        /**
         * Used to dispatch an event that need a response.
         *
         * @param request - The request event.
         * @param <T>     - The type of object to wait for.
         * @return The object read from the remote.
         * @throws IllegalStateException If the read object is not what we expect.
         */
        @SuppressWarnings("unchecked")
        @Nonnull
        public <T extends Serializable> T askEvent(@Nonnull final AskEvent<T> request) {
            // Serialize and write socket
            dispatchEvent(request);

            // Wait for response and return it.
            try {
                final Serializable obj = handler.receivePayload();

                // If we have that, the client has most likely given up
                if (obj instanceof TeardownEvent)
                    throw new TeardownException();

                return (T) obj;
            } catch (ClassCastException e) {
                throw new IllegalStateException("Protocol Error: the retrieved payload is of the wrong type !", e);
            }
        }

        /**
         * Notify the client that the connection will be closed.
         */
        public void teardown() {
            // Send teardown
            dispatchEvent(EventFactory.buildTeardownEvent());
        }

        @Override
        public void close() {
            // Close socket and streams
            handler.close();
        }
    }

    /**
     * Protocol to use when running as the client.
     */
    public static final class ProtocolClient implements Closeable {

        private final ClientNetHandler handler;

        public ProtocolClient(@Nonnull final String address, final int port) {
            try {
                handler = new ClientNetHandler(address, port);
            } catch (IOException e) {
                throw new IllegalStateException("Can't open a client socket !", e);
            }
        }

        /**
         * Setup the connection with the server.
         *
         * @param username - The username to use.
         */
        public void setup(@Nonnull final String username) {
            // Send connect
            dispatchEvent(EventFactory.buildConnectEvent());

            // Wait ask username
            final AskUsernameEvent event = (AskUsernameEvent) waitEvent();

            // Send username
            sendResponsePayload(username);

            // RDY
        }

        /**
         * Block until an event is read from the socket.
         *
         * @return The event that was read.
         */
        @Nonnull
        public Event waitEvent() {
            // Read until an event arrive
            return handler.receiveEvent();
        }

        /**
         * Used to dispatch an event to the server.
         *
         * @param event - The event to dispatch.
         */
        public void dispatchEvent(@Nonnull final Event event) {
            if (!handler.sendEvent(event))
                throw new IllegalStateException("Failed to send event through the socket: " + event);
        }

        /**
         * Send back an object in response to an AskEvent.
         *
         * @param payload - The response payload.
         */
        public void sendResponsePayload(@Nonnull final Serializable payload) {
            if (!handler.sendPayload(payload))
                throw new IllegalStateException("Failed to send response payload through the socket !");
        }

        /**
         * Notify the server that the connection need to be closed.
         */
        public void teardown() {
            // Send teardown
            dispatchEvent(EventFactory.buildTeardownEvent());
        }

        @Override
        public void close() {
            // Close socket and streams
            handler.close();
        }
    }
}
