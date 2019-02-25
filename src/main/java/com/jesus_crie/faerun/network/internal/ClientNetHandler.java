package com.jesus_crie.faerun.network.internal;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Specialization which setup the client socket to a given remote address.
 */
public class ClientNetHandler extends NetHandler {

    public ClientNetHandler(@Nonnull final String address, final int port) throws IOException {
        client = new Socket(address, port);

        toClient = new ObjectOutputStream(client.getOutputStream());
        fromClient = new ObjectInputStream(client.getInputStream());
    }
}
