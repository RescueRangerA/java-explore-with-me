package ru.practicum.ewm.repository.eventpaticipation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventParticipationRequest;
import ru.practicum.ewm.model.User;

import java.util.List;
import java.util.Optional;

public interface EventParticipationRequestRepository extends JpaRepository<EventParticipationRequest, Long> {
    List<EventParticipationRequest> findAllByCreator(User creator);

    List<EventParticipationRequest> findAllByEvent(Event event);

    Optional<EventParticipationRequest> findByCreatorAndEvent(User creator, Event event);

    List<EventParticipationRequest> findAllByIdIn(List<Long> ids);

    Boolean existsByEventAndCreator(Event event, User creator);
}
