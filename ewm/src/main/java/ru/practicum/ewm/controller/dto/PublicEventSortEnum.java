package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets PublicEventSortEnum
 */

public enum PublicEventSortEnum {

    EVENT_DATE("EVENT_DATE"),

    VIEWS("VIEWS"),

    RATINGS("RATINGS");

    private String value;

    PublicEventSortEnum(String value) {
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
    public static PublicEventSortEnum fromValue(String value) {
        for (PublicEventSortEnum b : PublicEventSortEnum.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}

