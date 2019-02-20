package com.jesus_crie.faerun.network.payload;

import javax.annotation.Nonnull;

/**
 * Represent a payload that doesn't carry any additional data.
 */
public abstract class EmptyPayload extends NetPayload {

    public EmptyPayload(@Nonnull final Opcode opcode) {
        super(opcode);
    }

    @Override
    public byte[] serialize() {
        return prepareBuffer(0);
    }
}
