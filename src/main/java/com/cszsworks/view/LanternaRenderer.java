package com.cszsworks.view;

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


    /**
     * Draws the table to the terminal, highlighting the selected cell
     *
     * @param table     the game table
     * @param cursorRow row index of the cursor
     * @param cursorCol column index of the cursor
     */
    public void render(Table table, int cursorRow, int cursorCol) {
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