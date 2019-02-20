package com.jesus_crie.faerun.network.payload;

import javax.annotation.Nonnull;

/**
 * Represent a request to ask the remote player his username.
 */
public class AskUsernamePayload extends EmptyPayload {

    public AskUsernamePayload(@Nonnull final byte[] data) {
        super(Opcode.ASK_USERNAME);
    }
}
