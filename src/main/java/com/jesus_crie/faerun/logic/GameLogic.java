package com.jesus_crie.faerun.logic;

import com.jesus_crie.faerun.iohandler.FightOutputHandler;
import com.jesus_crie.faerun.iohandler.InputHandler;
import com.jesus_crie.faerun.iohandler.OutputHandler;
import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.board.Board;
import com.jesus_crie.faerun.model.board.BoardCell;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.board.Castle;
import com.jesus_crie.faerun.model.warrior.Warrior;
import com.jesus_crie.faerun.utils.Dice;
import com.jesus_crie.faerun.utils.Pair;

import javax.annotation.Nonnull;
import java.util.*;

public class GameLogic {

    private final Map<Player, Pair<InputHandler, OutputHandler>> players;
    private final CommonOutputHandler commonOutputHandler;
    private final BoardLogic boardLogic;
    private int roundNumber = 0;

    public GameLogic(@Nonnull final Map<Player, Pair<InputHandler, OutputHandler>> players,
                     @Nonnull final BoardSettings settings) {
        this.players = players;
        boardLogic = new BoardLogic(settings, new ArrayList<>(players.keySet()));
        commonOutputHandler = new CommonOutputHandler(
                players.values().stream()
                        .map(ioh -> ioh.getRight().getFightOutputHandler()).toArray(FightOutputHandler[]::new)
        );
    }

    public Map<Player, Pair<InputHandler, OutputHandler>> getPlayers() {
        return players;
    }

    public void performFullRound() {
        // Collect players
        final Player[] players = this.players.keySet().toArray(new Player[2]);

        // Current player
        Player current = players[0];
        // Log new round
        this.players.get(current).getRight().displayNewRound(current, roundNumber);

        // For each player, until victory
        for (int i = 0; !performPlayerRound(current); current = players[++i % players.length]) {
            this.players.get(current).getRight().displayNewRound(current, ++roundNumber);
        }
    }

    boolean performPlayerRound(@Nonnull final Player player) {
        performRoundTraining(player);
        return performRoundMoveAndFight(player);
    }

    void performRoundTraining(@Nonnull final Player player) {
        // Ask for what to train
        final Map<Class<? extends Warrior>, Integer> toBuild = players.get(player).getLeft().provideQueue();

        final Castle castle = boardLogic.getBoard().getCastle(player);

        // Process these
        toBuild.entrySet().stream()
                // Collect them
                .collect(
                        ArrayList<Warrior>::new,
                        (container, entry) -> container.addAll(boardLogic.buildWarrior(player, entry.getKey(), entry.getValue())),
                        ArrayList::addAll
                        // Queue them
                ).forEach(castle::queueWarriors);

        // Train and spawn them later
    }

    boolean performRoundMoveAndFight(@Nonnull final Player player) {
        // If left player
        if (player.getSide() == Player.Side.LEFT) {

            for (int i = 0; i < boardLogic.getBoard().getSettings().getSize() - 1; ++i) {
                final BoardCell targetCell = boardLogic.getBoard().getCell(i + 1);

                if (performRoundMoveAndFightLogic(player, targetCell))
                    return true;
            }

        // If right player
        } else {

            for (int i = boardLogic.getBoard().getSettings().getSize() - 1; i > 0; --i) {
                final BoardCell targetCell = boardLogic.getBoard().getCell(i - 1);

                if (performRoundMoveAndFightLogic(player, targetCell))
                    return true;
            }
        }

        return false;
    }

    private boolean performRoundMoveAndFightLogic(@Nonnull final Player player,
                                                  @Nonnull final BoardCell cell) {
        // If enemies on target cell
        if (cell.countEnnemis(player) != 0) {
            new FightLogic(cell).fight(player);
        }

        // No enemy and cell is last cell = victory
        return cell.countEnnemis(player) == 0
                && cell.getPosition() == boardLogic.getBoard().getSettings().getSize() - 1;
    }

    public class FightLogic {

        private final BoardCell cell;

        public FightLogic(@Nonnull final BoardCell cell) {
            this.cell = cell;
        }

        public void fight(@Nonnull final Player attacker) {
            // Collect allies and enemies
            final LinkedList<Warrior> allies = cell.getWarriors().stream()
                    .filter(w -> w.getOwner().equals(attacker))
                    .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
            final LinkedList<Warrior> enemies = cell.getWarriors().stream()
                    .filter(w -> !w.getOwner().equals(attacker))
                    .collect(LinkedList::new, LinkedList::add, LinkedList::addAll);

            // Shuffle dat
            Collections.shuffle(allies);
            Collections.shuffle(enemies);

            // Display regardless to the side of the player
            commonOutputHandler.displayLogStart(cell);
            if (attacker.getSide() == Player.Side.LEFT)
                commonOutputHandler.displayWarriors(allies, enemies);
            else
                commonOutputHandler.displayWarriors(enemies, allies);

            // Attackers
            attack(allies, enemies);
            // Defenders response
            attack(enemies, allies);
        }

        @SuppressWarnings("ConstantConditions")
        void attack(@Nonnull final Queue<Warrior> attackers, @Nonnull final Queue<Warrior> defenders) {
            // For each attacker
            for (Warrior attacker : attackers) {
                // Compute damages
                final int damage = Dice.diceRoll(3, attacker.getStrength());

                // Check if there's an enemy
                if (!defenders.isEmpty()) {
                    // Hit
                    defenders.peek().takeDamage(damage);
                    commonOutputHandler.displayLogHit(attacker, defenders.peek(), damage);

                    // Check dead
                    if (!defenders.peek().isAlive()) {
                        final Warrior dead = defenders.poll();
                        commonOutputHandler.displayLogDead(dead);
                    }
                }
            }
        }
    }

    private class CommonOutputHandler implements FightOutputHandler {

        private final FightOutputHandler[] handlers;

        public CommonOutputHandler(@Nonnull final FightOutputHandler... foh) {
            handlers = foh;
        }

        @Override
        public void displayLogStart(@Nonnull final BoardCell cell) {
            for (FightOutputHandler handler : handlers) handler.displayLogStart(cell);
        }

        @Override
        public void displayLogEnd(@Nonnull final BoardCell cell) {
            for (FightOutputHandler handler : handlers) handler.displayLogEnd(cell);
        }

        @Override
        public void displayWarriors(@Nonnull final List<Warrior> left, @Nonnull final List<Warrior> right) {
            for (FightOutputHandler handler : handlers) handler.displayWarriors(left, right);
        }

        @Override
        public void displayLogHit(@Nonnull final Warrior attacker, @Nonnull final Warrior defender, final int damage) {
            for (FightOutputHandler handler : handlers) handler.displayLogHit(attacker, defender, damage);
        }

        @Override
        public void displayLogDead(@Nonnull final Warrior warrior) {
            for (FightOutputHandler handler : handlers) handler.displayLogDead(warrior);
        }
    }
}
