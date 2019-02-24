package com.jesus_crie.faerun.event;

import com.jesus_crie.faerun.model.board.BoardSettings;

import javax.annotation.Nonnull;

/**
 * Triggered when the settings are ready and definitive.
 * This event is meant to be used to send the settings to the remote player.
 */
public final class SettingsReadyEvent implements GameEvent {

    private final BoardSettings settings;

    public SettingsReadyEvent(@Nonnull final BoardSettings settings) {
        this.settings = settings;
    }

    @Nonnull
    public BoardSettings getSettings() {
        return settings;
    }
}
