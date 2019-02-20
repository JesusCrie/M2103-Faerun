package com.jesus_crie.faerun.iohandler;

import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.board.Board;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.board.Castle;

import javax.annotation.Nonnull;

public interface OutputHandler {

    FightOutputHandler getFightOutputHandler();

    void displayNewRound(@Nonnull final Player player, final int roundNumber);

    void displayBoardSettings(@Nonnull final BoardSettings settings);

    void displayBoardProgression(@Nonnull final Board board);

    void displayBoard(@Nonnull final Board board);

    void displayCastleState(@Nonnull final Castle castle);
}
