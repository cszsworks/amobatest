package com.cszsworks.view;

import com.cszsworks.controller.game.GameState;
import com.cszsworks.model.Table;
import com.cszsworks.model.CellVO;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.*;

import javax.swing.*;
import java.awt.*;

public class LanternaGameRenderer {

    private final Screen screen;

    // swing alapú terminál ablak létrehozáse
    public LanternaGameRenderer(Screen screen) throws Exception {
        this.screen = screen;

    }


    //lanterna renderTable, bemenet table, cursor sor és oszlop poz paraméterekkel
    public void renderTable(Table table, int cursorRow, int cursorCol) {
        TerminalScreen tscreen = (TerminalScreen) screen;
        tscreen.clear();

        TextGraphics g = screen.newTextGraphics();
        int startX = (screen.getTerminalSize().getColumns() - table.getCols() * 4) / 2;
        int startY = (screen.getTerminalSize().getRows() - table.getRows()) / 2;

        for (int i = 0; i < table.getRows(); i++) {
            for (int j = 0; j < table.getCols(); j++) {
                CellVO.Value v = table.getCell(i, j).getValue();
                String symbol = v == CellVO.Value.EMPTY ? "." : v.toString();

                if (i == cursorRow && j == cursorCol) {
                    g.setBackgroundColor(TextColor.ANSI.WHITE);
                    g.setForegroundColor(TextColor.ANSI.BLACK);
                } else {
                    g.setBackgroundColor(TextColor.ANSI.DEFAULT);
                    g.setForegroundColor(TextColor.ANSI.DEFAULT);
                }

                g.putString(startX + j * 4, startY + i, " " + symbol + " ");
            }
        }
    }

    public void renderGame(GameState state, Table table, int cursorRow, int cursorCol, String message) {
        renderTable(table, cursorRow, cursorCol);

        if (message != null && !message.isEmpty()) {
            TextGraphics g = screen.newTextGraphics();
            int row = 0; // top row
            int col = Math.max(0, (screen.getTerminalSize().getColumns() - message.length()) / 2);
            g.putString(col, row, message);
        }

        try {
            screen.refresh();
        } catch (Exception ignored) {}
    }

    private void renderDraw() {
        TextGraphics g = screen.newTextGraphics();

        String message = "Draw! Press Enter to return to the main menu";

        TerminalSize size = screen.getTerminalSize();
        int x = (size.getColumns() - message.length()) / 2;
        int y = 0;

        g.putString(x, y, message);

        try {
            screen.refresh();
        } catch (Exception ignored) {}
    }

    private void renderWin(CellVO.Value winner) {
        TextGraphics g = screen.newTextGraphics();

        String message = "The winner is: " + winner + " ! Press Enter to return to the main menu";

        TerminalSize size = screen.getTerminalSize();
        int x = (size.getColumns() - message.length()) / 2;
        int y = 0;

        g.putString(x, y, message);

        try {
            screen.refresh();
        } catch (Exception ignored) {}
    }

    public Screen getScreen() {
        return screen;
    }

}
