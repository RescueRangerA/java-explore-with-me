package ru.practicum.ewm.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

@Repository
public class CustomEventRepositoryImpl implements CustomEventRepository {
    private final EntityManager entityManager;

    public CustomEventRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<EventWithCount> findByIdAndAppendConfirmedParticipationCount(Long id) {
        return findByIdWithParticipationCount(
                id,
                (root, query, builder) -> builder.and()
        );
    }

    @Override
    public Map<Long, EventWithCount> findAllByIdsAndAppendConfirmedParticipationCount(
            List<Long> ids
    ) {
        CriteriaQuery<Tuple> query = buildEventWithParticipationCountProjectionQuery(
                false,
                (root, querySpec, builder) -> builder.and(
                        root.get(Event_.ID).in(ids)
                ),
                Sort.unsorted()
        );

        List<Tuple> resultList = entityManager
                .createQuery(query)
                .getResultList();

        return resultList.stream().collect(Collectors.toMap(
                t -> t.get(0, Event.class).getId(),
                t -> new EventWithCount(
                        t.get(0, Event.class),
                        t.get(1, Long.class)
                )
        ));
    }

    public Page<EventWithCount> findAllWithParticipationCount(
            Boolean onlyAvailable,
            Specification<Event> spec,
            Pageable pageable
    ) {
        final CriteriaQuery<Tuple> query = buildEventWithParticipationCountProjectionQuery(
                onlyAvailable,
                spec,
                pageable.getSort()
        );

        List<Tuple> resultList = entityManager
                .createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(
                resultList.stream()
                        .map(t -> new EventWithCount(
                                t.get(0, query.from(Event.class).getJavaType()),
                                t.get(1, Long.class)
                        ))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Optional<EventWithCount> findByIdWithParticipationCount(
            Long id,
            Specification<Event> spec
    ) {
        CriteriaQuery<Tuple> query = buildEventWithParticipationCountProjectionQuery(
                false,
                (root, querySpec, builder) -> builder.and(
                        builder.equal(root.get(Event_.ID), id),
                        spec.toPredicate(root, querySpec, builder)
                ),
                Sort.unsorted()
        );

        Tuple result;
        try {
            result = entityManager.createQuery(query).getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return Optional.empty();
        }

        return Optional.of(
                new EventWithCount(
                        result.get(0, Event.class),
                        result.get(1, Long.class)
                )
        );
    }

    private CriteriaQuery<Tuple> buildEventWithParticipationCountProjectionQuery(
            Boolean onlyAvailable,
            Specification<Event> spec,
            Sort sort
    ) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);

        Root<Event> eventRoot = query.from(Event.class);

        Join<Event, EventParticipationRequest> eventEventParticipationRequestJoin = eventRoot.join(
                Event_.REQUESTS,
                JoinType.LEFT
        );
        eventEventParticipationRequestJoin.on(
                criteriaBuilder.equal(
                        eventEventParticipationRequestJoin.get(EventParticipationRequest_.STATUS),
                        EventParticipationRequestStatus.CONFIRMED
                )
        );

        query.multiselect(eventRoot, eventEventParticipationRequestJoin);
        query.select(criteriaBuilder.tuple(eventRoot, criteriaBuilder.count(eventEventParticipationRequestJoin)));

        List<Predicate> wherePredicates = new ArrayList<>();
        wherePredicates.add(
                spec.toPredicate(eventRoot, query, criteriaBuilder)
        );

        if (onlyAvailable) {
            wherePredicates.add(
                    criteriaBuilder.greaterThan(
                            eventRoot.get(Event_.PARTICIPANT_LIMIT),
                            criteriaBuilder.count(eventEventParticipationRequestJoin)
                    )
            );
        }

        query.where(wherePredicates.toArray(new Predicate[0]));
        query.groupBy(eventRoot);

        if (sort.isSorted()) {
            query.orderBy(toOrders(sort, eventRoot, criteriaBuilder));
        }

        return query;
    }
}
