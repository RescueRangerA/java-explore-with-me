package ru.practicum.stats.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.stats.model.EndpointHit;

import java.util.List;

public interface CustomEndpointHitRepository {
    List<ViewStatsProjection> getStatsWithHits(Boolean unique, Specification<EndpointHit> where);
}
