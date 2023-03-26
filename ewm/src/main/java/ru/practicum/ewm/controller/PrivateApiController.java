package ru.practicum.ewm.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controller.dto.*;
import ru.practicum.ewm.service.PrivateService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
@Validated
public class PrivateApiController {

    private final PrivateService privateService;

    public PrivateApiController(PrivateService privateService) {
        this.privateService = privateService;
    }

    /**
     * POST /users/{userId}/events : Добавление нового события
     * Обратите внимание: дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
     *
     * @param userId      id текущего пользователя (required)
     * @param newEventDto данные добавляемого события (required)
     * @return Событие добавлено (status code 201)
     * or Запрос составлен некорректно (status code 400)
     * or Событие не удовлетворяет правилам создания (status code 409)
     */
    @Operation(
            operationId = "privateAddEvent",
            summary = "Добавление нового события",
            description = "Обратите внимание: дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента",
            tags = {"Private: События"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Событие добавлено", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = EventFullDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "409", description = "Событие не удовлетворяет правилам создания", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/users/{userId}/events",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<EventFullDto> privateAddEvent(
            @Parameter(name = "userId", description = "id текущего пользователя", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(name = "NewEventDto", description = "данные добавляемого события", required = true) @Valid @RequestBody NewEventDto newEventDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(privateService.privateAddEvent(userId, newEventDto));
    }


    /**
     * POST /users/{userId}/requests : Добавление запроса от текущего пользователя на участие в событии
     * Обратите внимание: - нельзя добавить повторный запрос  (Ожидается код ошибки 409) - инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409) - нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409) - если у события достигнут лимит запросов на участие - необходимо вернуть ошибку  (Ожидается код ошибки 409) - если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
     *
     * @param userId  id текущего пользователя (required)
     * @param eventId id события (required)
     * @return Заявка создана (status code 201)
     * or Запрос составлен некорректно (status code 400)
     * or Событие не найдено или недоступно (status code 404)
     * or Нарушение целостности данных (status code 409)
     */
    @Operation(
            operationId = "privateAddParticipationRequest",
            summary = "Добавление запроса от текущего пользователя на участие в событии",
            description = "Обратите внимание: - нельзя добавить повторный запрос  (Ожидается код ошибки 409) - инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409) - нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409) - если у события достигнут лимит запросов на участие - необходимо вернуть ошибку  (Ожидается код ошибки 409) - если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного",
            tags = {"Private: Запросы на участие"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Заявка создана", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ParticipationRequestDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Событие не найдено или недоступно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "409", description = "Нарушение целостности данных", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/users/{userId}/requests",
            produces = {"application/json"}
    )
    public ResponseEntity<ParticipationRequestDto> privateAddParticipationRequest(
            @Parameter(name = "userId", description = "id текущего пользователя", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @NotNull @Parameter(name = "eventId", description = "id события", required = true, in = ParameterIn.QUERY) @Valid @RequestParam(value = "eventId", required = true) Long eventId
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(privateService.privateAddParticipationRequest(userId, eventId));
    }


    /**
     * PATCH /users/{userId}/requests/{requestId}/cancel : Отмена своего запроса на участие в событии
     *
     * @param userId    id текущего пользователя (required)
     * @param requestId id запроса на участие (required)
     * @return Заявка отменена (status code 200)
     * or Запрос не найден или недоступен (status code 404)
     */
    @Operation(
            operationId = "privateCancelParticipationRequest",
            summary = "Отмена своего запроса на участие в событии",
            tags = {"Private: Запросы на участие"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Заявка отменена", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ParticipationRequestDto.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Запрос не найден или недоступен", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/users/{userId}/requests/{requestId}/cancel",
            produces = {"application/json"}
    )
    public ResponseEntity<ParticipationRequestDto> privateCancelParticipationRequest(
            @Parameter(name = "userId", description = "id текущего пользователя", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(name = "requestId", description = "id запроса на участие", required = true, in = ParameterIn.PATH) @PathVariable("requestId") Long requestId
    ) {
        return ResponseEntity.ok(privateService.privateCancelParticipationRequest(userId, requestId));
    }


    /**
     * PATCH /users/{userId}/events/{eventId}/requests : Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     * Обратите внимание: - если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется - нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409) - статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409) - если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить
     *
     * @param userId                          id текущего пользователя (required)
     * @param eventId                         id события текущего пользователя (required)
     * @param eventRequestStatusUpdateRequest Новый статус для заявок на участие в событии текущего пользователя (required)
     * @return Статус заявок изменён (status code 200)
     * or Запрос составлен некорректно (status code 400)
     * or Событие не найдено или недоступно (status code 404)
     * or Достигнут лимит одобренных заявок (status code 409)
     */
    @Operation(
            operationId = "privateChangeRequestStatus",
            summary = "Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя",
            description = "Обратите внимание: - если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется - нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409) - статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409) - если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить",
            tags = {"Private: События"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Статус заявок изменён", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = EventRequestStatusUpdateResult.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Событие не найдено или недоступно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "409", description = "Достигнут лимит одобренных заявок", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/users/{userId}/events/{eventId}/requests",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<EventRequestStatusUpdateResult> privateChangeRequestStatus(
            @Parameter(name = "userId", description = "id текущего пользователя", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(name = "eventId", description = "id события текущего пользователя", required = true, in = ParameterIn.PATH) @PathVariable("eventId") Long eventId,
            @Parameter(name = "EventRequestStatusUpdateRequest", description = "Новый статус для заявок на участие в событии текущего пользователя", required = true) @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest
    ) {
        return ResponseEntity.ok(privateService.privateChangeRequestStatus(userId, eventId, eventRequestStatusUpdateRequest));
    }


    /**
     * GET /users/{userId}/events/{eventId} : Получение полной информации о событии добавленном текущим пользователем
     * В случае, если события с заданным id не найдено, возвращает статус код 404
     *
     * @param userId  id текущего пользователя (required)
     * @param eventId id события (required)
     * @return Событие найдено (status code 200)
     * or Запрос составлен некорректно (status code 400)
     * or Событие не найдено или недоступно (status code 404)
     */
    @Operation(
            operationId = "privateGetEvent",
            summary = "Получение полной информации о событии добавленном текущим пользователем",
            description = "В случае, если события с заданным id не найдено, возвращает статус код 404",
            tags = {"Private: События"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Событие найдено", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = EventFullDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Событие не найдено или недоступно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/users/{userId}/events/{eventId}",
            produces = {"application/json"}
    )
    public ResponseEntity<EventFullDto> privateGetEvent(
            @Parameter(name = "userId", description = "id текущего пользователя", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(name = "eventId", description = "id события", required = true, in = ParameterIn.PATH) @PathVariable("eventId") Long eventId
    ) {
        return ResponseEntity.ok(privateService.privateGetEvent(userId, eventId));
    }


    /**
     * GET /users/{userId}/events/{eventId}/requests : Получение информации о запросах на участие в событии текущего пользователя
     * В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список
     *
     * @param userId  id текущего пользователя (required)
     * @param eventId id события (required)
     * @return Найдены запросы на участие (status code 200)
     * or Запрос составлен некорректно (status code 400)
     */
    @Operation(
            operationId = "privateGetEventParticipants",
            summary = "Получение информации о запросах на участие в событии текущего пользователя",
            description = "В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список",
            tags = {"Private: События"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Найдены запросы на участие", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ParticipationRequestDto.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/users/{userId}/events/{eventId}/requests",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ParticipationRequestDto>> privateGetEventParticipants(
            @Parameter(name = "userId", description = "id текущего пользователя", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(name = "eventId", description = "id события", required = true, in = ParameterIn.PATH) @PathVariable("eventId") Long eventId
    ) {
        return ResponseEntity.ok(privateService.privateGetEventParticipants(userId, eventId));
    }


    /**
     * GET /users/{userId}/events : Получение событий, добавленных текущим пользователем
     * В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     *
     * @param userId id текущего пользователя (required)
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора (optional, public to 0)
     * @param size   количество элементов в наборе (optional, public to 10)
     * @return События найдены (status code 200)
     * or Запрос составлен некорректно (status code 400)
     */
    @Operation(
            operationId = "privateSearchEvents",
            summary = "Получение событий, добавленных текущим пользователем",
            description = "В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список",
            tags = {"Private: События"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "События найдены", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EventShortDto.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/users/{userId}/events",
            produces = {"application/json"}
    )
    public ResponseEntity<List<EventShortDto>> privateSearchEvents(
            @Parameter(name = "userId", description = "id текущего пользователя", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Min(0) @Parameter(name = "from", description = "количество элементов, которые нужно пропустить для формирования текущего набора", in = ParameterIn.QUERY) @Valid @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Min(1) @Parameter(name = "size", description = "количество элементов в наборе", in = ParameterIn.QUERY) @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(privateService.privateSearchEvents(userId, from, size));
    }


    /**
     * GET /users/{userId}/requests : Получение информации о заявках текущего пользователя на участие в чужих событиях
     * В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список
     *
     * @param userId id текущего пользователя (required)
     * @return Найдены запросы на участие (status code 200)
     * or Запрос составлен некорректно (status code 400)
     * or Пользователь не найден (status code 404)
     */
    @Operation(
            operationId = "privateSearchUserRequests",
            summary = "Получение информации о заявках текущего пользователя на участие в чужих событиях",
            description = "В случае, если по заданным фильтрам не найдено ни одной заявки, возвращает пустой список",
            tags = {"Private: Запросы на участие"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Найдены запросы на участие", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ParticipationRequestDto.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/users/{userId}/requests",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ParticipationRequestDto>> privateSearchUserRequests(
            @Parameter(name = "userId", description = "id текущего пользователя", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId
    ) {
        return ResponseEntity.ok(privateService.privateSearchUserRequests(userId));
    }


    /**
     * PATCH /users/{userId}/events/{eventId} : Изменение события добавленного текущим пользователем
     * Обратите внимание: - изменить можно только отмененные события или события в состоянии ожидания модерации (Ожидается код ошибки 409) - дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента (Ожидается код ошибки 409)
     *
     * @param userId                 id текущего пользователя (required)
     * @param eventId                id редактируемого события (required)
     * @param updateEventUserRequest Новые данные события (required)
     * @return Событие обновлено (status code 200)
     * or Запрос составлен некорректно (status code 400)
     * or Событие не найдено или недоступно (status code 404)
     * or Событие не удовлетворяет правилам редактирования (status code 409)
     */
    @Operation(
            operationId = "privateUpdateEvent",
            summary = "Изменение события добавленного текущим пользователем",
            description = "Обратите внимание: - изменить можно только отмененные события или события в состоянии ожидания модерации (Ожидается код ошибки 409) - дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента (Ожидается код ошибки 409) ",
            tags = {"Private: События"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Событие обновлено", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = EventFullDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Событие не найдено или недоступно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "409", description = "Событие не удовлетворяет правилам редактирования", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/users/{userId}/events/{eventId}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<EventFullDto> privateUpdateEvent(
            @Parameter(name = "userId", description = "id текущего пользователя", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(name = "eventId", description = "id редактируемого события", required = true, in = ParameterIn.PATH) @PathVariable("eventId") Long eventId,
            @Parameter(name = "UpdateEventUserRequest", description = "Новые данные события", required = true) @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest
    ) {
        return ResponseEntity.ok(privateService.privateUpdateEvent(userId, eventId, updateEventUserRequest));
    }

    /**
     * POST /users/{userId}/events/{eventId}/reaction : Добавление реакции
     *
     * @param userId  id текущего пользователя (required)
     * @param eventId id редактируемого события (required)
     * @return Реакция добавлена (status code 201)
     * or Запрос составлен некорректно (status code 400)
     * or Событие или пользователь не найдено или недоступно (status code 404)
     * or Нарушены правила добавления реакции (status code 409)
     */
    @Operation(
            operationId = "privateAddReaction",
            summary = "Добавление реакции",
            description = "Реакции могут две: лайк или дизлайк",
            tags = {"Private: События"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Реакция добавлена", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = NewEventReactionDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Событие или пользователь не найдено или недоступно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "409", description = "Нарушены правила добавления реакции", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/users/{userId}/events/{eventId}/reaction",
            produces = {"application/json"}
    )
    public ResponseEntity<NewEventReactionDto> privateAddReaction(
            @Parameter(name = "userId", description = "id текущего пользователя", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(name = "eventId", description = "id редактируемого события", required = true, in = ParameterIn.PATH) @PathVariable("eventId") Long eventId,
            @Parameter(name = "reaction", description = "реакция", in = ParameterIn.QUERY) @Valid @RequestParam(value = "reaction", required = false) EventReactionEnum reaction
    ) {
        switch (reaction) {
            case LIKE:
                return ResponseEntity.status(HttpStatus.CREATED).body(privateService.addLikeOrDislikeEvent(userId, eventId, true));
            case DISLIKE:
                return ResponseEntity.status(HttpStatus.CREATED).body(privateService.addLikeOrDislikeEvent(userId, eventId, false));
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    /**
     * DELETE /users/{userId}/events/{eventId}/reaction : Удаление реакции
     *
     * @param userId  id текущего пользователя (required)
     * @param eventId id редактируемого события (required)
     * @return Реакция удалена (status code 200)
     * or Запрос составлен некорректно (status code 400)
     * or Событие или пользователь не найдено или недоступно (status code 404)
     */
    @Operation(
            operationId = "privateRemoveReaction",
            summary = "Удаление реакции",
            description = "Удаление реакции",
            tags = {"Private: События"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Реакция удалена", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = NewEventReactionDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Событие или пользователь не найдено или недоступно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/users/{userId}/events/{eventId}/reaction",
            produces = {"application/json"}
    )
    public ResponseEntity<Void> privateRemoveReaction(
            @Parameter(name = "userId", description = "id текущего пользователя", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId,
            @Parameter(name = "eventId", description = "id редактируемого события", required = true, in = ParameterIn.PATH) @PathVariable("eventId") Long eventId
    ) {
        privateService.removeLikeOrDislikeEvent(userId, eventId);

        return ResponseEntity.ok().build();
    }

}
