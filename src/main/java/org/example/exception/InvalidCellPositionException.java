package org.example.exception;

public class InvalidCellPositionException extends RuntimeException {
    public InvalidCellPositionException(int row, int col) {
        super("Invalid cell position: (" + row + ", " + col + ")");
    }
}

