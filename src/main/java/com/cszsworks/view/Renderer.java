package com.cszsworks.view;

import com.cszsworks.model.CellVO;
import com.cszsworks.model.Table;

public class Renderer {

    public Renderer() {

    }

    public void render(Table table) {
        for (int i = 0; i < table.getRows(); i++) {
            for (int j = 0; j < table.getCols(); j++) {
                CellVO.Value v = table.getCell(i, j).getValue();
                System.out.print(v == CellVO.Value.EMPTY ? "." : v);
                System.out.print(" ");
            }
            System.out.println();
        }
    }



}
