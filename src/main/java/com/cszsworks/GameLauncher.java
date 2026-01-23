package com.cszsworks;

import com.cszsworks.util.ThemeManager;
import com.cszsworks.view.*;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorAutoCloseTrigger;

import java.util.Scanner;

public class GameLauncher {

    public static void main(String[] args) throws Exception {

        // konzolos input ELŐBB, hogy ne blokkolja a GUI-t
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine().trim();
        if (playerName.isEmpty()) {
            playerName = "Player1"; // default név
        }

        System.out.println("\nAvailable themes:");
        ThemeManager.getAvailableThemes()
                .forEach(name -> System.out.println(" - " + name));

        System.out.print("Choose a theme (press Enter for default): ");
        String themeChoice = scanner.nextLine().trim();

        if (themeChoice.isEmpty() || !ThemeManager.setTheme(themeChoice)) {
            System.out.println("Using default theme.\n");
        } else {
            System.out.println("Theme set to " + themeChoice + "\n");
        }

        System.out.println("Hello, " + playerName + "!");

        // --- GUI csak ezután indul ---
        SwingTerminalFrame terminal = new SwingTerminalFrame(
                "Amőba",
                TerminalEmulatorAutoCloseTrigger.CloseOnExitPrivateMode
        );

        terminal.setVisible(true);
        terminal.toFront();
        terminal.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        //alap hd ablakméret
        terminal.setSize(1366, 768);
        //képernyő közepéhez igazít
        terminal.setLocationRelativeTo(null);

        TerminalScreen screen = new TerminalScreen(terminal);
        screen.startScreen();
        screen.setCursorPosition(null);
        screen.doResizeIfNecessary();

        Game game = new Game(
                playerName,
                screen,
                new LanternaGameRenderer(screen),
                new LanternaMenuRenderer(screen),
                new LanternaHighScoreRenderer(screen),
                new LanternaNewGameRenderer(screen)
        );

        game.run();

        screen.close();
        terminal.close();
        System.exit(0);
    }
}
