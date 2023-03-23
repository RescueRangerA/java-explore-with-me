package ru.practicum.stats.repository.endpointhit;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.model.EndpointHit;
import ru.practicum.stats.model.EndpointHit_;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CustomEndpointHitRepositoryImpl implements CustomEndpointHitRepository {
    private final EntityManager entityManager;

    public CustomEndpointHitRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<ViewStatsProjection> getStatsWithHits(Boolean unique, Specification<EndpointHit> where) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        final CriteriaQuery<Tuple> query = criteriaBuilder.createQuery(Tuple.class);

        final Root<EndpointHit> root = query.from(EndpointHit.class);


        SingularAttribute<EndpointHit, String> uriAttr = EndpointHit_.uri;
        final Path<String> expressionUriAttr = root.get(uriAttr);

        SingularAttribute<EndpointHit, String> appAttr = EndpointHit_.appId;
        final Path<String> expressionAppAttr = root.get(appAttr);

        SingularAttribute<EndpointHit, String> ipAttr = EndpointHit_.ip;
        final Path<String> expressionIpAttr = root.get(ipAttr);

        List<Expression<?>> selections = new LinkedList<>();
        selections.add(expressionUriAttr);
        selections.add(expressionAppAttr);
        if (unique) {
            selections.add(expressionIpAttr);
        }

        query.multiselect(expressionUriAttr, expressionAppAttr, criteriaBuilder.count(root));

        query.select(criteriaBuilder.tuple(expressionUriAttr, expressionAppAttr, criteriaBuilder.count(root)));
        query.where(where.toPredicate(root, query, criteriaBuilder));
        query.groupBy(selections);
        query.orderBy(criteriaBuilder.desc(criteriaBuilder.count(root)));

        final List<Tuple> resultList = entityManager.createQuery(query).getResultList();

        return resultList.stream()
                .map(t -> new ViewStatsProjection(
                        t.get(1, appAttr.getJavaType()),
                        t.get(0, uriAttr.getJavaType()),
                        t.get(2, Long.class)
                ))
                .collect(Collectors.toList());
    }
}
