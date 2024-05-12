package dev.chijiokeibekwe.librarymanagementsystem.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleName {
    ROLE_USER ("role_user");

    private final String value;

    RoleName(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
