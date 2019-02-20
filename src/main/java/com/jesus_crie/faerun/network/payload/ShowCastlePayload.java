package com.jesus_crie.faerun.network.payload;

import com.jesus_crie.faerun.model.board.Castle;
import com.jesus_crie.faerun.network.NetDestructuror;

import javax.annotation.Nonnull;

public class ShowCastlePayload extends NetPayload {

    private final Castle castle;

    public ShowCastlePayload(@Nonnull final Castle castle) {
        super(Opcode.SHOW_CASTLE);
        this.castle = castle;
    }

    public ShowCastlePayload(final byte[] data) {
        super(Opcode.SHOW_CASTLE);
        castle = NetDestructuror.rebuildCastle(data, 0);
    }

    @Override
    public byte[] serialize() {
        final byte[] data = prepareBuffer(
                NetDestructuror.predictSizeDestructuredCastle(castle));

        NetDestructuror.destructureCastle(data, OFFSET_RESERVED, castle);

        return data;
    }
}
