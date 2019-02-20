package com.jesus_crie.faerun.network.payload;

import com.jesus_crie.faerun.network.NetDestructuror;

public class ShowNewRoundPayload extends NetPayload {

    private final int round;

    public ShowNewRoundPayload(final int round) {
        super(Opcode.SHOW_NEW_ROUND);
        this.round = round;
    }

    public ShowNewRoundPayload(final byte[] data) {
        super(Opcode.SHOW_NEW_ROUND);
        round = NetDestructuror.rebuildInt(data, 0);
    }

    @Override
    public byte[] serialize() {
        final byte[] data = prepareBuffer(4);
        NetDestructuror.destructureInt(data, OFFSET_RESERVED, round);

        return data;
    }
}