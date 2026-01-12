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
        screen.clear();
        TextGraphics g = screen.newTextGraphics();

        TerminalSize size = screen.getTerminalSize();
        int startX = (size.getColumns() - table.getCols() * 4) / 2;
        int startY = (size.getRows() - table.getRows()) / 2;

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

        try {
            screen.refresh();
        } catch (Exception ignored) {}
    }

    public void renderGame(GameState state, Table table, int cursorRow, int cursorCol) {
        switch (state) {
            case PLAYING -> renderTable(table, cursorRow, cursorCol);
            case DRAW -> {
                renderTable(table,0,0);
                renderDraw();
            }
            case X_WINS -> {
                renderTable(table,0,0);
                renderWin(CellVO.Value.X);
            }
            case O_WINS -> {
                renderTable(table,0,0);
                renderWin(CellVO.Value.O);
            }
        }
    }

    private void renderDraw() {
        TextGraphics g = screen.newTextGraphics();

        String message = "Az eredmény döntetlen! Nyomjon Enter-t a kilépéshez!";

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

        String message = "A meccset nyerte az " + winner + "! Nyomjon Enter-t a kilépéshez!";

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

    public void close() {
        try {
            screen.stopScreen();
        } catch (Exception ignored) {}
    }
}
