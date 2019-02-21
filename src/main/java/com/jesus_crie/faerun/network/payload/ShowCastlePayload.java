package com.jesus_crie.faerun.network.payload;

import com.jesus_crie.faerun.model.board.Castle;
import com.jesus_crie.faerun.network.NetDeSerializer;

import javax.annotation.Nonnull;

/**
 * Send a castle to the remote client and request it to show it.
 */
public class ShowCastlePayload extends NetPayload {

    private final Castle castle;

    public ShowCastlePayload(@Nonnull final Castle castle) {
        super(Opcode.SHOW_CASTLE);
        this.castle = castle;
    }

    public ShowCastlePayload(final byte[] data) {
        super(Opcode.SHOW_CASTLE);
        castle = NetDeSerializer.deserializeCastle(data, OFFSET_RESERVED);
    }

    @Override
    public byte[] serialize() {
        final byte[] data = prepareBuffer(
                NetDeSerializer.predictSizeSerializedCastle(castle));

        NetDeSerializer.serializeCastle(data, OFFSET_RESERVED, castle);

        return data;
    }
}
