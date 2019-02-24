package com.jesus_crie.faerun.network.internal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

public class ServerNetHandler extends NetHandler {

    private final ServerSocket server;

    public ServerNetHandler() throws IOException {
        server = new ServerSocket(0);
    }

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
