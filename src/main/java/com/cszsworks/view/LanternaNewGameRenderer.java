package com.cszsworks.view;

import com.cszsworks.util.ThemeManager;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;

public class LanternaNewGameRenderer {
    private final Screen screen;
    private final MultiWindowTextGUI gui;

    private TextBox sizeBox;
    private TextBox winBox;


    public LanternaNewGameRenderer(Screen screen) {
        this.screen = screen;
        this.gui = new MultiWindowTextGUI(screen);
    }

    public void render() {
        gui.setTheme(ThemeManager.getTheme());
        BasicWindow window = new BasicWindow("New Game Setup");

        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));

        panel.addComponent(new Label("Table Size(4-25):"));
        sizeBox = new TextBox(new TerminalSize(5, 1));
        sizeBox.setText("10");
        panel.addComponent(sizeBox);


        panel.addComponent(new Label("Win Length:"));
        winBox = new TextBox(new TerminalSize(5, 1));
        winBox.setText("5");
        panel.addComponent(winBox);

        //a gombnak egy név , és egy futtatható akcio kell amire listenert csinál
        Button startButton = new Button("Start Game", window::close);
        panel.addComponent(startButton);

        window.setComponent(panel);
        window.setHints(java.util.Arrays.asList(Window.Hint.CENTERED));
        gui.addWindowAndWait(window);
    }

    public String getSizeText() {
        return sizeBox.getText();
    }


    public String getWinLengthText() {
        return winBox.getText();
    }
}
