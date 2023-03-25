package ru.practicum.ewm.repository.eventcategory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.EventCategory;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
}
