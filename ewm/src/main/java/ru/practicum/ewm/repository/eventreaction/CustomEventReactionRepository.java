package ru.practicum.ewm.repository.eventreaction;

import ru.practicum.ewm.model.Event;

import java.util.List;
import java.util.Map;

public interface CustomEventReactionRepository {
    Long calculateEventRating(Event event);

    Map<Long, Long> calculateEventsRating(List<Long> ids);
}
