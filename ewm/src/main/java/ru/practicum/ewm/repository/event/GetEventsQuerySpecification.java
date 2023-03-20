package ru.practicum.ewm.repository.event;

import lombok.Builder;
import org.springframework.data.jpa.domain.Specification;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventStatus;
import ru.practicum.ewm.model.Event_;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Builder
public class GetEventsQuerySpecification {
    private String text;

    private List<Long> categoryIds;

    private List<Long> userIds;

    private List<EventStatus> statuses;

    private Boolean paid;

    private LocalDateTime rangeStart;

    private LocalDateTime rangeEnd;

    public Specification<Event> makeSpecification() {
        return Specification
                .where(eventAnnotationOrDescriptionContains(text))
                .and(eventHasOneOfCategory(categoryIds))
                .and(eventIsPaid(paid))
                .and(eventStartIsBetween(rangeStart, rangeEnd))
                .and(eventCreatorIsIn(userIds))
                .and(eventStatusIsIn(statuses));
    }

    private static Specification<Event> eventHasOneOfCategory(Collection<Long> ids) {
        return (root, query, builder) -> {
            if (ids == null || ids.size() == 0) {
                return builder.and();
            }

            return root.get(Event_.CATEGORY).in(ids);
        };
    }

    private static Specification<Event> eventCreatorIsIn(Collection<Long> ids) {
        return (root, query, builder) -> {
            if (ids == null || ids.size() == 0) {
                return builder.and();
            }

            return root.get(Event_.CREATOR).in(ids);
        };
    }

    private static Specification<Event> eventStatusIsIn(Collection<EventStatus> statuses) {
        return (root, query, builder) -> {
            if (statuses == null || statuses.size() == 0) {
                return builder.and();
            }

            return root.get(Event_.STATUS).in(statuses);
        };
    }

    private static Specification<Event> eventAnnotationOrDescriptionContains(String expression) {
        return (root, query, builder) -> {
            if (expression == null) {
                return builder.and();
            }

            return builder.or(
                    builder.like(builder.lower(root.get(Event_.ANNOTATION)), contains(expression)),
                    builder.like(builder.lower(root.get(Event_.DESCRIPTION)), contains(expression))
            );
        };
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression.toLowerCase());
    }

    private static Specification<Event> eventIsPaid(Boolean paid) {
        return (root, query, builder) -> {
            if (paid == null) {
                return builder.and();
            }

            return builder.equal(root.get(Event_.PAID), paid);
        };
    }

    private static Specification<Event> eventStartIsBetween(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        return (root, query, builder) -> {

            LocalDateTime queryRangeStart = rangeStart != null ? rangeStart : LocalDateTime.now();
            LocalDateTime queryRangeEnd = rangeEnd;

            return builder.and(
                    builder.greaterThan(root.get(Event_.STARTED_ON), queryRangeStart),
                    queryRangeEnd != null ? builder.lessThan(root.get(Event_.STARTED_ON), queryRangeEnd) : builder.and()
            );
        };
    }
}
