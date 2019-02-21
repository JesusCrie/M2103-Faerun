package com.jesus_crie.faerun.network.payload;

/**
 * Request the remote client to indicate the end of a fight.
 */
public class ShowFightEndPayload extends EmptyPayload {

    public ShowFightEndPayload() {
        super(Opcode.SHOW_FIGHT_END);
    }

    public ShowFightEndPayload(final byte[] data) {
        this();
    }
}
