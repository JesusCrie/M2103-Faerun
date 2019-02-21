package com.jesus_crie.faerun.network.payload;

import javax.annotation.Nonnull;

/**
 * Request the remote client to indicate the start of a fight.
 */
public class ShowFightStartPayload extends EmptyPayload {

    public ShowFightStartPayload() {
        super(Opcode.SHOW_FIGHT_START);
    }

    public ShowFightStartPayload(final byte[] data) {
        this();
    }
}
