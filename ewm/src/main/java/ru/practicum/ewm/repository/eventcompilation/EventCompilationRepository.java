package ru.practicum.ewm.repository.eventcompilation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.EventCompilation;

public interface EventCompilationRepository extends JpaRepository<EventCompilation, Long> {
    Page<EventCompilation> findAllByPinnedIs(Boolean pinned, Pageable pageable);
}
