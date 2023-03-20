package ru.practicum.ewm;

import java.time.LocalDateTime;

public class EventDateValidator {

    public boolean isValidCreatedAndStarted(LocalDateTime createdOn, LocalDateTime startedOn) {
        if (createdOn == null || startedOn == null) {
            return false;
        }

        LocalDateTime thresholdOn = createdOn.plusHours(2);

        return thresholdOn.isEqual(startedOn) || thresholdOn.isBefore(startedOn);
    }

    public boolean isValidPublishedAndStarted(LocalDateTime publishedOn, LocalDateTime startedOn) {
        if (publishedOn == null || startedOn == null) {
            return false;
        }

        LocalDateTime thresholdOn = publishedOn.plusHours(1);

        return thresholdOn.isEqual(startedOn) || thresholdOn.isBefore(startedOn);
    }
}
