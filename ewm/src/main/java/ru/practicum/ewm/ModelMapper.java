package ru.practicum.ewm;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.repository.event.EventWithCount;
import ru.practicum.ewm.controller.dto.*;
import ru.practicum.ewm.model.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ModelMapper {
    private final CustomDateTimeFormatter dateTimeFormatter;

    public ModelMapper(CustomDateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public User toUser(NewUserRequest newUserRequest) {
        return new User(
                0L,
                newUserRequest.getName(),
                newUserRequest.getEmail()
        );
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getEmail(),
                user.getId(),
                user.getUsername()
        );
    }

    public UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getUsername()
        );
    }

    public EventCategory toEventCategory(NewCategoryDto newCategoryDto) {
        return new EventCategory(
                0L,
                newCategoryDto.getName()
        );
    }

    public CategoryDto toCategoryDto(EventCategory eventCategory) {
        return new CategoryDto(
                eventCategory.getId(),
                eventCategory.getName()
        );
    }

    public EventCompilation toEventCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return new EventCompilation(
                0L,
                Set.copyOf(events),
                newCompilationDto.getPinned(),
                newCompilationDto.getTitle()
        );
    }

    public Event toNewEvent(NewEventDto newEventDto, User creator, EventCategory eventCategory) {
        return new Event(
                0L,
                creator,
                eventCategory,
                Collections.emptySet(),
                EventStatus.PENDING,
                newEventDto.getTitle(),
                newEventDto.getAnnotation(),
                newEventDto.getDescription(),
                LocalDateTime.now(),
                dateTimeFormatter.parse(newEventDto.getEventDate()).orElse(null),
                null,
                newEventDto.getLocation().getLat(),
                newEventDto.getLocation().getLon(),
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                newEventDto.getRequestModeration()
        );
    }

    public EventShortDto toEventShortDto(Event event, Long confirmedRequestsCount, Long viewsCount) {
        return new EventShortDto(
                event.getAnnotation(),
                this.toCategoryDto(event.getCategory()),
                confirmedRequestsCount,
                dateTimeFormatter.format(event.getStartedOn()),
                event.getId(),
                this.toUserShortDto(event.getCreator()),
                event.getPaid(),
                event.getTitle(),
                viewsCount
        );
    }

    public EventFullDto toEventFullDto(Event event, Long confirmedRequestsCount, Long views) {
        return new EventFullDto(
                event.getAnnotation(),
                this.toCategoryDto(event.getCategory()),
                confirmedRequestsCount,
                dateTimeFormatter.format(event.getCreatedOn()),
                event.getDescription(),
                dateTimeFormatter.format(event.getStartedOn()),
                event.getId(),
                this.toUserShortDto(event.getCreator()),
                Location.builder().lat(event.getLocationLatitude()).lon(event.getLocationLongitude()).build(),
                event.getPaid(),
                event.getParticipantLimit(),
                dateTimeFormatter.format(event.getPublishedOn()),
                event.getRequestModeration(),
                this.toStateEnum(event.getStatus()),
                event.getTitle(),
                views
        );
    }

    public CompilationDto toCompilationDto(
            EventCompilation eventCompilation,
            Map<Long, EventWithCount> eventAndParticipationCount,
            Map<Long, Long> eventAndViews
    ) {
        return new CompilationDto(
                eventCompilation.getEvents()
                        .stream()
                        .map(e -> this.toEventShortDto(
                                e,
                                eventAndParticipationCount.get(e.getId()).getParticipationCount(),
                                eventAndViews.get(e.getId())
                        ))
                        .collect(Collectors.toSet()),
                eventCompilation.getId(),
                eventCompilation.getPinned(),
                eventCompilation.getTitle()
        );
    }

    public EventParticipationRequest toParticipationRequest(User creator, Event event) {
        return new EventParticipationRequest(
                0L,
                creator,
                event,
                EventParticipationRequestStatus.PENDING,
                LocalDateTime.now()
        );
    }

    public ParticipationRequestDto toParticipationRequestDto(EventParticipationRequest eventParticipationRequest) {
        return new ParticipationRequestDto(
                dateTimeFormatter.format(eventParticipationRequest.getCreatedOn()),
                eventParticipationRequest.getEvent().getId(),
                eventParticipationRequest.getId(),
                eventParticipationRequest.getCreator().getId(),
                eventParticipationRequest.getStatus().name()
        );
    }

    public EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(
            List<EventParticipationRequest> confirmedList,
            List<EventParticipationRequest> rejectedList
    ) {
        return new EventRequestStatusUpdateResult(
                confirmedList.stream().map(this::toParticipationRequestDto).collect(Collectors.toList()),
                rejectedList.stream().map(this::toParticipationRequestDto).collect(Collectors.toList())
        );
    }

    public EventFullDto.StateEnum toStateEnum(EventStatus eventStatus) {
        switch (eventStatus) {
            case PENDING:
                return EventFullDto.StateEnum.PENDING;
            case PUBLISHED:
                return EventFullDto.StateEnum.PUBLISHED;
            case CANCELED:
                return EventFullDto.StateEnum.CANCELED;
        }

        return null;
    }

    public EventStatus toEventStatus(EventStatusRequestEnum eventStatusRequestEnum) {
        switch (eventStatusRequestEnum) {
            case PENDING:
                return EventStatus.PENDING;
            case PUBLISHED:
                return EventStatus.PUBLISHED;
            case CANCELED:
                return EventStatus.CANCELED;
        }

        return null;
    }
}
