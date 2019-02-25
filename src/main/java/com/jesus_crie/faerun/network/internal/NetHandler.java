package com.jesus_crie.faerun.network.internal;

import com.jesus_crie.faerun.event.Event;
import com.jesus_crie.faerun.network.MalformedPayloadException;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.Socket;

public abstract class NetHandler {

    protected Socket client;
    protected ObjectOutputStream toClient;
    protected ObjectInputStream fromClient;

    /**
     * @return The current client socket.
     */
    @Nonnull
    public Socket getClient() {
        return client;
    }

    /**
     * Send a payload under the network to the client socket.
     *
     * @param payload - The payload to send.
     * @return True if the payload has been successfully sent through the socket, otherwise false.
     */
    public boolean sendPayload(@Nonnull final Event payload) {
        checkOrThrow();

        try {
            toClient.writeObject(payload);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Wait for a full payload to be retrieved from the socket and rebuilt.
     *
     * @return The rebuilt payload.
     */
    @Nonnull
    public Event receivePayload() throws MalformedPayloadException {
        checkOrThrow();

        try {
            return (Event) fromClient.readObject();
        } catch (ClassNotFoundException e) {
            throw new MalformedPayloadException("An unknown class was read !", e);
        } catch (InvalidClassException | StreamCorruptedException | OptionalDataException e) {
            throw new MalformedPayloadException("The payload is fucked up !", e);
        } catch (IOException e) {
            throw new MalformedPayloadException("An error occurred while reading the payload !", e);
        }
    }

    /**
     * Throw an exception if the socket or one of the streams are closed.
     */
    protected void checkOrThrow() {
        if (client == null || !client.isConnected())
            throw new IllegalStateException("The client is not ready !");
    }
}
