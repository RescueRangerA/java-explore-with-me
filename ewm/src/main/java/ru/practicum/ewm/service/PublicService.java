package ru.practicum.ewm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.CustomDateTimeFormatter;
import ru.practicum.ewm.ModelMapper;
import ru.practicum.ewm.controller.dto.*;
import ru.practicum.ewm.exception.generic.ExtendedEntityNotFoundException;
import ru.practicum.ewm.extension.CustomPageableParameters;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.repository.event.EventRepository;
import ru.practicum.ewm.repository.event.EventWithCount;
import ru.practicum.ewm.repository.event.GetEventsQuerySpecification;
import ru.practicum.ewm.repository.eventcategory.EventCategoryRepository;
import ru.practicum.ewm.repository.eventcompilation.EventCompilationRepository;
import ru.practicum.ewm.statsclient.StatsClientEwmWrapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublicService {
    private final EventCategoryRepository eventCategoryRepository;

    private final EventCompilationRepository eventCompilationRepository;

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final CustomDateTimeFormatter dateTimeFormatter;

    private final StatsClientEwmWrapper statsClientEwmWrapper;

    public PublicService(EventCategoryRepository eventCategoryRepository,
                         EventCompilationRepository eventCompilationRepository,
                         EventRepository eventRepository,
                         ModelMapper modelMapper,
                         CustomDateTimeFormatter dateTimeFormatter,
                         StatsClientEwmWrapper statsClientEwmWrapper
    ) {
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventCompilationRepository = eventCompilationRepository;
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.dateTimeFormatter = dateTimeFormatter;
        this.statsClientEwmWrapper = statsClientEwmWrapper;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> publicSearchCategories(Integer from, Integer size) {
        CustomPageableParameters pageableParameters = CustomPageableParameters.of(from, size);

        return eventCategoryRepository
                .findAll(pageableParameters.toPageable())
                .stream()
                .map(modelMapper::toCategoryDto)
                .collect(Collectors.toList())
                ;
    }

    @Transactional(readOnly = true)
    public CategoryDto publicGetCategory(Long catId) {
        return modelMapper
                .toCategoryDto(eventCategoryRepository
                        .findById(catId)
                        .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(EventCategory.class, catId))
                );
    }

    @Transactional(readOnly = true)
    public CompilationDto publicGetCompilation(Long compId) {
        EventCompilation eventCompilation = eventCompilationRepository
                .findById(compId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(EventCompilation.class, compId));

        Map<Long, EventWithCount> eventWithCount = eventRepository
                .findAllByIdsAndAppendConfirmedParticipationCount(
                        eventCompilation.getEvents()
                                .stream()
                                .map(Event::getId)
                                .collect(Collectors.toList())
                );

        Map<Long, Long> eventAndViews = statsClientEwmWrapper.fetchViewsOfEventIds(List.copyOf(eventWithCount.keySet()));

        return modelMapper.toCompilationDto(
                eventCompilation,
                eventWithCount,
                eventAndViews
        );
    }

    @Transactional(readOnly = true)
    public List<CompilationDto> publicSearchCompilations(Boolean pinned, Integer from, Integer size) {
        CustomPageableParameters pageableParameters = CustomPageableParameters.of(from, size);

        Page<EventCompilation> eventCompilationPage = eventCompilationRepository.findAllByPinnedIs(
                pinned,
                pageableParameters.toPageable()
        );

        Set<Long> eventsIds = new HashSet<>();
        eventCompilationPage.forEach((c) -> c.getEvents().forEach((e) -> eventsIds.add(e.getId())));

        Map<Long, EventWithCount> eventWithCount = eventRepository
                .findAllByIdsAndAppendConfirmedParticipationCount(List.copyOf(eventsIds));

        Map<Long, Long> eventAndViews = statsClientEwmWrapper.fetchViewsOfEventIds(List.copyOf(eventWithCount.keySet()));

        return eventCompilationPage
                .stream()
                .map(
                        eventCompilation -> modelMapper.toCompilationDto(
                                eventCompilation,
                                eventWithCount,
                                eventAndViews
                        )
                )
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public EventFullDto publicGetEvent(Long id) {
        statsClientEwmWrapper.registerHit();

        // событие должно быть опубликовано
        Specification<Event> specificationForPublished = Specification.where(
                (root, query, builder) -> builder.and(
                        builder.equal(root.get(Event_.STATUS), EventStatus.PUBLISHED)
                )
        );

        EventWithCount eventWithCount = eventRepository
                .findByIdWithParticipationCount(id, specificationForPublished)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(Event.class, id));

        return modelMapper.toEventFullDto(
                eventWithCount.getEvent(),
                eventWithCount.getParticipationCount(),
                statsClientEwmWrapper.fetchViewsOfEvent(eventWithCount.getEvent())
        );
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> publicSearchEvents(
            String text,
            List<Long> categories,
            Boolean paid,
            String rangeStart,
            String rangeEnd,
            Boolean onlyAvailable,
            PublicEventSortEnum sort,
            Integer from,
            Integer size
    ) {
        statsClientEwmWrapper.registerHit();

        LocalDateTime parsedRangeStart = rangeStart != null ? dateTimeFormatter.parse(rangeStart).orElse(null) : null;
        LocalDateTime parsedRangeEnd = rangeEnd != null ? dateTimeFormatter.parse(rangeEnd).orElse(null) : null;

        Specification<Event> specification = GetEventsQuerySpecification
                .builder()
                .text(text)
                .categoryIds(categories)
                .statuses(List.of(EventStatus.PUBLISHED))
                .paid(paid)
                .rangeStart(parsedRangeStart)
                .rangeEnd(parsedRangeEnd)
                .build()
                .makeSpecification();

        CustomPageableParameters pageableParameters = CustomPageableParameters.of(from, size);

        List<EventWithCount> eventWithCount;
        Map<Long, Long> views;
        if (Objects.equals(sort, PublicEventSortEnum.EVENT_DATE)) {
            eventWithCount = eventRepository.findAllWithParticipationCount(
                    onlyAvailable,
                    specification,
                    pageableParameters.toPageable(Sort.by(new Sort.Order(Sort.Direction.DESC, Event_.CREATED_ON)))
            ).stream().collect(Collectors.toList());

            views = statsClientEwmWrapper.fetchViewsOfEventIds(
                    eventWithCount.stream().map(EventWithCount::getEvent).map(Event::getId).collect(Collectors.toList())
            );
        } else if (Objects.equals(sort, PublicEventSortEnum.VIEWS)) {
            eventWithCount = eventRepository.findAllWithParticipationCount(
                    onlyAvailable,
                    specification,
                    pageableParameters.toPageable(Sort.unsorted())
            ).stream().collect(Collectors.toList());

            views = statsClientEwmWrapper.fetchViewsOfEventIds(
                    eventWithCount.stream().map(EventWithCount::getEvent).map(Event::getId).collect(Collectors.toList())
            );

            eventWithCount.sort(Comparator.comparingLong(a -> views.get(a.getEvent().getId())));
        } else {
            eventWithCount = eventRepository.findAllWithParticipationCount(
                    onlyAvailable,
                    specification,
                    pageableParameters.toPageable(Sort.unsorted())
            ).stream().collect(Collectors.toList());

            views = statsClientEwmWrapper.fetchViewsOfEventIds(
                    eventWithCount.stream().map(EventWithCount::getEvent).map(Event::getId).collect(Collectors.toList())
            );
        }

        statsClientEwmWrapper.registerMultipleHitsByEvent(
                eventWithCount.stream().map(EventWithCount::getEvent).collect(Collectors.toList())
        );

        return eventWithCount
                .stream()
                .map(
                        e -> modelMapper.toEventShortDto(
                                e.getEvent(),
                                e.getParticipationCount(),
                                views.get(e.getEvent().getId())
                        )
                )
                .collect(Collectors.toList());
    }
}
