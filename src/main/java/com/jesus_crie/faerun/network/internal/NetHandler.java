package com.jesus_crie.faerun.network.internal;

import com.jesus_crie.faerun.event.Event;
import com.jesus_crie.faerun.network.MalformedPayloadException;

import javax.annotation.Nonnull;
import java.io.*;
import java.net.Socket;

public abstract class NetHandler implements Closeable {

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
     * Send a payload over the network through the socket.
     *
     * @param payload - The paylaod to send.
     * @return True of the payload has been successfully sent through the socket, otherwise False.
     */
    public boolean sendPayload(@Nonnull final Serializable payload) {
        checkOrThrow();

        try {
            toClient.writeObject(payload);
            toClient.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Send an event over the network through the client socket.
     *
     * @param payload - The payload to send.
     * @return True if the payload has been successfully sent through the socket, otherwise false.
     */
    public boolean sendEvent(@Nonnull final Event payload) {
        return sendPayload(payload);
    }

    /**
     * Wait for an object to be retrieved from the socket.
     *
     * @param <T> - The type of object to wait for.
     * @return The deserialized object read from the socket.
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T receivePayload() {
        checkOrThrow();

        try {
            return (T) fromClient.readObject();
        } catch (ClassNotFoundException e) {
            throw new MalformedPayloadException("An unknown class was read !", e);
        } catch (InvalidClassException | StreamCorruptedException | OptionalDataException e) {
            throw new MalformedPayloadException("The payload is fucked up !", e);
        } catch (IOException e) {
            throw new MalformedPayloadException("An error occurred while reading the payload !", e);
        }
    }

    /**
     * Wait for an event to be retrieved from the socket.
     *
     * @return The deserialized event read from the socket.
     */
    @Nonnull
    public Event receiveEvent() throws MalformedPayloadException {
        return receivePayload();
    }

    /**
     * Throw an exception if the socket or one of the streams are closed.
     */
    protected void checkOrThrow() {
        if (client == null || !client.isConnected())
            throw new IllegalStateException("The client is not ready !");
    }

    @Override
    public void close() {
        try {
            client.close();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to close the socket !", e);
        }
    }
}
