package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets EventStatusRequestEnum
 */

public enum EventStatusRequestEnum {

    PENDING("PENDING"),

    PUBLISHED("PUBLISHED"),

    CANCELED("CANCELED");

    private String value;

    EventStatusRequestEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static EventStatusRequestEnum fromValue(String value) {
        for (EventStatusRequestEnum b : EventStatusRequestEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

