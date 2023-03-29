package ru.practicum.ewm.repository.eventreaction;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.EventReaction;
import ru.practicum.ewm.model.EventReactionID;

public interface EventReactionRepository extends JpaRepository<EventReaction, EventReactionID>, CustomEventReactionRepository {
    @Override
    boolean existsById(EventReactionID eventReactionID);
}
