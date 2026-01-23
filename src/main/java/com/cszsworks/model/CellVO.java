package com.cszsworks.model;

import java.io.Serial;
import java.io.Serializable;

//recordként a default toString() and equals() methodok automatikusan overrideolva
public record CellVO(int row, int col, Value value) implements Serializable {
    //bytestreamként menthető, uid gen
    @Serial
    private static final long serialVersionUID = 1L;

    public enum Value {
        X,
        O,
        EMPTY
    }

    public CellVO(int row, int col) {
        this(row, col, Value.EMPTY);
    }

    public CellVO withValue(Value newValue) {
        return new CellVO(row, col, newValue);
    }

}
