package com.cszsworks.view;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;


// sarkok a széleken ANSI ┌ ┐ └ ┘
// kapcsolatok a cellák között ┬ ┴
//keretet rajzol cellák köré

//a Cellázott tábla rajzolásának forrása javarészt egy stackoverflow kérdés
public class CellBorderRenderer {

    // cella méret borderrel együtt
    public static final int CELL_WIDTH = 3;


    public void drawCell(TextGraphics g, int x, int y, String content, boolean selected) {

        // Ha a kurzor ezen a cellán áll, akkor kiemeljük
        if (selected) {
            g.setBackgroundColor(TextColor.ANSI.WHITE);
            g.setForegroundColor(TextColor.ANSI.BLACK);
        } else {
            g.setBackgroundColor(TextColor.ANSI.DEFAULT);
            g.setForegroundColor(TextColor.ANSI.DEFAULT);
        }

        // bal keret
        g.putString(x, y, "│");
        //jobb keret, cellaszélesség + 1
        g.putString(x + CELL_WIDTH + 1, y, "│");

        // a cella tartalmát középre igazitja
        g.putString(x + 1, y, " " + content + " ");

        //SZINEKET VISSZA HOGY NE MARADJON ÚGY !!!!!
        g.setBackgroundColor(TextColor.ANSI.DEFAULT);
        g.setForegroundColor(TextColor.ANSI.DEFAULT);
    }

    //felső keret ┌───┬───┬───┐
    public void drawHorizontalBorder(TextGraphics g, int x, int y, int cellCount) {
        //bal felső sarok
        g.putString(x, y, "┌");
        // cellák felső szakasza
        for (int i = 0; i < cellCount; i++) {
            // ez a ─── rajzolja
            g.putString(x + 1 + i * (CELL_WIDTH + 2), y, "───");

            //ha cellák között vagyunk,  (tehát ha nem utolsó)!!
            if (i < cellCount - 1) {
                g.putString(x + 4 + i * (CELL_WIDTH + 2), y, "┬");
            }
        }
        //jobb felső sarok
        g.putString(x + cellCount * (CELL_WIDTH + 2), y, "┐");
    }

    //alsó keret └───┴───┴───┘
    public void drawBottomBorder(TextGraphics g, int x, int y, int cellCount
    ) {
        //bal alsó sarok
        g.putString(x, y, "└");

        //cellák alsó szakaszai
        for (int i = 0; i < cellCount; i++) {
            g.putString(x + 1 + i * (CELL_WIDTH + 2), y, "───");

            //HA CELLÁK KÖZÖTT VAGYUNK (tehát nem utolsó)
            if (i < cellCount - 1) {
                g.putString(x + 4 + i * (CELL_WIDTH + 2), y, "┴");
            }
        }
        //jobb alsó sarok
        g.putString(x + cellCount * (CELL_WIDTH + 2), y, "┘");
    }
}
