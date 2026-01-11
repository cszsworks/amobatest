package com.cszsworks.view;

import com.cszsworks.model.GameState;
import com.cszsworks.model.Table;
import com.cszsworks.model.CellVO;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorColorConfiguration;
import com.googlecode.lanterna.terminal.swing.TerminalEmulatorDeviceConfiguration;

import javax.swing.*;

public class LanternaRenderer {

    private final Screen screen;

    // swing alapú terminál ablak létrehozáse, elkerüli az inkonzisztens viselkedést a különböző OS terminalok között
    public LanternaRenderer() throws Exception {

        //default beállítások amik a lanterna swing terminal constructorhoz szükségesek
        TerminalEmulatorDeviceConfiguration deviceConfig = new TerminalEmulatorDeviceConfiguration();
        SwingTerminalFontConfiguration fontConfig = SwingTerminalFontConfiguration.getDefault();
        TerminalEmulatorColorConfiguration colorConfig = TerminalEmulatorColorConfiguration.getDefault();

        SwingTerminal terminal = new SwingTerminal(deviceConfig, fontConfig, colorConfig);

        // Jframe java konténerbe
        JFrame frame = new JFrame("Amőba");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(terminal);             // hozzáad
        frame.pack();                     // prekalkulál
        frame.setLocationRelativeTo(null);// centerez
        frame.setVisible(true);           // létrehoz

        terminal.requestFocusInWindow(); // a terminál kéri az OS input focust

        // most a lanterna terminal körülveszi
        screen = new TerminalScreen(terminal);

        // Elindítja a kombinaciot
        screen.startScreen();
        screen.setCursorPosition(null);
    }


    //lanterna renderTable, bemenet table, cursor sor és oszlop poz paraméterekkel
    public void renderTable(Table table, int cursorRow, int cursorCol) {
        screen.clear();
        TextGraphics g = screen.newTextGraphics();

        TerminalSize size = screen.getTerminalSize();
        //margin felező logika
        int startX = (size.getColumns() - table.getCols() * 4) / 2; //horizontális kezdőpont, minden cella 4 széles, ezt kivonom majd osztom 2vel
        int startY = (size.getRows() - table.getRows()) / 2; //vertikalis kezdőpont, minden cella 2 magas, ezt kivonom majd osztom 2vel

        for (int i = 0; i < table.getRows(); i++) {
            for (int j = 0; j < table.getCols(); j++) {
                CellVO.Value v = table.getCell(i, j).getValue();
                String symbol = v == CellVO.Value.EMPTY ? "." : v.toString();

                //ha a kurzor ezen a pozicion van, átszínezzük
                if (i == cursorRow && j == cursorCol) {
                    g.setBackgroundColor(TextColor.ANSI.WHITE);
                    g.setForegroundColor(TextColor.ANSI.BLACK);
                } else {
                    g.setBackgroundColor(TextColor.ANSI.DEFAULT);
                    g.setForegroundColor(TextColor.ANSI.DEFAULT);
                }
                //kezdo oszlop + j-edik hely, kezdo sor + i-edik sor, adatszimbolum és spacek
                g.putString(startX + j * 4, startY + i, " " + symbol + " ");
            }
        }

        try {
            screen.refresh();
        } catch (Exception filler) {}
    }

    //beágyazott render, tábla tetejére rajzolható státusz és egyéb üzenetek
    public void renderGame(GameState state, Table table, int cursorRow, int cursorCol) {
        switch (state) {
            case PLAYING -> renderTable(table, cursorRow, cursorCol);
            case DRAW -> {
                renderTable(table,0,0);
                renderDraw();
            }
            case X_WINS ->
            {
                renderTable(table,0,0);
                renderWin(CellVO.Value.X);
            }
            case O_WINS ->
            {
                renderTable(table,0,0);
                renderWin(CellVO.Value.O);
            }
        }
    }

    //döntetlen felirat a tábla layer felé
    private void renderDraw() {
        TextGraphics g = screen.newTextGraphics();

        String message = "Az eredmény döntetlen! Nyomjon Enter-t a kilépéshez!";

        TerminalSize size = screen.getTerminalSize();
        int x = (size.getColumns() - message.length()) / 2;
        int y = 0; //centralizált pozicio a terminal screenből

        g.putString(x, y, message);

        try {
            screen.refresh();
        } catch (Exception ignored) {}
    }

    //nyertes felirat a tábla layer felé
    private void renderWin(CellVO.Value winner) {
        TextGraphics g = screen.newTextGraphics();

        String message =
                "A meccset nyerte az " + winner + "! Nyomjon Enter-t a kilépéshez!";

        TerminalSize size = screen.getTerminalSize();
        int x = (size.getColumns() - message.length()) / 2;
        int y = 0; //centralizált pozicio a terminal screenből

        g.putString(x, y, message);

        try {
            screen.refresh();
        } catch (Exception ignored) {}
    }

    public Screen getScreen() {
        return screen;
    }

    /**
     * Stops the Lanterna screen and releases the terminal
     */
    public void close() {
        try {
            screen.stopScreen();
        } catch (Exception ignored) {}
    }
}