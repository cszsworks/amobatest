package com.cszsworks.view;


import com.cszsworks.controller.game.GameState;
import com.cszsworks.model.Table;
import com.cszsworks.model.CellVO;
import com.cszsworks.persistence.model.HighScoreDisplayEntry;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;


public class LanternaHighScoreRenderer {
    private final Screen screen;
    private List<HighScoreDisplayEntry> scores;

    public LanternaHighScoreRenderer(Screen screen) {
        this.screen = screen;
    }



    public Screen getScreen() {
        return screen;
    }

    public void render(List<HighScoreDisplayEntry> scores) throws IOException {

        screen.clear();
        TextGraphics tg = screen.newTextGraphics();

        int startRow = 1;      // top padding
        int nameColumn = 2;    // left side
        int scoreColumn = 30;  // right side (adjust if needed)

        int maxLines = Math.min(10, scores.size());

        for (int i = 0; i < maxLines; i++) {
            HighScoreDisplayEntry entry = scores.get(i);

            tg.putString(
                    nameColumn,
                    startRow + i,
                    entry.getUsername()
            );

            tg.putString(
                    scoreColumn,
                    startRow + i,
                    String.valueOf(entry.getScore())
            );
        }

        screen.refresh();
    }


}
