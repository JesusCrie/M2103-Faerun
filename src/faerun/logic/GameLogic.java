package faerun.logic;

import faerun.game.Player;
import faerun.warrior.Warrior;

import java.util.Arrays;
import java.util.List;

public class GameLogic {

    private final InputProvider provider;
    private final Board board;
    private final List<Player> players;

    public GameLogic(final InputProvider provider) {
        this.provider = provider;
        board = new Board(provider.provideBoardFormat());
        players = Arrays.asList(provider.providePlayers());
    }

    public void round() {
        // Training phase
        for (Player player : players) {
            roundPhaseTraining(player);
        }

        // Move phase

    }

    private void roundPhaseTraining(final Player player) {
        final Castle castle = board.getCastle(player);
        castle.collectResources();


        castle.queueTraining();

        final List<Warrior> toDeploy = castle.train();

        if (toDeploy.isEmpty())
            return;

        int[] deployPoints = board.getCastleDeployPoints(castle);

        final int deployPos;
        if (deployPoints.length > 1) {
            // TODO ask to choose
            deployPos = 0;
        } else deployPos = deployPoints[0];

        board.deploy(deployPos, toDeploy);
    }

    private void roundPhaseMoveAttack(final Player player) {
        // Iterate all cells
        for (int i = 0; i < board.getBoard().size(); i++) {
            if (board.getBoard().get(i).isThereEnemiesOf(player)) {

            }
        }
    }
}
