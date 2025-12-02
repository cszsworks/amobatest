package org.example;
import org.example.exception.InvalidCellPositionException;

public class Table {
    private CellVO.Value winState;
    private final int rows;
    private final int cols;
    private final CellVO[][] cells;
    private final int winLength;


    /* ======= Win Check Logic ======= */

    //inkabb void, es winstatet modosit
    private CellVO.Value checkRow() {
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
                    return checkAgainst;
                }
            }
        }
        return CellVO.Value.EMPTY;
    }



    private CellVO.Value checkCol() {

        for (int i = 0; i < cols; i++) {
            int sameCount = 0;
            CellVO.Value checkAgainst = CellVO.Value.EMPTY;

            for (int j = 0; j < rows; j++) {
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
                    return checkAgainst;
                }
            }
        }

        return CellVO.Value.EMPTY;

    }
    private CellVO.Value checkSEDiagonal() {

        return null;
    }
    private CellVO.Value checkSWDiagonal() {
        return null;
    }
    /* =============================== */

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
