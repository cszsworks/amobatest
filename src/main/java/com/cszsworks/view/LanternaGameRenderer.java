package com.cszsworks.view;

import com.cszsworks.model.Table;
import com.cszsworks.model.CellVO;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
//tábla, kurzor szinezés, üzenetek, refresh
public class LanternaGameRenderer {

    private final Screen screen;
    private final CellBorderRenderer cellBorderRenderer;

    // swing alapú terminál ablak létrehozáse
    public LanternaGameRenderer(Screen screen) {
        this.screen = screen;
        this.cellBorderRenderer = new CellBorderRenderer();
    }


    //lanterna renderTable, cellák , keretek kirazjolasa
    public void renderTable(Table table, int cursorRow, int cursorCol) {
        screen.clear();
        TextGraphics g = screen.newTextGraphics();

        int rows = table.getRows();
        int cols = table.getCols();
        //ez lesz a cella teljes szélesség, borderrel és kerettel
        int cellBlockWidth = cols * (CellBorderRenderer.CELL_WIDTH + 2) + 1;

        //vizszintes középre
        int startX = (screen.getTerminalSize().getColumns() - cellBlockWidth) / 2;
        //függőleges középre (soronként 2 magas)
        int startY = (screen.getTerminalSize().getRows() - rows * 2) / 2;

        // oszlop számozás felül
        for (int j = 0; j < cols; j++) {
            g.putString(startX + 2 + j * (CellBorderRenderer.CELL_WIDTH + 2), startY - 2, String.valueOf(j + 1)
            );
        }

        //felső vizszintes keret
        cellBorderRenderer.drawHorizontalBorder(g, startX, startY - 1, cols);

        for (int i = 0; i < rows; i++) {
            //soronkénti betü előre
            char rowLabel = (char) ('A' + i % 26); //ez loopol ha hosszabb lenne mint 26.. ( nem lesz..)
            g.putString(startX - 2, startY + i * 2, String.valueOf(rowLabel));

            // CELLÁK RAJZOLÁSA sorokban
            for (int j = 0; j < cols; j++) {
                //Cellából kérem a szimbolumot
                var cell = table.getCell(i, j);
                //ha üres pont, ha nem üres, a jel
                String symbol = cell.value() == CellVO.Value.EMPTY ? "." : cell.value().toString();

                boolean selected = (i == cursorRow && j == cursorCol);

                // erre a koordinátára fog kerülni a cella
                int cellX = startX + j * (CellBorderRenderer.CELL_WIDTH + 2);
                int cellY = startY + i * 2;

                //--------------------CELLAKIRAJZOLÁS
                cellBorderRenderer.drawCell(g, cellX, cellY, symbol, selected);
            }

            // HA UTOLSÓ SOR, ALÁ IS KERÜL VONAL
            if (i == rows - 1) {
                cellBorderRenderer.drawBottomBorder(g, startX, startY + i * 2 + 1, cols);
            }
        }
    }
    //telejs képernyő : resize, tábla , üzenet ,refreshelés!
    public void renderGame(Table table, int cursorRow, int cursorCol, String message) {
        screen.doResizeIfNecessary(); //reagál ha újraméretezés történt, de csak refresh callnál
        renderTable(table, cursorRow, cursorCol);
        // Felső üzenet (pl. hiba, győzelem)
        if (message != null && !message.isEmpty()) {
            TextGraphics g = screen.newTextGraphics();
            int row = 0; // felso sor
            int col = Math.max(0, (screen.getTerminalSize().getColumns() - message.length()) / 2); //math max h ne legyen akadás
            g.putString(col, row, message);
        }
        //Itt a BUFFER teljes tartalma felkerül a terminálra
        try {
            screen.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //a kontroller tudjon inputot olvasni
    public Screen getScreen() {
        return screen;
    }

}
