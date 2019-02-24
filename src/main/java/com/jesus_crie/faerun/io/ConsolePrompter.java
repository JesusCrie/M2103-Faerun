package com.jesus_crie.faerun.io;

import com.jesus_crie.faerun.event.AskBoardSettingsEvent;
import com.jesus_crie.faerun.event.AskQueueEvent;
import com.jesus_crie.faerun.event.AskUsernameEvent;
import com.jesus_crie.faerun.model.board.BoardSettings;
import com.jesus_crie.faerun.model.warrior.Warrior;

import javax.annotation.Nonnull;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;

public class ConsolePrompter implements Prompter {

    private final PrintStream out;
    private final Scanner in;

    public ConsolePrompter(@Nonnull final PrintStream out,
                           @Nonnull final InputStream in) {
        this.out = out;
        this.in = new Scanner(in);
    }

    public ConsolePrompter() {
        this(System.out, System.in);
    }

    @Nonnull
    @Override
    public String onAskUsername(@Nonnull final AskUsernameEvent e) {
        return ConsoleUtils.ask(in, out, "What username do you want to use ?", "> ", s -> s);
    }

    @Nonnull
    @Override
    public BoardSettings onAskBoardSettings(@Nonnull final AskBoardSettingsEvent e) {
        // TODO 2/25/19
        return null;
    }

    @Nonnull
    @Override
    public Map<Class<? extends Warrior>, Integer> onAskQueue(@Nonnull final AskQueueEvent e) {
        // TODO 2/25/19 
        return null;
    }
}
