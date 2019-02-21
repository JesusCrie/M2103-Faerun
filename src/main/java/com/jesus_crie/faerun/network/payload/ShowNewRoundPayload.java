package com.jesus_crie.faerun.network.payload;

import com.jesus_crie.faerun.network.NetDeSerializer;

/**
 * Send the current round index to the remote client and ask to
 * show it.
 */
public class ShowNewRoundPayload extends NetPayload {

    private final int round;

    public ShowNewRoundPayload(final int round) {
        super(Opcode.SHOW_NEW_ROUND);
        this.round = round;
    }

    public ShowNewRoundPayload(final byte[] data) {
        super(Opcode.SHOW_NEW_ROUND);
        round = NetDeSerializer.rebuildInt(data, OFFSET_RESERVED);
    }

    @Override
    public byte[] serialize() {
        final byte[] data = prepareBuffer(4);
        NetDeSerializer.destructureInt(data, OFFSET_RESERVED, round);

        return data;
    }
}
