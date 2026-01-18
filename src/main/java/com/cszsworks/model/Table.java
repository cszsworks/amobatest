package com.cszsworks.model;
import com.cszsworks.exception.InvalidCellPositionException;

import java.io.Serializable;

public class Table implements Serializable {
    //bytestreamként menthető, uid gen
    private static final long serialVersionUID = 1L;

    private CellVO.Value winState;
    private final int rows;
    private final int cols;
    private final CellVO[][] cells;
    private final int winLength;


    //                NYERTESKERESÉS              //


        //sorcsekk
    private CellVO.Value checkRow() {
        for (int i = 0; i < rows; i++) {
            int sameCount = 0;
            CellVO.Value checkAgainst = CellVO.Value.EMPTY;


            for (int j = 0; j < cols; j++) {
                CellVO.Value cur = cells[i][j].getValue();

                if (cur == CellVO.Value.EMPTY) {  // skip empty cells
                    sameCount = 0;
                    checkAgainst = CellVO.Value.EMPTY;
                    continue;
                }

                if (cur == checkAgainst) {
                    sameCount++;
                } else {
                    checkAgainst = cur;
                    sameCount = 1;
                }

                if (sameCount == winLength) {
                    return checkAgainst;  // winner found
                }
            }
        }

        return CellVO.Value.EMPTY;  // no winner in any row
    }


        //oszlopcsekk
        private CellVO.Value checkCol() {
            for (int col = 0; col < cols; col++) {
                int sameCount = 0;
                CellVO.Value checkAgainst = CellVO.Value.EMPTY;

                for (int row = 0; row < rows; row++) {
                    CellVO.Value cur = cells[row][col].getValue(); // row first, col second

                    if (cur == CellVO.Value.EMPTY) {
                        sameCount = 0;
                        checkAgainst = CellVO.Value.EMPTY;
                        continue;
                    }

                    if (cur == checkAgainst) {
                        sameCount++;
                    } else {
                        checkAgainst = cur;
                        sameCount = 1;
                    }

                    if (sameCount == winLength) {
                        return checkAgainst;
                    }
                }
            }

            return CellVO.Value.EMPTY;
        }


            //delkelet atlo csekk
    private CellVO.Value checkSEDiagonal() {
        int size = rows;
        for (int rowStart = 0; rowStart <= size - winLength; rowStart++) {
            for (int colStart = 0; colStart <= size - winLength; colStart++) {

                int sameCount = 0;
                CellVO.Value checkAgainst = CellVO.Value.EMPTY;

                for (int k = 0; k < winLength; k++) {
                    CellVO.Value cur = cells[rowStart + k][colStart + k].getValue();

                    if (cur == CellVO.Value.EMPTY) {
                        sameCount = 0;
                        checkAgainst = CellVO.Value.EMPTY;
                        break; // nem nyerhet
                    }

                    if (cur == checkAgainst) {
                        sameCount++;
                    } else {
                        checkAgainst = cur;
                        sameCount = 1;
                    }

                    if (sameCount == winLength) {
                        return checkAgainst;
                    }
                }
            }
        }

        return CellVO.Value.EMPTY;
    }

            //delnyugat atlo csekk
    private CellVO.Value checkSWDiagonal() {
        int size = rows;


        for (int rowStart = 0; rowStart <= size - winLength; rowStart++) {
            for (int colStart = winLength - 1; colStart < size; colStart++) {

                int sameCount = 0;
                CellVO.Value checkAgainst = CellVO.Value.EMPTY;

                for (int k = 0; k < winLength; k++) {
                    CellVO.Value cur = cells[rowStart + k][colStart - k].getValue();

                    if (cur == CellVO.Value.EMPTY) {
                        sameCount = 0;
                        checkAgainst = CellVO.Value.EMPTY;
                        break; //nem nyerhet
                    }

                    if (cur == checkAgainst) {
                        sameCount++;
                    } else {
                        checkAgainst = cur;
                        sameCount = 1;
                    }

                    if (sameCount == winLength) {
                        return checkAgainst;
                    }
                }
            }
        }

        return CellVO.Value.EMPTY;
    }
    // =============================== //

    public Table(int rows, int cols, int winLength) {
        this.rows = rows;
        this.cols = cols;
        this.winState = CellVO.Value.EMPTY;
        this.cells = new CellVO[rows][cols];
        this.winLength = winLength;
        initializeCells();
    }

    private void initializeCells() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new CellVO(r, c);
            }
        }
    }

    public CellVO getCell(int row, int col) throws InvalidCellPositionException{
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new InvalidCellPositionException(row,col);
        }
        return cells[row][col];
    }
    //Mar meglevö cella átállítása
    public void setCell(int row, int col, CellVO.Value newValue) throws InvalidCellPositionException{
        if(row < 0 || row >= rows || col < 0 || col >= cols)
        {
            throw new InvalidCellPositionException(row,col);
        }
        cells[row][col] = cells[row][col].withValue(newValue);
    }


    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    //ha tele, sehol nem empty ->true
    public boolean isBoardFull(){
        for (int i = 0; i<rows; i++)
        {
            for (int j = 0; j<cols; j++)
            {
                if(cells[i][j].getValue() == CellVO.Value.EMPTY)
                {
                    return false;
                }
            }
        }
        return true;
    }
    public CellVO.Value checkWinner()
    {
        CellVO.Value w = CellVO.Value.EMPTY;

        w = checkRow();
        if (w != CellVO.Value.EMPTY) return w;

        w = checkCol();
        if (w != CellVO.Value.EMPTY) return w;

        w = checkSEDiagonal();
        if (w != CellVO.Value.EMPTY) return w;

        w = checkSWDiagonal();
        return w;
    }
}
