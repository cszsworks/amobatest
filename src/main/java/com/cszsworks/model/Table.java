package com.cszsworks.model;
import com.cszsworks.exception.InvalidCellPositionException;

import java.io.Serial;
import java.io.Serializable;

public class Table implements Serializable {
    //bytestreamként menthető, uid gen
    @Serial
    private static final long serialVersionUID = 1L;


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
                CellVO.Value cur = cells[i][j].value();

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
                    CellVO.Value cur = cells[row][col].value(); // row first, col second

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
        int size = rows; //sorméret
        //megkeres minden lehetséges kezdőpontot ahol még elfér a winLength hossz átlóban
        for (int rowStart = 0; rowStart <= size - winLength; rowStart++) {
            for (int colStart = 0; colStart <= size - winLength; colStart++) {

                int sameCount = 0;
                CellVO.Value checkAgainst = CellVO.Value.EMPTY;

                for (int k = 0; k < winLength; k++) {
                    // +k minden esetben, átlósan ugrálunk délkelet felé 1esével
                    CellVO.Value cur = cells[rowStart + k][colStart + k].value();

                    if (cur == CellVO.Value.EMPTY) {
                        break; // ha van üres mező nem nyerhet
                    }

                    if (cur == checkAgainst) {
                        sameCount++; //ha egyezést talál , növeli a samecountoit
                    } else {
                        checkAgainst = cur; //ha nem talál egyezést, reseteli a jelölőt
                        sameCount = 1; //ha nem talál egyezést, reseteli a samecounto
                    }

                    if (sameCount == winLength) {
                        return checkAgainst; //ha a samecount eléri a nyerési hosszt, visszaadja a nyertes jelet
                    }
                }
            }
        }

        return CellVO.Value.EMPTY;
    }

            //delnyugat atlo csekk
    private CellVO.Value checkSWDiagonal() {
        int size = rows;
        //sorméret
        //megkeres minden lehetséges kezdőpontot ahol még elfér a winLength hossz átlóban
        for (int rowStart = 0; rowStart <= size - winLength; rowStart++) {
            for (int colStart = winLength - 1; colStart < size; colStart++) {

                int sameCount = 0;

                CellVO.Value checkAgainst = CellVO.Value.EMPTY;
                // +k és -k minden esetben, átlósan ugrálunk délnyugat felé 1esével
                for (int k = 0; k < winLength; k++) {
                    CellVO.Value cur = cells[rowStart + k][colStart - k].value();

                    if (cur == CellVO.Value.EMPTY) {
                        break; //nem nyerhet
                    }

                    if (cur == checkAgainst) {
                        //ha egyezést talál , növeli a samecountoit
                        sameCount++;
                    } else {
                        checkAgainst = cur; //ha nem talál egyezést, reseteli a jelölőt
                        sameCount = 1; //ha nem talál egyezést, reseteli a samecounto
                    }

                    if (sameCount == winLength) {
                        return checkAgainst; //ha a samecount eléri a nyerési hosszt, visszaadja a nyertes jelet
                    }
                }
            }
        }

        return CellVO.Value.EMPTY;
    }
    // =============================================== //

    public Table(int rows, int cols, int winLength) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new CellVO[rows][cols];
        this.winLength = winLength;
        initializeCells();
    }
    //minden cellához új CellVO-t generál
    private void initializeCells() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new CellVO(i, j);
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
                if(cells[i][j].value() == CellVO.Value.EMPTY)
                {
                    return false;
                }
            }
        }
        return true;
    }

    public CellVO.Value checkWinner()
    {
        CellVO.Value w; //default empty

        w = checkRow();
        if (w != CellVO.Value.EMPTY) return w;

        w = checkCol();
        if (w != CellVO.Value.EMPTY) return w;

        w = checkSEDiagonal();
        if (w != CellVO.Value.EMPTY) return w;

        w = checkSWDiagonal();
        return w;
    }
    //segítő method , ellenőrzi hogy a szomszédos mezőben van-e már jel
    // (row-1,col-1)  (row-1,col)  (row-1,col+1)
    // (row  ,col-1)  (row  ,col)  (row  ,col+1)
    // (row+1,col-1)  (row+1,col)  (row+1,col+1)
    public boolean isConnectedToExisting(int row, int col) {
        // Ha a cella nem üres, nem használható
        if (cells[row][col].value() != CellVO.Value.EMPTY) {
            return false;
        }

        // Szomszédokon keresztül loop
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                //önmagát kihagy
                if (i == 0 && j == 0) continue;
                //loop helyzeti szerint, 1-el csökkenti , meghagyja 0val, vagy 1el növeli az oszlopot/sort
                int newRow = row + i;
                int newCol = col + j;

                // határok
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                    if (cells[newRow][newCol].value() != CellVO.Value.EMPTY) {
                        return true;  // talált egy csatlakozót
                    }
                }
            }
        }

        // Nincs szomszéd ami nem üres
        return false;
    }

}
