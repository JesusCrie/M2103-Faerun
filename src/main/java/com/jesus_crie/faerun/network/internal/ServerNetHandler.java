package com.jesus_crie.faerun.network.internal;

import com.jesus_crie.faerun.event.Event;
import com.jesus_crie.faerun.event.TeardownEvent;
import com.jesus_crie.faerun.network.MalformedPayloadException;
import com.jesus_crie.faerun.network.TeardownException;

import javax.annotation.Nonnull;
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

    @Nonnull
    @Override
    public Event receiveEvent() throws MalformedPayloadException {
        final Event e = super.receiveEvent();
        if (e instanceof TeardownEvent)
            throw new TeardownException();

        return e;
    }
}
