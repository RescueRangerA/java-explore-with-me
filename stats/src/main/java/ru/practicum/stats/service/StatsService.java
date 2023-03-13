package ru.practicum.stats.service;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.stats.utils.CustomDateTimeFormatter;
import ru.practicum.stats.mapper.ModelMapper;
import ru.practicum.stats.controller.StatsControllerApiDelegate;
import ru.practicum.stats.controller.dto.ViewStats;
import ru.practicum.stats.model.EndpointHit;
import ru.practicum.stats.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsService implements StatsControllerApiDelegate {
    private final EndpointHitRepository endpointHitRepository;

    private final ModelMapper modelMapper;

    private final CustomDateTimeFormatter dateTimeFormatter;

    public StatsService(EndpointHitRepository endpointHitRepository,
                        ModelMapper modelMapper,
                        CustomDateTimeFormatter dateTimeFormatter
    ) {
        this.endpointHitRepository = endpointHitRepository;
        this.modelMapper = modelMapper;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public ResponseEntity<List<ViewStats>> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime parsedRangeStart = start != null ? dateTimeFormatter.parse(start).orElse(null) : null;
        LocalDateTime parsedRangeEnd = end != null ? dateTimeFormatter.parse(end).orElse(null) : null;

        Specification<EndpointHit> specification = Specification
                .where(endpointHitIsBetween(parsedRangeStart, parsedRangeEnd))
                .and(endpointHitHasOneOfUri(uris));

        return ResponseEntity.ok(
                endpointHitRepository.getStatsWithHits(unique, specification)
                        .stream()
                        .map(modelMapper::toViewStats)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public ResponseEntity<Void> hit(ru.practicum.stats.controller.dto.EndpointHit endpointHitDto) {
        endpointHitRepository.save(modelMapper.toEndpointHit(endpointHitDto));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private static Specification<EndpointHit> endpointHitIsBetween(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        return (root, query, builder) -> {
            if (rangeStart == null || rangeEnd == null) {
                return builder.and();
            }

            return builder.between(root.get("createdOn"), rangeStart, rangeEnd);
        };
    }

    private static Specification<EndpointHit> endpointHitHasOneOfUri(Collection<String> uris) {
        return (root, query, builder) -> {
            if (uris == null || uris.size() == 0) {
                return builder.and();
            }

            return root.get("uri").in(uris);
        };
    }
}
