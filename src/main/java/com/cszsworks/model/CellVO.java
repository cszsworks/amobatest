package com.cszsworks.model;

import java.util.Objects;

public final class CellVO {

    public enum Value {
        X,
        O,
        EMPTY
    }

    private final int row;
    private final int col;
    private final Value value;

    public CellVO(int row, int col) {
        this(row, col, Value.EMPTY);
    }

    public CellVO(int row, int col, Value value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public Value getValue() {
        return value;
    }

    public CellVO withValue(Value newValue) {
        return new CellVO(row, col, newValue);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "CellVO{" +
                "row=" + row +
                ", col=" + col +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CellVO)) return false;
        CellVO cell = (CellVO) o;
        return row == cell.row && col == cell.col && value == cell.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, value);
    }
}
