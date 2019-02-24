package com.jesus_crie.faerun.network.internal;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientNetHandler extends NetHandler {

    public ClientNetHandler(@Nonnull final InetAddress address, final int port) throws IOException {
        client = new Socket(address, port);
    }
}
