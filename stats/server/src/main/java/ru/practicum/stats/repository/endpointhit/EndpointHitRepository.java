package ru.practicum.stats.repository.endpointhit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.practicum.stats.model.EndpointHit;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long>, JpaSpecificationExecutor<EndpointHit>, CustomEndpointHitRepository {
}
