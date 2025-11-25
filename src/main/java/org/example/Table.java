package org.example;
import org.example.exception.InvalidCellPositionException;

public class Table {

    private final int rows;
    private final int cols;
    private final CellVO[][] cells;
    private final int winLength;

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
    public void setCell(int row, int col, CellVO.Value newValue) {
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
        return null;
    }
}
