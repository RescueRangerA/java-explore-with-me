package ru.practicum.ewm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.CustomDateTimeFormatter;
import ru.practicum.ewm.EventDateValidator;
import ru.practicum.ewm.ModelMapper;
import ru.practicum.ewm.controller.dto.*;
import ru.practicum.ewm.exception.event.EventMutationViolationException;
import ru.practicum.ewm.exception.generic.ExtendedEntityNotFoundException;
import ru.practicum.ewm.extension.CustomPageableParameters;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.repository.event.EventRepository;
import ru.practicum.ewm.repository.event.EventWithCount;
import ru.practicum.ewm.repository.event.GetEventsQuerySpecification;
import ru.practicum.ewm.repository.eventcategory.EventCategoryRepository;
import ru.practicum.ewm.repository.eventcompilation.EventCompilationRepository;
import ru.practicum.ewm.repository.user.UserRepository;
import ru.practicum.ewm.statsclient.StatsClientEwmWrapper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final UserRepository userRepository;

    private final EventCategoryRepository eventCategoryRepository;

    private final EventCompilationRepository eventCompilationRepository;

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final CustomDateTimeFormatter dateTimeFormatter;

    private final EventDateValidator eventDateValidator;

    private final StatsClientEwmWrapper statsClientEwmWrapper;

    public AdminService(UserRepository userRepository,
                        EventCategoryRepository eventCategoryRepository,
                        EventCompilationRepository eventCompilationRepository,
                        EventRepository eventRepository,
                        ModelMapper modelMapper,
                        CustomDateTimeFormatter dateTimeFormatter,
                        StatsClientEwmWrapper statsClientEwmWrapper
    ) {
        this.userRepository = userRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventCompilationRepository = eventCompilationRepository;
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.dateTimeFormatter = dateTimeFormatter;
        this.statsClientEwmWrapper = statsClientEwmWrapper;
        this.eventDateValidator = new EventDateValidator();
    }

    @Transactional
    public CategoryDto adminAddCategory(NewCategoryDto newCategoryDto) {
        return modelMapper.toCategoryDto(
                eventCategoryRepository.save(
                        modelMapper.toEventCategory(newCategoryDto)
                )
        );
    }

    @Transactional
    public void adminDeleteUser(Long userId) {
        userRepository.delete(
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(User.class, userId))
        );
    }

    @Transactional
    public void adminDeleteCategory(Long catId) {
        eventCategoryRepository.delete(
                eventCategoryRepository
                        .findById(catId)
                        .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(EventCategory.class, catId))
        );
    }

    @Transactional
    public void adminDeleteCompilation(Long compId) {
        eventCompilationRepository.delete(
                eventCompilationRepository
                        .findById(compId)
                        .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(EventCompilation.class, compId))
        );
    }

    @Transactional(readOnly = true)
    public List<EventFullDto> adminSearchEvents(
            List<Long> users,
            List<EventStatusRequestEnum> states,
            List<Long> categories,
            String rangeStart,
            String rangeEnd,
            Integer from,
            Integer size
    ) {
        LocalDateTime parsedRangeStart = rangeStart != null ? dateTimeFormatter.parse(rangeStart).orElse(null) : null;
        LocalDateTime parsedRangeEnd = rangeEnd != null ? dateTimeFormatter.parse(rangeEnd).orElse(null) : null;

        List<EventStatus> eventStatuses = new ArrayList<>();
        if (states != null) {
            eventStatuses.addAll(states.stream().map(modelMapper::toEventStatus).collect(Collectors.toList()));
        }

        Specification<Event> specification = GetEventsQuerySpecification
                .builder()
                .userIds(users)
                .statuses(eventStatuses)
                .categoryIds(categories)
                .rangeStart(parsedRangeStart)
                .rangeEnd(parsedRangeEnd)
                .build()
                .makeSpecification();

        CustomPageableParameters pageableParameters = CustomPageableParameters.of(from, size);

        Page<EventWithCount> eventWithCount = eventRepository.findAllWithParticipationCount(
                false,
                specification,
                pageableParameters.toPageable()
        );

        Map<Long, Long> views = statsClientEwmWrapper.fetchViewsOfEventIds(
                eventWithCount.stream().map(EventWithCount::getEvent).map(Event::getId).collect(Collectors.toList())
        );

        return eventWithCount
                .stream()
                .map((
                        e -> modelMapper.toEventFullDto(
                                e.getEvent(),
                                e.getParticipationCount(),
                                views.get(e.getEvent().getId())
                        )
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserDto> adminSearchUsers(List<Long> ids, Integer from, Integer size) {
        List<User> users = Collections.emptyList();

        if (ids != null) {
            users = userRepository.getAllByIdIn(ids);
        } else {
            CustomPageableParameters pageableParameters = CustomPageableParameters.of(from, size);
            users = userRepository.findAll(pageableParameters.toPageable()).stream().collect(Collectors.toList());
        }

        return users.stream().map(modelMapper::toUserDto).collect(Collectors.toList());
    }

    @Transactional
    public UserDto adminAddUser(NewUserRequest newUserRequest) {
        return modelMapper.toUserDto(
                userRepository.save(
                        modelMapper.toUser(newUserRequest)
                )
        );
    }

    @Transactional
    public CompilationDto adminAddCompilation(NewCompilationDto newCompilationDto) {
        Map<Long, EventWithCount> eventWithCount = eventRepository.findAllByIdsAndAppendConfirmedParticipationCount(
                List.copyOf(newCompilationDto.getEvents())
        );

        EventCompilation eventCompilation = eventCompilationRepository.save(
                modelMapper.toEventCompilation(
                        newCompilationDto,
                        eventWithCount
                                .values()
                                .stream()
                                .map(EventWithCount::getEvent)
                                .collect(Collectors.toList())
                )
        );

        Map<Long, Long> eventAndViews = statsClientEwmWrapper.fetchViewsOfEventIds(List.copyOf(eventWithCount.keySet()));

        return modelMapper.toCompilationDto(
                eventCompilation,
                eventWithCount,
                eventAndViews
        );
    }

    @Transactional
    public CategoryDto adminUpdateCategory(Long catId, CategoryDto categoryDto) {
        EventCategory eventCategory = eventCategoryRepository
                .findById(catId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(EventCategory.class, catId));

        if (categoryDto.getName() != null) {
            eventCategory.setName(categoryDto.getName());
        }

        return modelMapper.toCategoryDto(eventCategory);
    }

    @Transactional
    public CompilationDto adminUpdateCompilation(
            Long compId,
            UpdateCompilationRequest updateCompilationRequest
    ) {
        EventCompilation eventCompilation = eventCompilationRepository
                .findById(compId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(EventCompilation.class, compId));

        Map<Long, EventWithCount> eventWithParticipationCountProjections = Collections.emptyMap();
        if (updateCompilationRequest.getEvents() != null) {
            eventWithParticipationCountProjections = eventRepository
                    .findAllByIdsAndAppendConfirmedParticipationCount(List.copyOf(updateCompilationRequest.getEvents()));

            eventCompilation.setEvents(
                    Set.copyOf(
                            eventWithParticipationCountProjections
                                    .values()
                                    .stream()
                                    .map(EventWithCount::getEvent)
                                    .collect(Collectors.toList())
                    )
            );
        } else {
            eventWithParticipationCountProjections = eventRepository
                    .findAllByIdsAndAppendConfirmedParticipationCount(
                            eventCompilation
                                    .getEvents()
                                    .stream()
                                    .map(Event::getId)
                                    .collect(Collectors.toList())
                    );
        }

        if (updateCompilationRequest.getPinned() != null) {
            eventCompilation.setPinned(updateCompilationRequest.getPinned());
        }

        if (updateCompilationRequest.getTitle() != null) {
            eventCompilation.setTitle(updateCompilationRequest.getTitle());
        }

        Map<Long, Long> eventAndViews = statsClientEwmWrapper.fetchViewsOfEventIds(
                eventCompilation.getEvents()
                        .stream()
                        .map(Event::getId)
                        .collect(Collectors.toList())
        );

        return modelMapper.toCompilationDto(
                eventCompilation,
                eventWithParticipationCountProjections,
                eventAndViews
        );
    }

    @Transactional
    public EventFullDto adminUpdateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        EventWithCount eventWithCount = eventRepository
                .findByIdAndAppendConfirmedParticipationCount(eventId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(Event.class, eventId));

        Event event = eventWithCount.getEvent();
        Long participationCount = eventWithCount.getParticipationCount();

        if (!event.getStatus().equals(EventStatus.PENDING)) {
            throw EventMutationViolationException.ofIncorrectStatus(event.getStatus());
        }

        if (updateEventAdminRequest.getStateAction() != null) {
            switch (updateEventAdminRequest.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setStatus(EventStatus.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setStatus(EventStatus.CANCELED);
                    event.setPublishedOn(null);
                    break;
            }
        }

        if (updateEventAdminRequest.getEventDate() != null) {
            dateTimeFormatter.parse(updateEventAdminRequest.getEventDate()).ifPresent(event::setStartedOn);

            if (!eventDateValidator.isValidPublishedAndStarted(event.getPublishedOn(), event.getStartedOn())) {
                throw EventMutationViolationException.ofIncorrectPublishAndStartDateUpdate(
                        event.getPublishedOn(),
                        event.getStartedOn()
                );
            }
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }

        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }

        if (updateEventAdminRequest.getCategory() != null) {
            Long eventCategoryId = updateEventAdminRequest.getCategory();

            EventCategory eventCategory = eventCategoryRepository
                    .findById(eventCategoryId)
                    .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(EventCategory.class, eventCategoryId));

            event.setCategory(eventCategory);
        }

        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocationLatitude(updateEventAdminRequest.getLocation().getLat());
            event.setLocationLongitude(updateEventAdminRequest.getLocation().getLon());
        }

        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }

        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }

        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }

        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        return modelMapper.toEventFullDto(
                event,
                participationCount,
                statsClientEwmWrapper.fetchViewsOfEvent(event)
        );
    }
}
