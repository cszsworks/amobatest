package com.cszsworks.view;

import com.cszsworks.persistence.model.HighScoreDisplayEntry;
import com.cszsworks.util.ThemeManager;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;

import java.util.List;

public class LanternaHighScoreRenderer {

    private final MultiWindowTextGUI gui;
    private BasicWindow window;

    public LanternaHighScoreRenderer(Screen screen) {
        this.gui = new MultiWindowTextGUI(screen);
    }

    public void render(List<HighScoreDisplayEntry> scores) {
        gui.setTheme(ThemeManager.getTheme());
        window = new BasicWindow("High Scores");

        // Main panel with vertical layout
        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        mainPanel.setPreferredSize(new TerminalSize(40, 20));

        // Title
        mainPanel.addComponent(new Label("Top High Scores")
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center)));

        // Highscore entries
        int rank = 1;
        for (HighScoreDisplayEntry entry : scores) {
            String line = String.format("%2d. %-15s %5d", rank++, entry.getUsername(), entry.getScore());
            mainPanel.addComponent(new Label(line));
        }

        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        // Close button to return to main menu
        Button closeButton = new Button("Return to Main Menu", () -> window.close());
        mainPanel.addComponent(closeButton.setLayoutData(
                LinearLayout.createLayoutData(LinearLayout.Alignment.Center)
        ));

        window.setComponent(mainPanel);
        window.setHints(java.util.Arrays.asList(Window.Hint.CENTERED));

        // Show window (blocks until closed)
        gui.addWindowAndWait(window);
    }
}
