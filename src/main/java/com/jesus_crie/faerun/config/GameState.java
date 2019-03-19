package com.jesus_crie.faerun.config;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.EnumGetMethod;
import com.jesus_crie.faerun.model.Player;
import com.jesus_crie.faerun.model.Side;
import com.jesus_crie.faerun.model.board.Board;
import com.jesus_crie.faerun.model.board.BoardCell;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.board.Castle;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameState {

    private final Board board;
    private final int round;
    private final Player playerLeft;
    private final Player playerRight;

    public GameState(@Nonnull final Board board,
                     final int round,
                     @Nonnull final Player playerLeft,
                     @Nonnull final Player playerRight) {
        this.board = board;
        this.round = round;
        this.playerLeft = playerLeft;
        this.playerRight = playerRight;
    }

    public GameState(@Nonnull final Config from) {
        round = from.getInt("round");

        final BoardSettings settings = new BoardSettings(
                from.getInt("board.settings.size"),
                from.getInt("board.settings.baseCost"),
                from.getInt("board.settings.diceAmount"),
                from.getInt("board.settings.initialResources"),
                from.getInt("board.settings.resourcesPerRound")
        );

        final Castle castleLeft = inflateCastle(from.get("board.castle.left"), settings, Side.LEFT);
        final Castle castleRight = inflateCastle(from.get("board.castle.right"), settings, Side.RIGHT);

        playerLeft = castleLeft.getOwner();
        playerRight = castleRight.getOwner();

        final List<Config> cellsCfg = from.get("board.cells");
        final List<BoardCell> cells = cellsCfg.stream()
                .map(this::inflateCell)
                .collect(Collectors.toList());

        board = new Board(settings, Arrays.asList(castleLeft, castleRight), cells);
    }

    public void writeTo(@Nonnull final Config config) {
        // Round
        config.set("round", round);

        // Save settings
        final BoardSettings settings = board.getSettings();
        config.set("board.settings.size", settings.getSize());
        config.set("board.settings.baseCost", settings.getBaseCost());
        config.set("board.settings.diceAmount", settings.getDiceAmount());
        config.set("board.settings.initialResources", settings.getInitialResources());
        config.set("board.settings.resourcesPerRound", settings.getResourcesPerRound());

        // Save castles & players
        final Config castleLeft = config.createSubConfig();
        final Config castleRight = config.createSubConfig();
        saveCastle(castleLeft, board.getCastle(playerLeft));
        saveCastle(castleRight, board.getCastle(playerRight));

        config.set("board.castle.left", castleLeft);
        config.set("board.castle.right", castleRight);

        // Cells
        final List<Config> cellsCfg = IntStream.range(0, settings.getSize())
                .mapToObj(board::getCell)
                .map(cell -> {
                    final Config cellCfg = config.createSubConfig();
                    saveCell(cellCfg, cell);
                    return cellCfg;
                }).collect(Collectors.toList());

        config.set("board.cells", cellsCfg);
    }

    private void saveCastle(@Nonnull final Config cfg, @Nonnull final Castle castle) {
        cfg.set("owner", castle.getOwner().getPseudo());
        cfg.set("resources", castle.getResources());
        cfg.set("queue", castle.getTrainingQueue().stream()
                .map(w -> w.getClass().getName())
                .collect(Collectors.toList())
        );
    }

    private Castle inflateCastle(@Nonnull final Config cfg,
                                 @Nonnull final BoardSettings settings,
                                 @Nonnull final Side side) {

        final Player player = new Player(cfg.get("owner"), side);
        final Castle castle = new Castle(player, settings.getBaseCost(), cfg.get("resources"));

        final List<String> queue = cfg.get("queue");
        queue.stream()
                .map(ws -> inflateQueueWarrior(ws, player))
                .filter(Objects::nonNull)
                .forEachOrdered(castle::queueWarrior);

        return castle;
    }

    private void saveCell(@Nonnull final Config cfg, @Nonnull final BoardCell cell) {
        cfg.set("index", cell.getPosition());
        cfg.set("warriors", cell.getWarriors().stream()
                .map(w -> {
                    final Config wCfg = cfg.createSubConfig();
                    saveWarrior(wCfg, w);
                    return wCfg;
                })
                .collect(Collectors.toList())
        );
    }

    private BoardCell inflateCell(@Nonnull final Config cfg) {
        final BoardCell cell = new BoardCell(cfg.get("index"));

        final List<Config> warriors = cfg.get("warriors");
        cell.addWarriors(warriors.stream()
                .map(this::inflateWarrior)
                .collect(Collectors.toList()));

        return cell;
    }

    private void saveWarrior(@Nonnull final Config cfg, @Nonnull final Warrior warrior) {
        cfg.set("class", warrior.getClass().getName());
        cfg.set("side", warrior.getOwner().getSide().ordinal());
        cfg.set("health", warrior.getHealth());
    }

    @Nullable
    private Warrior inflateWarrior(@Nonnull final Config cfg) {

        final Warrior w;
        if (cfg.getEnum("side", Side.class, EnumGetMethod.ORDINAL_OR_NAME) == Side.LEFT) {
            w = inflateQueueWarrior(cfg.get("class"), playerLeft);
        } else {
            w = inflateQueueWarrior(cfg.get("class"), playerRight);
        }

        if (w == null)
            return null;

        w.setHealth(cfg.getInt("health"));
        return w;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    private Warrior inflateQueueWarrior(@Nonnull final String className, @Nonnull final Player player) {
        try {
            final Class<? extends Warrior> clazz = (Class<? extends Warrior>) Class.forName(className);
            final Constructor<? extends Warrior> constructor = clazz.getConstructor(Player.class);

            return constructor.newInstance(player);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
    }

    @Nonnull
    public Board getBoard() {
        return board;
    }

    public int getRound() {
        return round;
    }

    @Nonnull
    public Player getPlayerLeft() {
        return playerLeft;
    }

    @Nonnull
    public Player getPlayerRight() {
        return playerRight;
    }
}
