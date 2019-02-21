package com.jesus_crie.faerun.network;

import com.jesus_crie.faerun.network.payload.NetPayload;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;

public abstract class NetHandler {

    protected Socket client;
    protected PrintStream toClient;
    protected DataInputStream fromClient;

    protected static final byte TERMINATOR = '\n';

    /**
     * Send a payload under the network to the client socket.
     *
     * @param payload - The payload to send.
     * @return True if the payload has been successfully sent through the socket, otherwise false.
     */
    public abstract boolean sendPayload(@Nonnull final NetPayload payload);

    /**
     * Wait for a full payload to be retrieved from the socket and rebuilt.
     *
     * @param <T> - The type of payload to receive.
     * @return The rebuilt payload.
     */
    @Nonnull
    public abstract NetPayload receivePayload();
}
