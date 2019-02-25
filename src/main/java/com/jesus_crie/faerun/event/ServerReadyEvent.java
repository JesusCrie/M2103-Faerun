package com.jesus_crie.faerun.event;

/**
 * Triggered when the server socket has been created.
 */
public final class ServerReadyEvent implements Event {

    private static final long serialVersionUID = 4646412115291117812L;

    private final int port;

    public ServerReadyEvent(final int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }
}
