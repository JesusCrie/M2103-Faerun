package faerun.logic;

import faerun.game.Player;
import faerun.warrior.Warrior;

public interface InputProvider {

    /**
     * @return The list of currently playing players.
     */
    Player[] providePlayers();

    /**
     * @return The board format of this game.
     */
    Board.BoardFormat provideBoardFormat();

    /**
     * Query the player to get his choices regarding the training of new warriors.
     *
     * @return The warriors to train.
     */
    Warrior[] provideWarriorsToTrain(final Player player);

    /**
     * Called when there are multiple possibilities of where to deploy warriors.
     *
     * @return True for left, false for right.
     */
    boolean askDirection(final Player player);
}
