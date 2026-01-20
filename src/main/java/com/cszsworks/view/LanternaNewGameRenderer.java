package com.cszsworks.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;

public class LanternaNewGameRenderer {
    private final Screen screen;
    private final MultiWindowTextGUI gui;

    private TextBox rowsBox;
    private TextBox colsBox;
    private TextBox winBox;


    public LanternaNewGameRenderer(Screen screen) {
        this.screen = screen;
        this.gui = new MultiWindowTextGUI(screen);
    }

    public void render() {
        BasicWindow window = new BasicWindow("New Game Setup");

        Panel panel = new Panel();
        panel.setLayoutManager(new GridLayout(2));

        panel.addComponent(new Label("Rows:"));
        rowsBox = new TextBox(new TerminalSize(5, 1));
        rowsBox.setText("8");
        panel.addComponent(rowsBox);

        panel.addComponent(new Label("Columns:"));
        colsBox = new TextBox(new TerminalSize(5, 1));
        colsBox.setText("8");
        panel.addComponent(colsBox);

        panel.addComponent(new Label("Win Length:"));
        winBox = new TextBox(new TerminalSize(5, 1));
        winBox.setText("4");
        panel.addComponent(winBox);

        Button startButton = new Button("Start Game", window::close);
        panel.addComponent(startButton);

        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }

    public String getRowsText() {
        return rowsBox.getText();
    }

    public String getColsText() {
        return colsBox.getText();
    }

    public String getWinLengthText() {
        return winBox.getText();
    }
}
