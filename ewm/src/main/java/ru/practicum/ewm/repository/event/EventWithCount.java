package ru.practicum.ewm.repository.event;

import lombok.*;
import ru.practicum.ewm.model.Event;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EventWithCount {
    private Event event;

    private Long participationCount;
}
