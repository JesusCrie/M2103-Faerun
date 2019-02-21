package com.jesus_crie.faerun.network.payload;

import javax.annotation.Nonnull;

/**
 * Represent a request to ask the remote player his username.
 */
public class AskUsernamePayload extends EmptyPayload {

    public AskUsernamePayload() {
        super(Opcode.ASK_USERNAME);
    }

    public AskUsernamePayload(@Nonnull final byte[] data) {
        this();
    }
}
