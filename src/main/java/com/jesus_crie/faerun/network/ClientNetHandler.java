package com.jesus_crie.faerun.network;

import com.jesus_crie.faerun.network.payload.NetPayload;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientNetHandler extends NetHandler {

    public ClientNetHandler(@Nonnull final InetAddress address, final int port) throws IOException {
        client = new Socket(address, port);
        // TODO 2/21/19 
    }

    @Override

    public boolean sendPayload(@Nonnull NetPayload payload) {
        return false;
    }

    @Nonnull
    @Override
    public NetPayload receivePayload() {
        return null;
    }
}
