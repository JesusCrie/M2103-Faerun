package com.jesus_crie.faerun.network.payload;

import javax.annotation.Nonnull;

/**
 * Represent a request to ask the remote player what units he wants
 * to queue.
 */
public class AskQueuePayload extends EmptyPayload {

    public AskQueuePayload(@Nonnull final byte[] data) {
        super(Opcode.ASK_QUEUE);
    }
}
