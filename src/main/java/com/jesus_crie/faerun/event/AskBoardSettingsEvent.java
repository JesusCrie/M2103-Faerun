package com.jesus_crie.faerun.event;

import com.jesus_crie.faerun.model.board.BoardSettings;

/**
 * Ask for the current game settings.
 */
public final class AskBoardSettingsEvent implements AskEvent<BoardSettings> {
    private static final long serialVersionUID = 6718754423326836422L;
}
