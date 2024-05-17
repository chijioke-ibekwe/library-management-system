package dev.chijiokeibekwe.librarymanagementsystem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BookStatus {
    AVAILABLE("available"), UNAVAILABLE("unavailable");

    private final String value;

    BookStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
