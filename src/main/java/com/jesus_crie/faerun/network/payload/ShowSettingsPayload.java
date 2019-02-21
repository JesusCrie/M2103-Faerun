package com.jesus_crie.faerun.network.payload;

import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.network.NetDeSerializer;

import javax.annotation.Nonnull;

/**
 * Send the settings of the game and request the remote client to show them.
 */
public class ShowSettingsPayload extends NetPayload {

    private final BoardSettings settings;

    public ShowSettingsPayload(@Nonnull final BoardSettings settings) {
        super(Opcode.SHOW_SETTINGS);
        this.settings = settings;
    }

    public ShowSettingsPayload(final byte[] data) {
        super(Opcode.SHOW_SETTINGS);
        settings = NetDeSerializer.deserializeSettings(data, OFFSET_RESERVED);
    }

    @Override
    public byte[] serialize() {
        final byte[] data = prepareBuffer(NetDeSerializer.predictSizeSerializedSettings(settings));
        NetDeSerializer.serializeSettings(data, OFFSET_RESERVED, settings);

        return data;
    }
}
