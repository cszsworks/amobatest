package com.cszsworks.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.InputProvider;
import com.googlecode.lanterna.screen.Screen;

public class LanternaMenuRenderer {
    private final Screen screen;

    public LanternaMenuRenderer(Screen screen) {
        this.screen = screen;
    }

    public void renderMenu(String[] options, int selectedIndex) throws Exception {
        screen.clear();
        TextGraphics g = screen.newTextGraphics();

        TerminalSize size = screen.getTerminalSize();
        int startY = 5;
        for (int i = 0; i < options.length; i++) {
            if (i == selectedIndex) {
                g.setBackgroundColor(TextColor.ANSI.WHITE);
                g.setForegroundColor(TextColor.ANSI.BLACK);
            } else {
                g.setBackgroundColor(TextColor.ANSI.DEFAULT);
                g.setForegroundColor(TextColor.ANSI.DEFAULT);
            }
            int x = (size.getColumns() - options[i].length()) / 2;
            g.putString(x, startY + i, options[i]);
        }

        screen.refresh();
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
