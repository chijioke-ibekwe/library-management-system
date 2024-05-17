package dev.chijiokeibekwe.librarymanagementsystem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BorrowingRecordStatus {
    OPEN("open"), CLOSED("closed");

    private final String value;

    BorrowingRecordStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
