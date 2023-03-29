package ru.practicum.ewm.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.CustomDateTimeFormatter;
import ru.practicum.ewm.EventDateValidator;
import ru.practicum.ewm.ModelMapper;
import ru.practicum.ewm.exception.eventreaction.EventReactionMutationViolationException;
import ru.practicum.ewm.exception.request.EventRequestMutationViolationException;
import ru.practicum.ewm.controller.dto.*;
import ru.practicum.ewm.exception.AccessDeniedException;
import ru.practicum.ewm.exception.event.EventMutationViolationException;
import ru.practicum.ewm.exception.generic.ExtendedEntityNotFoundException;
import ru.practicum.ewm.extension.CustomPageableParameters;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.repository.event.EventRepository;
import ru.practicum.ewm.repository.event.EventWithCount;
import ru.practicum.ewm.repository.eventcategory.EventCategoryRepository;
import ru.practicum.ewm.repository.eventreaction.EventReactionRepository;
import ru.practicum.ewm.repository.eventpaticipation.EventParticipationRequestRepository;
import ru.practicum.ewm.repository.user.UserRepository;
import ru.practicum.ewm.statsclient.StatsClientEwmWrapper;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PrivateService {
    private final UserRepository userRepository;

    private final EventCategoryRepository eventCategoryRepository;

    private final EventRepository eventRepository;

    private final ModelMapper modelMapper;

    private final CustomDateTimeFormatter dateTimeFormatter;
    private final EventParticipationRequestRepository eventParticipationRequestRepository;

    private final EventReactionRepository eventReactionRepository;

    private final EventDateValidator eventDateValidator;

    private final StatsClientEwmWrapper statsClientEwmWrapper;

    public PrivateService(UserRepository userRepository,
                          EventCategoryRepository eventCategoryRepository,
                          EventRepository eventRepository,
                          ModelMapper modelMapper,
                          CustomDateTimeFormatter dateTimeFormatter,
                          EventParticipationRequestRepository eventParticipationRequestRepository,
                          EventReactionRepository eventReactionRepository, StatsClientEwmWrapper statsClientEwmWrapper
    ) {
        this.userRepository = userRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.dateTimeFormatter = dateTimeFormatter;
        this.eventParticipationRequestRepository = eventParticipationRequestRepository;
        this.eventReactionRepository = eventReactionRepository;
        this.statsClientEwmWrapper = statsClientEwmWrapper;
        this.eventDateValidator = new EventDateValidator();
    }

    @Transactional
    public EventFullDto privateAddEvent(Long userId, NewEventDto newEventDto) {
        User currentUser = userRepository
                .findById(userId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(User.class, userId));

        EventCategory eventCategory = eventCategoryRepository
                .findById(newEventDto.getCategory())
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(EventCategory.class, newEventDto.getCategory()));

        Event event = modelMapper.toNewEvent(newEventDto, currentUser, eventCategory);

        if (!eventDateValidator.isValidCreatedAndStarted(event.getCreatedOn(), event.getStartedOn())) {
            throw EventMutationViolationException.ofIncorrectCreateAndStartDateCreate(event.getCreatedOn(), event.getStartedOn());
        }

        return modelMapper.toEventFullDto(
                eventRepository.save(event),
                0L,
                0L,
                0L
        );
    }

    @Transactional
    public ParticipationRequestDto privateAddParticipationRequest(Long userId, Long eventId) {
        User currentUser = userRepository
                .findById(userId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(User.class, userId));

        EventWithCount eventWithCount = eventRepository
                .findByIdAndAppendConfirmedParticipationCount(eventId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(Event.class, eventId));

        Event event = eventWithCount.getEvent();
        Long participationCount = eventWithCount.getParticipationCount();

        if (eventParticipationRequestRepository.existsByEventAndCreator(event, currentUser)) {
            throw EventRequestMutationViolationException.ofDuplicatedRequest(event, currentUser);
        }

        if (Objects.equals(currentUser.getId(), event.getCreator().getId())) {
            throw EventRequestMutationViolationException.ofParticipateOnOwnRequest(event, currentUser);
        }

        if (!event.getStatus().equals(EventStatus.PUBLISHED)) {
            throw EventRequestMutationViolationException.ofNotPublishedEvent(event, currentUser);
        }

        if (event.getParticipantLimit() <= participationCount) {
            throw EventRequestMutationViolationException.ofParticipantLimitExceeded(event, currentUser);
        }

        EventParticipationRequest request = modelMapper.toParticipationRequest(currentUser, event);

        if (!event.getRequestModeration()) {
            request.setStatus(EventParticipationRequestStatus.CONFIRMED);
        }

        return modelMapper.toParticipationRequestDto(
                eventParticipationRequestRepository.save(request)
        );
    }

    @Transactional
    public ParticipationRequestDto privateCancelParticipationRequest(Long userId, Long requestId) {
        User currentUser = userRepository
                .findById(userId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(User.class, userId));

        EventParticipationRequest request = eventParticipationRequestRepository
                .findById(requestId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(EventParticipationRequest.class, requestId));

        if (!Objects.equals(currentUser.getId(), request.getCreator().getId())) {
            throw AccessDeniedException.ofEventRequestAndUser(request, currentUser);
        }

        request.setStatus(EventParticipationRequestStatus.CANCELED);

        return modelMapper.toParticipationRequestDto(request);
    }

    @Transactional
    public EventRequestStatusUpdateResult privateChangeRequestStatus(
            Long userId,
            Long eventId,
            EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {
        User currentUser = userRepository
                .findById(userId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(User.class, userId));

        EventWithCount eventWithCount = eventRepository
                .findByIdAndAppendConfirmedParticipationCount(eventId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(Event.class, eventId));

        Event event = eventWithCount.getEvent();

        if (!Objects.equals(currentUser.getId(), event.getCreator().getId())) {
            throw AccessDeniedException.ofEventAndUser(event, currentUser);
        }

        Long participationCount = eventWithCount.getParticipationCount();

        if (Objects.equals(participationCount, event.getParticipantLimit())) {
            throw EventRequestMutationViolationException.ofParticipantLimitExceeded(event, currentUser);
        }

        List<EventParticipationRequest> requests = eventParticipationRequestRepository.findAllByIdIn(
                eventRequestStatusUpdateRequest.getRequestIds()
        );

        long availableRequestCount = event.getParticipantLimit() - participationCount;
        List<EventParticipationRequest> confirmedList = new ArrayList<>();
        List<EventParticipationRequest> rejectedList = new ArrayList<>();

        for (EventParticipationRequest request : requests) {
            if (eventRequestStatusUpdateRequest.getStatus().equals(EventRequestStatusUpdateRequest.StatusEnum.CONFIRMED)) {
                if (availableRequestCount == 0) {
                    request.setStatus(EventParticipationRequestStatus.REJECTED);
                    rejectedList.add(request);
                    continue;
                }

                if (request.getStatus().equals(EventParticipationRequestStatus.PENDING)) {
                    request.setStatus(EventParticipationRequestStatus.CONFIRMED);
                    availableRequestCount--;
                    confirmedList.add(request);
                } else {
                    throw EventRequestMutationViolationException.ofUnchangeableRequestStatus(
                            request.getStatus(),
                            EventParticipationRequestStatus.PENDING
                    );
                }
            } else if (eventRequestStatusUpdateRequest.getStatus().equals(EventRequestStatusUpdateRequest.StatusEnum.REJECTED)) {
                if (request.getStatus().equals(EventParticipationRequestStatus.PENDING)) {
                    request.setStatus(EventParticipationRequestStatus.REJECTED);
                    rejectedList.add(request);
                } else {
                    throw EventRequestMutationViolationException.ofUnchangeableRequestStatus(
                            request.getStatus(),
                            EventParticipationRequestStatus.PENDING
                    );
                }
            }
        }

        return modelMapper.toEventRequestStatusUpdateResult(
                confirmedList,
                rejectedList
        );
    }

    @Transactional(readOnly = true)
    public EventFullDto privateGetEvent(Long userId, Long eventId) {
        User currentUser = userRepository
                .findById(userId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(User.class, userId));

        Specification<Event> specificationForCurrentUser = Specification.where(
                (root, querySpec, builder) -> builder.and(
                        builder.equal(root.get(Event_.CREATOR), currentUser.getId())
                )
        );

        EventWithCount eventWithCount = eventRepository
                .findByIdWithParticipationCount(eventId, specificationForCurrentUser)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(Event.class, eventId));

        return modelMapper.toEventFullDto(
                eventWithCount.getEvent(),
                eventWithCount.getParticipationCount(),
                statsClientEwmWrapper.fetchViewsOfEvent(eventWithCount.getEvent()),
                eventReactionRepository.calculateEventRating(eventWithCount.getEvent())
        );
    }


    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> privateGetEventParticipants(Long userId, Long eventId) {
        User currentUser = userRepository
                .findById(userId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(User.class, userId));

        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(Event.class, eventId));

        if (!Objects.equals(currentUser.getId(), event.getCreator().getId())) {
            throw AccessDeniedException.ofEventAndUser(event, currentUser);
        }

        return eventParticipationRequestRepository
                .findAllByEvent(event)
                .stream()
                .map(modelMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EventShortDto> privateSearchEvents(Long userId, Integer from, Integer size) {
        User currentUser = userRepository
                .findById(userId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(User.class, userId));

        Specification<Event> specificationForCurrentUser = Specification.where(
                (root, querySpec, builder) -> builder.and(
                        builder.equal(root.get(Event_.CREATOR), currentUser.getId())
                )
        );

        CustomPageableParameters pageableParameters = CustomPageableParameters.of(from, size);

        Page<EventWithCount> eventWithCount = eventRepository.findAllWithParticipationCount(
                false,
                specificationForCurrentUser,
                pageableParameters.toPageable()
        );

        Map<Long, Long> views = statsClientEwmWrapper.fetchViewsOfEventIds(
                eventWithCount.stream().map(EventWithCount::getEvent).map(Event::getId).collect(Collectors.toList())
        );

        Map<Long, Long> eventWithRating = eventReactionRepository.calculateEventsRating(
                eventWithCount.stream().map(EventWithCount::getEvent).map(Event::getId).collect(Collectors.toList())
        );

        return eventWithCount
                .stream()
                .map(
                        e -> modelMapper.toEventShortDto(
                                e.getEvent(),
                                e.getParticipationCount(),
                                views.getOrDefault(e.getEvent().getId(), 0L),
                                eventWithRating.getOrDefault(e.getEvent().getId(), 0L)
                        )
                )
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> privateSearchUserRequests(Long userId) {
        User currentUser = userRepository
                .findById(userId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(User.class, userId));

        return eventParticipationRequestRepository
                .findAllByCreator(currentUser)
                .stream()
                .map(modelMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto privateUpdateEvent(
            Long userId,
            Long eventId,
            UpdateEventUserRequest updateEventUserRequest
    ) {
        User currentUser = userRepository
                .findById(userId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(User.class, userId));

        EventWithCount eventWithCount = eventRepository
                .findByIdAndAppendConfirmedParticipationCount(eventId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(Event.class, eventId));

        Event event = eventWithCount.getEvent();
        Long participationCount = eventWithCount.getParticipationCount();

        if (!Objects.equals(currentUser.getId(), event.getCreator().getId())) {
            throw AccessDeniedException.ofEventAndUser(event, currentUser);
        }

        if (!event.getStatus().equals(EventStatus.PENDING) && !event.getStatus().equals(EventStatus.CANCELED)) {
            throw EventMutationViolationException.ofIncorrectStatusForUser(event.getStatus(), currentUser);
        }

        if (updateEventUserRequest.getEventDate() != null) {
            dateTimeFormatter.parse(updateEventUserRequest.getEventDate()).ifPresent(event::setStartedOn);

            if (!eventDateValidator.isValidPublishedAndStarted(event.getPublishedOn(), event.getStartedOn())) {
                throw EventMutationViolationException.ofIncorrectPublishAndStartDateUpdate(event.getPublishedOn(), event.getStartedOn());
            }
        }

        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }

        if (updateEventUserRequest.getCategory() != null) {
            Long eventCategoryId = updateEventUserRequest.getCategory();

            EventCategory eventCategory = eventCategoryRepository
                    .findById(eventCategoryId)
                    .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(EventCategory.class, eventCategoryId));

            event.setCategory(eventCategory);
        }

        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }

        if (updateEventUserRequest.getLocation() != null) {
            event.setLocationLatitude(updateEventUserRequest.getLocation().getLat());
            event.setLocationLongitude(updateEventUserRequest.getLocation().getLon());
        }

        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }

        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }

        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }

        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }

        if (updateEventUserRequest.getStateAction() != null) {
            switch (updateEventUserRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setStatus(EventStatus.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setStatus(EventStatus.CANCELED);
                    break;
            }
        }

        return modelMapper.toEventFullDto(
                event,
                participationCount,
                statsClientEwmWrapper.fetchViewsOfEvent(event),
                eventReactionRepository.calculateEventRating(event)
        );
    }

    @Transactional
    public NewEventReactionDto addLikeOrDislikeEvent(Long userId, Long eventId, Boolean isLike) {
        User currentUser = userRepository
                .findById(userId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(User.class, userId));

        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(Event.class, eventId));

        if (!event.getStatus().equals(EventStatus.PUBLISHED)) {
            throw EventReactionMutationViolationException.ofIncorrectEventStatus(event);
        }

        EventParticipationRequest participationRequest = eventParticipationRequestRepository.findByCreatorAndEvent(
                currentUser, event
        ).orElseThrow(() -> EventReactionMutationViolationException.ofMissingParticipationRequest(event, currentUser));

        if (!participationRequest.getStatus().equals(EventParticipationRequestStatus.CONFIRMED)) {
            throw EventReactionMutationViolationException.ofParticipationRequestIncorrectStatus(
                    participationRequest,
                    event,
                    currentUser
            );
        }

        EventReaction eventReaction = eventReactionRepository.save(
                new EventReaction(
                        new EventReactionID(event, currentUser),
                        isLike
                )
        );

        return modelMapper.toNewEventReactionDto(eventReaction);
    }

    public void removeLikeOrDislikeEvent(Long userId, Long eventId) {
        User currentUser = userRepository
                .findById(userId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(User.class, userId));

        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> ExtendedEntityNotFoundException.ofEntity(Event.class, eventId));

        try {
            eventReactionRepository.deleteById(new EventReactionID(event, currentUser));
        } catch (EmptyResultDataAccessException ignored) {

        }
    }
}
