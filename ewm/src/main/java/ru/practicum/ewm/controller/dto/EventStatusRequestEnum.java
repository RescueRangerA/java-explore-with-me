package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.Generated;

/**
 * Gets or Sets EventStatusRequestEnum
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-03-20T18:18:39.930348+04:00[Europe/Saratov]")
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

