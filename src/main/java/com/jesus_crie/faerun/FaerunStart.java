package com.jesus_crie.faerun;

import com.jesus_crie.faerun.gamemode.ConsoleGame;
import com.jesus_crie.faerun.gamemode.GameClient;
import com.jesus_crie.faerun.gamemode.NetworkClient;
import com.jesus_crie.faerun.gamemode.NetworkServer;
import com.jesus_crie.faerun.io.ConsoleUtils;
import com.jesus_crie.faerun.utils.Pair;

import javax.annotation.Nonnull;
import java.util.Scanner;

public class FaerunStart {

    public static void main(String[] args) {
        ConsoleUtils.createMenu(new Scanner(System.in), System.out,
                "What gamemode do you want to play with ?",
                Pair.of(
                        "Local 1 vs 1 (resumable)",
                        () -> startGame(new ConsoleGame())
                ), Pair.of(
                        "Network: Server",
                        () -> startGame(new NetworkServer())
                ), Pair.of(
                        "Network: Client",
                        () -> startGame(new NetworkClient())
                )
        );
    }

    private static void startGame(@Nonnull final GameClient client) {
        client.start();
    }
}
