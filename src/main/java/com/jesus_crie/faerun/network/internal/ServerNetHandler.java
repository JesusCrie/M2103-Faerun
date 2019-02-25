package com.jesus_crie.faerun.network.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

/**
 * Specialization which can wait for a client socket to connect to it.
 */
public class ServerNetHandler extends NetHandler {

    private final ServerSocket server;

    public ServerNetHandler() throws IOException {
        server = new ServerSocket(0);
    }

    /**
     * @return The port of the server socket.
     */
    public int getPort() {
        return server.getLocalPort();
    }

    /**
     * Block until a client socket attempts to connect to it.
     *
     * @return True if a client has successfully connected to it, otherwise false.
     */
    public boolean waitForClient() {
        try {
            client = server.accept();

            toClient = new ObjectOutputStream(client.getOutputStream());
            fromClient = new ObjectInputStream(client.getInputStream());

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
