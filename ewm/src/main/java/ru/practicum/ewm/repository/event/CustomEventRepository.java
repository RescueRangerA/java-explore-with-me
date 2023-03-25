package ru.practicum.ewm.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.model.Event;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomEventRepository {
    Optional<EventWithCount> findByIdAndAppendConfirmedParticipationCount(Long id);

    Optional<EventWithCount> findByIdWithParticipationCount(Long id, Specification<Event> spec);

    Map<Long, EventWithCount> findAllByIdsAndAppendConfirmedParticipationCount(List<Long> ids);

    Page<EventWithCount> findAllWithParticipationCount(Boolean onlyAvailable, Specification<Event> spec, Pageable pageable);
}
