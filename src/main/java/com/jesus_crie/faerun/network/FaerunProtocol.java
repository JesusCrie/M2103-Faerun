package com.jesus_crie.faerun.network;

import com.jesus_crie.faerun.network.internal.ClientNetHandler;
import com.jesus_crie.faerun.network.internal.ServerNetHandler;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

public class FaerunProtocol {

    public static class ProtocolServer implements Closeable {

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

    public static class ProtocolClient implements Closeable {

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
