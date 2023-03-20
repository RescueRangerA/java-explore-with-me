package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Generated;

/**
 * Gets or Sets PublicEventSortEnum
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-03-20T18:18:39.930348+04:00[Europe/Saratov]")
public enum PublicEventSortEnum {

    EVENT_DATE("EVENT_DATE"),

    VIEWS("VIEWS");

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

