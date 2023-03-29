package ru.practicum.ewm.repository.eventreaction;

import lombok.*;
import ru.practicum.ewm.model.Event;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventWithRating {
    private Event event;

    private Long rating = 0L;
}
