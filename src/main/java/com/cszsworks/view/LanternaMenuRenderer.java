package com.cszsworks.view;

import com.cszsworks.util.ThemeManager;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.bundle.LanternaThemes;
import com.googlecode.lanterna.graphics.DefaultMutableThemeStyle;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.Theme;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;

import java.util.List;

public class LanternaMenuRenderer {


    private final MultiWindowTextGUI gui;
    private BasicWindow window;

    private int selectedIndex = -1;

    public LanternaMenuRenderer(Screen screen) {
        //A screen-re húz egy GUI-t
        this.gui = new MultiWindowTextGUI(screen);
    }

    //menu renderelése és a kiválaszott int index visszaadása
    public int renderMenu(String title, String[] options, int initialSelection) {
        gui.setTheme(ThemeManager.getTheme()); //az ui-hoz adom a kiválasztott theme-t
        window = new BasicWindow(title);
        //setHints : ablakviselkedési metódusok
        window.setHints(List.of(Window.Hint.CENTERED));

        Panel panel = new Panel();
        //preferred size megengedi az automatikus ujraméretezést később
        panel.setPreferredSize(new TerminalSize(44, 16));
        panel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        //loop ami minden option után hozzáad egy gombot a létrehozott panelhez
        for (int i = 0; i < options.length; i++) {
            final int index = i;
            //gomb neve a listából, és funkcioja függvényként (listener)
            Button button = new Button(options[i], () -> {
                selectedIndex = index;
                window.close();
            });
            TerminalSize buttonSize = new TerminalSize(30, 4);
            button.setPreferredSize(buttonSize);
            //layout data szerint hozzáad
            panel.addComponent(button, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

            // restore previously selected item
            if (i == initialSelection) {
                window.setFocusedInteractable(button);
            }
        }

        window.setComponent(panel);

        gui.addWindowAndWait(window);
        //visszaad a controllernek egy számot, a kiválaszott menu alapján
        return selectedIndex;
    }

    public void close() {
        if (window != null) {
            window.close();
        }
    }
}
