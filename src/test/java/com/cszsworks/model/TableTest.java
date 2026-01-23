package com.cszsworks.model;

import com.cszsworks.exception.InvalidCellPositionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TableTest {

    @Test
    void testInitialization() throws InvalidCellPositionException {
        Table table = new Table(3, 3, 3);

        assertEquals(3, table.getRows());
        assertEquals(3, table.getCols());

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                assertEquals(CellVO.Value.EMPTY, table.getCell(r, c).value());
            }
        }
    }

    @Test
    void testCheckRowWin()  {
        Table table = new Table(3,3,3);
        table.setCell(0,0, CellVO.Value.X);
        table.setCell(0,1, CellVO.Value.X);
        table.setCell(0,2, CellVO.Value.X);

        assertEquals(CellVO.Value.X, table.checkWinner());
    }

    @Test
    void testCheckColWin()  {
        Table table = new Table(3,3,3);
        table.setCell(0,0, CellVO.Value.O);
        table.setCell(1,0, CellVO.Value.O);
        table.setCell(2,0, CellVO.Value.O);

        assertEquals(CellVO.Value.O, table.checkWinner());
    }

    @Test
    void testCheckSEDiagonalWin() {
        Table table = new Table(3,3,3);
        table.setCell(0,0, CellVO.Value.X);
        table.setCell(1,1, CellVO.Value.X);
        table.setCell(2,2, CellVO.Value.X);

        assertEquals(CellVO.Value.X, table.checkWinner());
    }

    @Test
    void testCheckSWDiagonalWin(){
        Table table = new Table(3,3,3);
        table.setCell(0,2, CellVO.Value.O);
        table.setCell(1,1, CellVO.Value.O);
        table.setCell(2,0, CellVO.Value.O);

        assertEquals(CellVO.Value.O, table.checkWinner());
    }

    @Test
    void testNoWinner() {
        Table table = new Table(3,3,3);
        table.setCell(0,0, CellVO.Value.X);
        table.setCell(0,1, CellVO.Value.O);
        table.setCell(0,2, CellVO.Value.X);
        table.setCell(1,0, CellVO.Value.O);

        assertEquals(CellVO.Value.EMPTY, table.checkWinner());
    }

    @Test
    void testIsBoardFull() {
        Table table = new Table(2,2,2);
        assertFalse(table.isBoardFull());

        table.setCell(0,0, CellVO.Value.X);
        table.setCell(0,1, CellVO.Value.O);
        table.setCell(1,0, CellVO.Value.O);
        table.setCell(1,1, CellVO.Value.X);

        assertTrue(table.isBoardFull());
    }

    @Test
    void testIsConnectedToExisting() {
        Table table = new Table(3,3,3);

        // üres tábla
        assertFalse(table.isConnectedToExisting(1,1));

        // 1,0 az 1,1 szomszédja
        table.setCell(1,0, CellVO.Value.X);
        assertTrue(table.isConnectedToExisting(1,1));

        // önmaga nem számít bele teszt
        table.setCell(0,0, CellVO.Value.O);
        assertFalse(table.isConnectedToExisting(0,0));
    }

    @Test
    void testGetCellOutOfBounds() {
        Table table = new Table(2,2,2);
        assertThrows(InvalidCellPositionException.class, () -> table.getCell(-1,0));
        assertThrows(InvalidCellPositionException.class, () -> table.getCell(0,2));
    }

    @Test
    void testSetCellOutOfBounds() {
        Table table = new Table(2,2,2);
        assertThrows(InvalidCellPositionException.class, () -> table.setCell(-1,0, CellVO.Value.X));
        assertThrows(InvalidCellPositionException.class, () -> table.setCell(0,2, CellVO.Value.X));
    }

    @Test
    void testSetCellUpdatesValue() {
        Table table = new Table(2,2,2);
        table.setCell(0,0, CellVO.Value.X);
        assertEquals(CellVO.Value.X, table.getCell(0,0).value());
    }
}
