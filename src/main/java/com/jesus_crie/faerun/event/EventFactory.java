package com.jesus_crie.faerun.event;

import com.jesus_crie.faerun.logic.FightRecord;
import com.jesus_crie.faerun.model.Side;
import com.jesus_crie.faerun.model.board.Board;
import com.jesus_crie.faerun.model.board.BoardCell;
import com.jesus_crie.faerun.model.board.Castle;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * Contains static factory methods to easily build any event.
 */
public final class EventFactory {

    @Nonnull
    public static ConnectEvent buildConnectEvent() {
        return new ConnectEvent();
    }

    @Nonnull
    public static WelcomeEvent buildWelcomeEvent() {
        return new WelcomeEvent();
    }

    @Nonnull
    public static NewRoundEvent buildNewRoundEvent(final int roundNumber,
                                                   @Nonnull final String playerName,
                                                   @Nonnull final Board board) {
        return new NewRoundEvent(roundNumber, playerName,
                IntStream.range(0, board.getSettings().getSize())
                        .mapToObj((IntFunction<Object>) board::getCell)
                        .map(c -> ((BoardCell) c).getSide()).toArray(Side[]::new)
        );
    }

    @Nonnull
    public static ServerReadyEvent buildServerReadyEvent(final int port) {
        return new ServerReadyEvent(port);
    }

    @Nonnull
    public static GoodbyeEvent buildGoodbyeEvent(@Nonnull final String winner) {
        return new GoodbyeEvent(winner);
    }

    @Nonnull
    public static TeardownEvent buildTeardownEvent() {
        return new TeardownEvent();
    }

    @Nonnull
    public static AskUsernameEvent buildAskUsernameEvent() {
        return new AskUsernameEvent();
    }

    @Nonnull
    public static AskRemoteAddress buildAskRemoteAddress() {
        return new AskRemoteAddress();
    }

    @Nonnull
    public static AskSettingsEvent buildAskSettingsEvent() {
        return new AskSettingsEvent();
    }

    @Nonnull
    public static FightEvent buildFightEvent(@Nonnull final FightRecord record) {
        return new FightEvent(record);
    }

    @SuppressWarnings("unchecked")
    public static AskQueueEvent buildAskQueueEvent(@Nonnull final Castle castle) {
        return new AskQueueEvent(castle.getResources(),
                castle.getTrainingQueue().stream()
                        .map(Warrior::getClass)
                        .toArray(Class[]::new)
        );
    }
}
