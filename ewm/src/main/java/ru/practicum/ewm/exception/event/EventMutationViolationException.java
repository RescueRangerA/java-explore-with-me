package ru.practicum.ewm.exception.event;

import ru.practicum.ewm.model.EventStatus;
import ru.practicum.ewm.model.User;

import java.time.LocalDateTime;

public class EventMutationViolationException extends RuntimeException {
    protected EventMutationViolationException() {
        super();
    }

    protected EventMutationViolationException(String message) {
        super(message);
    }

    public static EventMutationViolationException ofIncorrectStatus(EventStatus status) {
        return new EventMutationViolationException(
                String.format("Cannot persist the event because it's not in the right state: %s", status.name())
        );
    }

    public static EventMutationViolationException ofIncorrectStatusForUser(EventStatus status, User user) {
        return new EventMutationViolationException(
                String.format(
                        "Cannot persist the event because it's not in the right state (%s) for user with id=%d",
                        status.name(),
                        user.getId()
                )
        );
    }

    public static EventMutationViolationException ofIncorrectPublishAndStartDateUpdate(
            LocalDateTime publishedOn,
            LocalDateTime startedOn
    ) {
        return new EventMutationViolationException(
                String.format(
                        "Cannot persist the event because of incorrect publish and start date: %s, %s",
                        publishedOn,
                        startedOn
                )
        );
    }

    public static EventMutationViolationException ofIncorrectCreateAndStartDateCreate(
            LocalDateTime createdOn,
            LocalDateTime startedOn
    ) {
        return new EventMutationViolationException(
                String.format(
                        "Cannot persist an event because of incorrect create and start date: %s, %s",
                        createdOn,
                        startedOn
                )
        );
    }
}
