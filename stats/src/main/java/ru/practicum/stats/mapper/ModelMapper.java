package ru.practicum.stats.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.stats.controller.dto.EndpointHitDto;
import ru.practicum.stats.utils.CustomDateTimeFormatter;
import ru.practicum.stats.controller.dto.ViewStats;
import ru.practicum.stats.model.EndpointHit;
import ru.practicum.stats.repository.ViewStatsProjection;

@Component
public class ModelMapper {
    private final CustomDateTimeFormatter dateTimeFormatter;


    public ModelMapper(CustomDateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(
                0L,
                endpointHitDto.getId(),
                endpointHitDto.getApp(),
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                dateTimeFormatter.parse(endpointHitDto.getTimestamp()).orElse(null)
        );
    }

    public ViewStats toViewStats(ViewStatsProjection viewStatsProjection) {
        return new ViewStats(
                viewStatsProjection.getAppId(),
                viewStatsProjection.getUri(),
                viewStatsProjection.getHitsCount()
        );
    }
}
