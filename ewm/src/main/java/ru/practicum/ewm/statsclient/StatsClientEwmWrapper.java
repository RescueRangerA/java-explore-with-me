package ru.practicum.ewm.statsclient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.ewm.CustomDateTimeFormatter;
import ru.practicum.ewm.extension.WebUtils;
import ru.practicum.ewm.model.Event;
import ru.practicum.stats.client.StatsControllerApi;
import ru.practicum.stats.controller.dto.EndpointHitDto;
import ru.practicum.stats.controller.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class StatsClientEwmWrapper {

    private final String serviceName;

    private final StatsControllerApi statsApiClient;

    private final WebUtils webUtils;

    private final CustomDateTimeFormatter dateTimeFormatter;

    public StatsClientEwmWrapper(
            @Value("${ru.practicum.ewm.server.name}") String serviceName,
            StatsControllerApi statsApiClient,
            WebUtils webUtils,
            CustomDateTimeFormatter dateTimeFormatter
    ) {
        this.serviceName = serviceName;
        this.statsApiClient = statsApiClient;
        this.webUtils = webUtils;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public void registerHit() {
        statsApiClient.hit(
                new EndpointHitDto(
                        0L,
                        serviceName,
                        webUtils.getRequestURI(),
                        webUtils.getClientIp(),
                        dateTimeFormatter.format(LocalDateTime.now())
                )
        ).block();
    }

    public Mono<Void> registerHitByEvent(Event event) {
        return statsApiClient.hit(
                new EndpointHitDto(
                        0L,
                        serviceName,
                        "/events/" + event.getId(),
                        webUtils.getClientIp(),
                        dateTimeFormatter.format(LocalDateTime.now())
                )
        );
    }

    public void registerMultipleHitsByEvent(List<Event> events) {
        Flux.fromIterable(events).flatMap(this::registerHitByEvent).collectList().block();
    }

    public Long fetchViewsOfEvent(Event event) {
        String uri = "/events/" + event.getId();

        Flux<ViewStats> stats = statsApiClient.getStats(
                dateTimeFormatter.format(LocalDateTime.now().minusYears(1L)),
                dateTimeFormatter.format(LocalDateTime.now().plusYears(1L)),
                List.of(uri),
                false
        );

        Optional<ViewStats> viewStats = Objects.requireNonNull(
                stats.collectList().block()
        ).stream().filter(s -> Objects.equals(s.getUri(), uri)).findFirst();

        return viewStats.isPresent() ? viewStats.get().getHits() : 0L;
    }

    public Map<Long, Long> fetchViewsOfEventIds(List<Long> eventIds) {
        if (eventIds == null || eventIds.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Long> uris = eventIds.stream().collect(Collectors.toMap(
                e -> "/events/" + e,
                e -> e
        ));

        Flux<ViewStats> stats = statsApiClient.getStats(
                dateTimeFormatter.format(LocalDateTime.now().minusYears(1L)),
                dateTimeFormatter.format(LocalDateTime.now().plusYears(1L)),
                List.copyOf(uris.keySet()),
                false
        );

        return Objects.requireNonNull(stats.collectList().block())
                .stream()
                .collect(Collectors.toMap(
                        s -> uris.get(s.getUri()),
                        ViewStats::getHits
                ));
    }
}
