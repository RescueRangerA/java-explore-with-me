package ru.practicum.ewm.exception.eventreaction;

import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventParticipationRequest;
import ru.practicum.ewm.model.User;

public class EventReactionMutationViolationException extends RuntimeException {
    protected EventReactionMutationViolationException() {
        super();
    }

    protected EventReactionMutationViolationException(String message) {
        super(message);
    }

    public static EventReactionMutationViolationException ofIncorrectEventStatus(Event event) {
        return new EventReactionMutationViolationException(
                String.format(
                        "Cannot persist the event like because the event with id=%d not in the right state: %s",
                        event.getId(),
                        event.getStatus()
                )
        );
    }

    public static EventReactionMutationViolationException ofMissingParticipationRequest(Event event, User user) {
        return new EventReactionMutationViolationException(
                String.format(
                        "Cannot persist the event like because the user with id=%d does not have the request to event with id=%d",
                        user.getId(),
                        event.getId()
                )
        );
    }

    public static EventReactionMutationViolationException ofParticipationRequestIncorrectStatus(
            EventParticipationRequest participationRequest,
            Event event,
            User user
    ) {
        return new EventReactionMutationViolationException(
                String.format(
                        "Cannot persist the event like because participation request of the user with id=%d to event with id=%d is in incorrect state: %s",
                        user.getId(),
                        event.getId(),
                        participationRequest.getStatus()
                )
        );
    }
}
