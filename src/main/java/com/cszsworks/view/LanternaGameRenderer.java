package com.cszsworks.view;

import com.cszsworks.model.Table;
import com.cszsworks.model.CellVO;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class LanternaGameRenderer {

    private final Screen screen;

    // swing alapú terminál ablak létrehozáse
    public LanternaGameRenderer(Screen screen) {
        this.screen = screen;

    }


    //lanterna renderTable, bemenet table, cursor sor és oszlop poz paraméterekkel
    public void renderTable(Table table, int cursorRow, int cursorCol) {
        screen.clear();
        TextGraphics g = screen.newTextGraphics();

        int cols = table.getCols();

        int startX = (screen.getTerminalSize().getColumns() - table.getCols() * 4) / 2;
        int startY = (screen.getTerminalSize().getRows() - table.getRows()) / 2;

        for (int j = 0; j < cols; j++) {
            String colLabel = String.valueOf(j + 1); // 1től kezdje
            g.putString(startX + j * 4 + 1, startY - 1, colLabel); // +1 for padding
        }

        for (int i = 0; i < table.getRows(); i++) {

            char rowLabel = (char) ('A' + i % 26); // Ez átfordul Z-re ansi kód alapján
            g.putString(startX - 2, startY + i, String.valueOf(rowLabel));


            for (int j = 0; j < table.getCols(); j++) {
                CellVO.Value v = table.getCell(i, j).value();
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

    public void renderGame(Table table, int cursorRow, int cursorCol, String message) {
        renderTable(table, cursorRow, cursorCol);

        if (message != null && !message.isEmpty()) {
            TextGraphics g = screen.newTextGraphics();
            int row = 0; // felso sor
            int col = Math.max(0, (screen.getTerminalSize().getColumns() - message.length()) / 2);
            g.putString(col, row, message);
        }

        try {
            screen.refresh();
        } catch (Exception ignored) {}
    }


    public Screen getScreen() {
        return screen;
    }

}
