package dev.chijiokeibekwe.librarymanagementsystem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {
    USER("user");

    private final String value;

    UserType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
