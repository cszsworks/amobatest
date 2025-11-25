package org.example;
import org.example.exception.InvalidCellPositionException;

public class Table {
    private CellVO.Value winState = CellVO.Value.EMPTY;
    private final int rows;
    private final int cols;
    private final CellVO[][] cells;
    private final int winLength;


    /* ======= Win Check Logic ======= */

    //inkabb void, es winstatet modosit
    private void checkRow() {
        CellVO.Value winner = CellVO.Value.EMPTY;
        outer:
        for (int i = 0; i < rows; i++) {
            int sameCount = 0;
            CellVO.Value checkAgainst = CellVO.Value.EMPTY;

            for (int j = 0; j < cols; j++) {
                CellVO.Value cur = cells[i][j].getValue();

                if (cur == CellVO.Value.EMPTY) {  //üres cellát nem vizsgál
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
                    winner = checkAgainst;
                    break outer;
                }
            }
        }

        this.winState = winner;
    }

    private CellVO.Value checkCol() {
        return null;

    }
    private CellVO.Value checkMainDiagonal() {
        return null;
    }
    private CellVO.Value checkAntiDiagonal() {
        return null;
    }
    /* =============================== */

    public Table(int rows, int cols, int winLength) {
        this.rows = rows;
        this.cols = cols;
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

    public void printTable() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                System.out.print(cells[r][c].getValue() + " ");
            }
            System.out.println();
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public CellVO.Value checkWinner()
    {
        this.checkRow();
        return this.winState;
    }
}
