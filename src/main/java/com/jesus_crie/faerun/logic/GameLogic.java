package com.jesus_crie.faerun.logic;

import com.jesus_crie.faerun.io.FightOutputHandler;
import com.jesus_crie.faerun.io.InputHandler;
import com.jesus_crie.faerun.io.OutputHandler;
import com.jesus_crie.faerun.model.Player;
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

    @Nonnull
    public Player performFullGame() {
        // Collect players
        final Player[] players = this.players.keySet().toArray(new Player[2]);

        // Index current player
        byte i = -1;
        // Current player
        Player current;
        // For each player, until victory
        do {
            ++i;
            i %= players.length;
            current = players[i];

            // Log new round
            this.players.get(current).getRight().displayNewRound(current, ++roundNumber);
        } while (!performPlayerRound(current));

        // Return the winning player
        return current;
    }

    private boolean performPlayerRound(@Nonnull final Player player) {
        performRoundTraining(player);
        performRoundMoveAndFight(player);
        performRoundSpawn(player);

        players.get(player).getRight().displayBoardProgression(boardLogic.getBoard());
        players.get(player).getRight().displayBoard(boardLogic.getBoard());

        return hasWon(player.getSide());
    }

    private void performRoundTraining(@Nonnull final Player player) {
        // Castle resources
        final Castle castle = boardLogic.getBoard().getCastle(player);
        castle.addResources(boardLogic.getBoard().getSettings().getResourcesPerRound());

        // Ask for what to train
        players.get(player).getRight().displayCastleState(castle);
        final Map<Class<? extends Warrior>, Integer> toBuild = players.get(player).getLeft().provideQueue();

        // Process these
        toBuild.entrySet().stream()
                // Collect them
                .collect(
                        ArrayList<Warrior>::new,
                        (container, entry) -> container.addAll(boardLogic.buildWarrior(player, entry.getKey(), entry.getValue())),
                        ArrayList::addAll
                        // Queue them
                ).forEach(castle::queueWarriors);
    }

    private void performRoundSpawn(@Nonnull final Player player) {
        // Train and spawn them
        final List<Warrior> trained = boardLogic.getBoard().getCastle(player).train();

        boardLogic.spawn(
                player.getSide() == Player.Side.LEFT ?
                        0 : boardLogic.getBoard().getSettings().getSize() - 1,
                trained
        );
    }

    private void performRoundMoveAndFight(@Nonnull final Player player) {
        // If right player
        if (player.getSide() == Player.Side.RIGHT) {

            for (int i = 0; i < boardLogic.getBoard().getSettings().getSize(); i++) {
                final BoardCell origin = boardLogic.getBoard().getCell(i);

                // If allies on cell
                if (origin.countAllies(player.getSide()) != 0) {
                    final BoardCell target = boardLogic.getBoard().getCell(Math.max(i - 1, 0));

                    performRoundMoveAndFightLogic(player, origin, target);
                }
            }

        } else { // If left player

            for (int i = boardLogic.getBoard().getSettings().getSize() - 1; i >= 0; i--) {
                final BoardCell origin = boardLogic.getBoard().getCell(i);

                // If allies on cell
                if (origin.countAllies(player.getSide()) != 0) {
                    final BoardCell target = boardLogic.getBoard().getCell(
                            Math.min(i + 1, boardLogic.getBoard().getSettings().getSize() - 1)
                    );

                    performRoundMoveAndFightLogic(player, origin, target);
                }
            }
        }
    }

    private void performRoundMoveAndFightLogic(@Nonnull final Player player,
                                               @Nonnull final BoardCell origin,
                                               @Nonnull final BoardCell target) {
        // If enemies on origin cell, fight
        if (origin.countEnemies(player.getSide()) != 0) {
            final FightLogic fight = new FightLogic(origin);
            fight.fight(player);

            // Replace everyone
            origin.removeWarriors(fight.getDeadWarriors());

        } else { // If nobody, move
            // If not the same cell (happen when fight in the last cell
            if (origin.getPosition() != target.getPosition())
                boardLogic.move(origin.getPosition(), target.getPosition());

            // If enemies on target cell, fight
            if (target.countEnemies(player.getSide()) != 0) {
                new FightLogic(target).fight(player);
            }
        }
    }

    private boolean hasWon(@Nonnull final Player.Side side) {
        // Get opposite cell depending on side
        final BoardCell target;
        if (side == Player.Side.RIGHT)
            target = boardLogic.getBoard().getCell(0);
        else
            target = boardLogic.getBoard().getCell(boardLogic.getBoard().getSettings().getSize() - 1);

        // If there are only allies in the cell = victory
        return target.countEnemies(side) == 0 && target.countAllies(side) != 0;
    }

    public class FightLogic {

        private final BoardCell cell;
        private final LinkedList<Warrior> deadWarriors = new LinkedList<>();

        public FightLogic(@Nonnull final BoardCell cell) {
            this.cell = cell;
        }

        public LinkedList<Warrior> getDeadWarriors() {
            return deadWarriors;
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

            commonOutputHandler.displayLogEnd(cell);
        }

        @SuppressWarnings("ConstantConditions")
        private void attack(@Nonnull final Queue<Warrior> attackers, @Nonnull final Queue<Warrior> defenders) {
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
                        deadWarriors.add(dead);
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
            for (FightOutputHandler handler : handlers)
                handler.displayLogHit(attacker, defender, damage);
        }

        @Override
        public void displayLogDead(@Nonnull final Warrior warrior) {
            for (FightOutputHandler handler : handlers) handler.displayLogDead(warrior);
        }
    }
}
