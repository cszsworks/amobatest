package com.cszsworks.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;

public class StatusBarRenderer {
    private final Screen screen;

    public StatusBarRenderer(Screen screen) {
        this.screen = screen;
    }

    public void renderTitle(String title) throws Exception {
        TextGraphics g = screen.newTextGraphics();
        g.putString(0, 0, title);
        screen.refresh();
    }

    public void renderStatus(String status) throws Exception {
        TextGraphics g = screen.newTextGraphics();
        TerminalSize size = screen.getTerminalSize();
        g.putString(0, 1, status);
        screen.refresh();
    }
}

