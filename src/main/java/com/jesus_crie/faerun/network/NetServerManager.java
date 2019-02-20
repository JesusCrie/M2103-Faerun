package com.jesus_crie.faerun.network;

import javax.annotation.Nonnull;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetServerManager {

    private static final byte TERMINATOR = '\n';

    private final ServerSocket server;
    private Socket client;

    private PrintStream toClient;
    private DataInputStream fromClient;

    public NetServerManager() throws IOException {
        server = new ServerSocket(0);
    }

    public boolean waitForClient() {
        try {
            client = server.accept();

            toClient = new PrintStream(client.getOutputStream());
            fromClient = new DataInputStream(client.getInputStream());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean sendPayload(@Nonnull final NetPayload payload) {
        checkOrThrow();

        try {
            toClient.write(payload.serialize());
            toClient.write(TERMINATOR);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Nonnull
    public <T extends NetPayload> T receivePayload() {
        checkOrThrow();

        try {
            // Read header
            int opcodeRaw = fromClient.readUnsignedByte();
            final NetPayload.Opcode opcode = NetPayload.Opcode.fromOpcode(opcodeRaw);

            // Sanity check 1, valid opcode
            if (opcode == NetPayload.Opcode.UNKNOWN)
                throw new MalformedPayloadException("Unknown opcode: " + opcodeRaw);

            short dataLen = fromClient.readShort();

            // Read data
            byte[] data = new byte[dataLen];
            int res = fromClient.read(data);

            // Sanity check 2, length
            if (res != dataLen)
                throw new IllegalStateException("Something is wrong with the payload");

            // Sanity check 3, check terminator
            if (data[dataLen - 1] != TERMINATOR)
                throw new MalformedPayloadException("No newline at the end of the data !");

            // Rebuild payload

            // Need a constructor of the form XPayload(int len, byte[] data)
            final Constructor<T> constructor =
                    (Constructor<T>) opcode.getPayloadClass().getConstructor(byte[].class);

            return constructor.newInstance((Object) data);

        } catch (IOException e) {
            throw new MalformedPayloadException("An error occurred while reading the payload !", e);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            throw new IllegalStateException("This payload doesn't have a valid deserializer constructor !");
        } catch (InvocationTargetException e) {
            throw new MalformedPayloadException("Can't rebuild the payload !", e);
        }
    }

    public void skipToTerminator() {
        try {
            byte b;
            while ((b = (byte) fromClient.read()) != TERMINATOR) {
                /* no-op */
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkOrThrow() {
        if ((client == null || !client.isConnected()) && toClient != null && fromClient != null)
            throw new IllegalStateException("The client is not ready !");
    }
}
