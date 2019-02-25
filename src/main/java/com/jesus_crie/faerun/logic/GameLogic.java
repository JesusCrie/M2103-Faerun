package com.jesus_crie.faerun.logic;

import com.jesus_crie.faerun.event.Event;
import com.jesus_crie.faerun.event.EventFactory;
import com.jesus_crie.faerun.io.IOCombiner;
import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.Side;
import com.jesus_crie.faerun.model.board.BoardCell;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.board.Castle;
import com.jesus_crie.faerun.model.warrior.Warrior;
import com.jesus_crie.faerun.utils.Dice;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * Contains all of the logic of the game.
 */
public final class GameLogic {

    private final Map<Player, IOCombiner> players;
    private final BoardLogic boardLogic;
    private int roundNumber = 0;

    public GameLogic(@Nonnull final Map<Player, IOCombiner> players,
                     @Nonnull final BoardSettings settings) {
        this.players = players;
        boardLogic = new BoardLogic(settings, new ArrayList<>(players.keySet()));
    }

    public Map<Player, IOCombiner> getPlayers() {
        return players;
    }

    /**
     * Dispatch an event to every player of the game.
     *
     * @param event - The event to dispatch.
     */
    private void dispatchToEveryone(@Nonnull final Event event) {
        players.forEach((p, io) -> io.dispatch(event));
    }

    /**
     * Main loop of the game.
     * Calling this method will play the whole game and returns the winner.
     *
     * @return The winner of the game.
     */
    @Nonnull
    public Player performFullGame() {
        // Collect players
        final Player[] players = this.players.keySet().toArray(new Player[2]);

        // Send welcome
        dispatchToEveryone(EventFactory.buildWelcomeEvent());

        // Index current player
        byte i = -1;
        // Current player
        Player current;
        // For each player, until victory
        do {
            ++i;
            i %= players.length;
            current = players[i];

            roundNumber++;

            // Dispatch new round
            dispatchToEveryone(
                    EventFactory.buildNewRoundEvent(roundNumber, current.getPseudo(), boardLogic.getBoard())
            );

        } while (!performPlayerRound(current));

        // Return the winning player
        return current;
    }

    /**
     * Perform one full round for a player.
     *
     * @param player - The owner of the round.
     * @return True if the player has won, otherwise False.
     */
    private boolean performPlayerRound(@Nonnull final Player player) {
        // Train units
        performRoundTraining(player);
        // Move units and fight
        performRoundMoveAndFight(player);
        // Spawn trained units
        performRoundSpawn(player);

        return hasWon(player.getSide());
    }

    /**
     * Perform the training part of the round where the player is asked what units he wants to train.
     *
     * @param player - The owner of the round.
     */
    private void performRoundTraining(@Nonnull final Player player) {
        // Castle resources
        final Castle castle = boardLogic.getBoard().getCastle(player);
        castle.addResources(boardLogic.getBoard().getSettings().getResourcesPerRound());

        // Ask for what to train
        final Map<Class<? extends Warrior>, Integer> toBuild = players.get(player).dispatch(
                EventFactory.buildAskQueueEvent(castle)
        );

        // Build and queue
        toBuild.entrySet().stream()
                .flatMap(entry -> boardLogic.buildWarrior(player, entry.getKey(), entry.getValue()).stream())
                .forEachOrdered(castle::queueWarrior);
    }

    /**
     * Perform the move step of the round and delegates the subsequent fights that might happen.
     *
     * @param player - The owner of the round.
     */
    private void performRoundMoveAndFight(@Nonnull final Player player) {
        // If right player
        if (player.getSide() == Side.RIGHT) {

            for (int i = 0; i < boardLogic.getBoard().getSettings().getSize(); i++) {
                final BoardCell origin = boardLogic.getBoard().getCell(i);

                // If allies on cell (there is something to move)
                if (origin.countAllies(player.getSide()) != 0) {
                    final BoardCell target = boardLogic.getBoard().getCell(Math.max(i - 1, 0));

                    performRoundMoveLogic(player, origin, target);
                }
            }

        } else { // If left player

            for (int i = boardLogic.getBoard().getSettings().getSize() - 1; i >= 0; i--) {
                final BoardCell origin = boardLogic.getBoard().getCell(i);

                // If allies on cell (there is something to move)
                if (origin.countAllies(player.getSide()) != 0) {
                    final BoardCell target = boardLogic.getBoard().getCell(
                            Math.min(i + 1, boardLogic.getBoard().getSettings().getSize() - 1)
                    );

                    performRoundMoveLogic(player, origin, target);
                }
            }
        }
    }

    /**
     * Perform the move and fight mechanism where we trigger a fight on a cell with enemies.
     * The fight logic is delegated somewhere else.
     *
     * @param player - The owner of the round.
     * @param origin - The origin cell where we come from.
     * @param target - The target cell where we want to move to.
     */
    private void performRoundMoveLogic(@Nonnull final Player player,
                                       @Nonnull final BoardCell origin,
                                       @Nonnull final BoardCell target) {
        // If enemies on origin cell, fight
        if (origin.countEnemies(player.getSide()) != 0) {
            performRoundFightLogic(player, origin);

        } else { // If nobody, just move

            // If not the same cell (happens when on the last cell)
            if (origin.getPosition() != target.getPosition())
                boardLogic.move(origin.getPosition(), target.getPosition());

            // If enemies on target cell, fight
            if (target.countEnemies(player.getSide()) != 0) {
                performRoundFightLogic(player, target);
            }
        }
    }

    /**
     * Perform the logic of a single fight and produces the associated event.
     *
     * @param player - The attacker.
     * @param cell   - The target cell.
     */
    private void performRoundFightLogic(@Nonnull final Player player,
                                        @Nonnull final BoardCell cell) {
        final FightLogic fight = new FightLogic(cell);
        // Fight
        final FightRecord record = fight.fight(player);

        // Remove corpses
        cell.removeWarriors(fight.getDeadWarriors());

        // Event
        dispatchToEveryone(
                EventFactory.buildFightEvent(record)
        );
    }

    /**
     * Perform the spawning part of the round where the units are effectively trained and spawned onto the board.
     *
     * @param player - The owner of the round.
     */
    private void performRoundSpawn(@Nonnull final Player player) {
        // Train and spawn them
        final List<Warrior> trained = boardLogic.getBoard().getCastle(player).train();

        boardLogic.spawn(player.getSide() == Side.LEFT ?
                0 : boardLogic.getBoard().getSettings().getSize() - 1, trained
        );
    }

    /**
     * Check if the player of the given side has won.
     *
     * @param side - The side of the player to check.
     * @return True if the player has won, otherwise False.
     */
    private boolean hasWon(@Nonnull final Side side) {
        // Get opposite cell depending on side
        final BoardCell target;
        if (side == Side.RIGHT)
            target = boardLogic.getBoard().getCell(0);
        else // side == Side.LEFT
            target = boardLogic.getBoard().getCell(boardLogic.getBoard().getSettings().getSize() - 1);

        // If there are only allies in the cell = victory
        return target.countEnemies(side) == 0 && target.countAllies(side) != 0;
    }

    /**
     * Handles the logic of one fight
     */
    public final class FightLogic {

        private final BoardCell cell;
        private final LinkedList<Warrior> deadWarriors = new LinkedList<>();
        private final List<FightEntry> fightEntries = new LinkedList<>();

        public FightLogic(@Nonnull final BoardCell cell) {
            this.cell = cell;
        }

        /**
         * @return The list of the warriors who died during this fight.
         */
        public LinkedList<Warrior> getDeadWarriors() {
            return deadWarriors;
        }

        /**
         * Initiate the fight, query the attackers and defenders, shuffle them and start the fight.
         *
         * @param attacker - The attacker.
         * @return The {@link FightRecord} that contains every action of this fight.
         * @throws IllegalStateException If this fight logic has already been performed.
         */
        @Nonnull
        public FightRecord fight(@Nonnull final Player attacker) {
            if (!fightEntries.isEmpty())
                throw new IllegalStateException("This fight logic has already been performed !");

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

            // Store array representation for the record
            final Warrior[] attackersArray = allies.toArray(Warrior[]::new);
            final Warrior[] defendersArray = enemies.toArray(Warrior[]::new);

            // Attackers
            attack(allies, enemies);
            // Defenders response
            attack(enemies, allies);

            // Build fight record
            return new FightRecord(cell.getPosition(), attacker.getSide(),
                    attackersArray, defendersArray, fightEntries.toArray(FightEntry[]::new));
        }

        /**
         * Perform the logic of one pass of the fight.
         *
         * @param attackers - The attackers of this pass.
         * @param defenders - The defenders of this pass.
         */
        @SuppressWarnings("ConstantConditions")
        private void attack(@Nonnull final LinkedList<Warrior> attackers, @Nonnull final LinkedList<Warrior> defenders) {
            // For each attacker
            for (int attackerI = 0; attackerI < attackers.size(); attackerI++) {
                final Warrior attacker = attackers.get(attackerI);

                // Compute damages
                final int damage = Dice.diceRoll(3, attacker.getStrength());

                // Check if there's an enemy
                if (!defenders.isEmpty()) {
                    // Hit
                    defenders.peek().takeDamage(damage);
                    fightEntries.add(new FightEntry.Hit(attacker.getOwner().getSide(),
                            defenders.size() - 1, attackerI, damage)
                    );

                    // Check dead
                    if (!defenders.peek().isAlive()) {
                        final Warrior dead = defenders.poll();
                        fightEntries.add(new FightEntry.Dead(dead.getOwner().getSide(), defenders.size()));
                        deadWarriors.add(dead);
                    }
                }
            }
        }
    }
}
