package ru.practicum.ewm.exception.request;

import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventParticipationRequestStatus;
import ru.practicum.ewm.model.User;

public class EventRequestMutationViolationException extends RuntimeException {
    protected EventRequestMutationViolationException() {
        super();
    }

    protected EventRequestMutationViolationException(String message) {
        super(message);
    }

    public static EventRequestMutationViolationException ofDuplicatedRequest(Event event, User user) {
        return new EventRequestMutationViolationException(
                String.format(
                        "Cannot persist one more request for event with id=%d and user with id=%d",
                        event.getId(),
                        user.getId()
                )
        );
    }

    public static EventRequestMutationViolationException ofParticipateOnOwnRequest(Event event, User user) {
        return new EventRequestMutationViolationException(
                String.format(
                        "Cannot persist a request because user created the event with id=%d and user with id=%d",
                        event.getId(),
                        user.getId()
                )
        );
    }

    public static EventRequestMutationViolationException ofParticipantLimitExceeded(Event event, User user) {
        return new EventRequestMutationViolationException(
                String.format(
                        "Participation limit has been exceeded for the event with id=%d for user with id=%d",
                        event.getId(),
                        user.getId()
                )
        );
    }

    public static EventRequestMutationViolationException ofNotPublishedEvent(Event event, User user) {
        return new EventRequestMutationViolationException(
                String.format(
                        "Cannot persist a request because event with id=%d is not published of user with id=%d",
                        event.getId(),
                        user.getId()
                )
        );
    }

    public static EventRequestMutationViolationException ofUnchangeableRequestStatus(
            EventParticipationRequestStatus currentStatus,
            EventParticipationRequestStatus correctStatus
    ) {
        return new EventRequestMutationViolationException(
                String.format(
                        "Cannot persist a request because of incorrect status: expected %s, actual %s",
                        correctStatus.name(),
                        currentStatus.name()
                )
        );
    }
}
