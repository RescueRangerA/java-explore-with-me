package ru.practicum.ewm.exception;

import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventParticipationRequest;
import ru.practicum.ewm.model.User;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }

    public static AccessDeniedException ofEventAndUser(Event event, User user) {
        return new AccessDeniedException(
                String.format("Access denied to event with id=%d for user with id=%d", event.getId(), user.getId())
        );
    }

    public static AccessDeniedException ofEventRequestAndUser(EventParticipationRequest request, User user) {
        return new AccessDeniedException(
                String.format("Access denied to request with id=%d for user with id=%d", request.getId(), user.getId())
        );
    }
}
