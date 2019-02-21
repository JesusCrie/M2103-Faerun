package com.jesus_crie.faerun.network.payload;

import javax.annotation.Nonnull;

/**
 * Represent a request to ask the remote player the settings
 * of the game.
 */
public class AskSettingsPayload extends EmptyPayload {

    public AskSettingsPayload() {
        super(Opcode.ASK_SETTINGS);
    }

    public AskSettingsPayload(@Nonnull final byte[] data) {
        this();
    }
}
