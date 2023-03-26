package ru.practicum.ewm.repository.eventreaction;

import org.springframework.stereotype.Repository;
import ru.practicum.ewm.model.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class CustomEventReactionRepositoryImpl implements CustomEventReactionRepository {
    private final EntityManager entityManager;


    public CustomEventReactionRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Long calculateEventRating(Event event) {
        Tuple result;
        try {
            result = entityManager.createQuery(
                            "SELECT er.id.event.id as event_id, count(eLikes) - count(eDisLikes) as rating FROM EventReaction er " +
                                    "LEFT OUTER JOIN Event eLikes on eLikes=er.id.event and er.isLike=true " +
                                    "LEFT OUTER JOIN Event eDisLikes on eDisLikes=er.id.event and er.isLike=false " +
                                    "WHERE er.id.event.id = :id " +
                                    "GROUP BY er.id.event.id",
                            Tuple.class
                    )
                    .setParameter("id", event.getId())
                    .getSingleResult();
        } catch (NoResultException ignored) {
            return 0L;
        }

        return result.get("rating", Long.class);
    }

    public Map<Long, Long> calculateEventsRating(List<Long> ids) {
        return entityManager.createQuery(
                        "SELECT er.id.event.id as event_id, count(eLikes) - count(eDisLikes) as rating FROM EventReaction er " +
                                "LEFT OUTER JOIN Event eLikes on eLikes=er.id.event and er.isLike=true " +
                                "LEFT OUTER JOIN Event eDisLikes on eDisLikes=er.id.event and er.isLike=false " +
                                "WHERE er.id.event.id in (:ids) " +
                                "GROUP BY er.id.event.id",
                        Tuple.class
                )
                .setParameter("ids", ids)
                .getResultStream()
                .collect(
                        Collectors.toMap(
                                t -> t.get("event_id", Long.class),
                                t -> t.get("rating", Long.class)
                        )
                );
    }
}
