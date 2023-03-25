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
import ru.practicum.ewm.service.AdminService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Controller
@Validated
public class AdminApiController {

    private final AdminService adminService;

    public AdminApiController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * POST /admin/categories : Добавление новой категории
     * Обратите внимание: имя категории должно быть уникальным
     *
     * @param newCategoryDto данные добавляемой категории (required)
     * @return Категория добавлена (status code 201)
     * or Запрос составлен некорректно (status code 400)
     * or Нарушение целостности данных (status code 409)
     */
    @Operation(
            operationId = "adminAddCategory",
            summary = "Добавление новой категории",
            description = "Обратите внимание: имя категории должно быть уникальным",
            tags = {"Admin: Категории"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Категория добавлена", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "409", description = "Нарушение целостности данных", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/admin/categories",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<CategoryDto> adminAddCategory(
            @Parameter(name = "NewCategoryDto", description = "данные добавляемой категории", required = true) @Valid @RequestBody NewCategoryDto newCategoryDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.adminAddCategory(newCategoryDto));
    }


    /**
     * POST /admin/compilations : Добавление новой подборки (подборка может не содержать событий)
     *
     * @param newCompilationDto данные новой подборки (required)
     * @return Подборка добавлена (status code 201)
     * or Запрос составлен некорректно (status code 400)
     * or Нарушение целостности данных (status code 409)
     */
    @Operation(
            operationId = "adminAddCompilation",
            summary = "Добавление новой подборки (подборка может не содержать событий)",
            tags = {"Admin: Подборки событий"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Подборка добавлена", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CompilationDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "409", description = "Нарушение целостности данных", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/admin/compilations",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<CompilationDto> adminAddCompilation(
            @Parameter(name = "NewCompilationDto", description = "данные новой подборки", required = true) @Valid @RequestBody NewCompilationDto newCompilationDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.adminAddCompilation(newCompilationDto));
    }


    /**
     * POST /admin/users : Добавление нового пользователя
     *
     * @param newUserRequest Данные добавляемого пользователя (required)
     * @return Пользователь зарегистрирован (status code 201)
     * or Запрос составлен некорректно (status code 400)
     * or Нарушение целостности данных (status code 409)
     */
    @Operation(
            operationId = "adminAddUser",
            summary = "Добавление нового пользователя",
            tags = {"Admin: Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Пользователь зарегистрирован", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "409", description = "Нарушение целостности данных", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/admin/users",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<UserDto> adminAddUser(
            @Parameter(name = "NewUserRequest", description = "Данные добавляемого пользователя", required = true) @Valid @RequestBody NewUserRequest newUserRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.adminAddUser(newUserRequest));
    }


    /**
     * DELETE /admin/categories/{catId} : Удаление категории
     * Обратите внимание: с категорией не должно быть связано ни одного события.
     *
     * @param catId id категории (required)
     * @return Категория удалена (status code 204)
     * or Категория не найдена или недоступна (status code 404)
     * or Существуют события, связанные с категорией (status code 409)
     */
    @Operation(
            operationId = "adminDeleteCategory",
            summary = "Удаление категории",
            description = "Обратите внимание: с категорией не должно быть связано ни одного события.",
            tags = {"Admin: Категории"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Категория удалена"),
                    @ApiResponse(responseCode = "404", description = "Категория не найдена или недоступна", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "409", description = "Существуют события, связанные с категорией", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/admin/categories/{catId}",
            produces = {"application/json"}
    )
    public ResponseEntity<Void> adminDeleteCategory(
            @Parameter(name = "catId", description = "id категории", required = true, in = ParameterIn.PATH) @PathVariable("catId") Long catId
    ) {
        adminService.adminDeleteCategory(catId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * DELETE /admin/compilations/{compId} : Удаление подборки
     *
     * @param compId id подборки (required)
     * @return Подборка удалена (status code 204)
     * or Подборка не найдена или недоступна (status code 404)
     */
    @Operation(
            operationId = "adminDeleteCompilation",
            summary = "Удаление подборки",
            tags = {"Admin: Подборки событий"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Подборка удалена"),
                    @ApiResponse(responseCode = "404", description = "Подборка не найдена или недоступна", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/admin/compilations/{compId}",
            produces = {"application/json"}
    )
    public ResponseEntity<Void> adminDeleteCompilation(
            @Parameter(name = "compId", description = "id подборки", required = true, in = ParameterIn.PATH) @PathVariable("compId") Long compId
    ) {
        adminService.adminDeleteCompilation(compId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * DELETE /admin/users/{userId} : Удаление пользователя
     *
     * @param userId id пользователя (required)
     * @return Пользователь удален (status code 204)
     * or Пользователь не найден или недоступен (status code 404)
     */
    @Operation(
            operationId = "adminDeleteUser",
            summary = "Удаление пользователя",
            tags = {"Admin: Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Пользователь удален"),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден или недоступен", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/admin/users/{userId}",
            produces = {"application/json"}
    )
    public ResponseEntity<Void> adminDeleteUser(
            @Parameter(name = "userId", description = "id пользователя", required = true, in = ParameterIn.PATH) @PathVariable("userId") Long userId
    ) {
        adminService.adminDeleteUser(userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    /**
     * GET /admin/events : Поиск событий
     * Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия  В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     *
     * @param users      список id пользователей, чьи события нужно найти (optional)
     * @param states     список состояний в которых находятся искомые события (optional)
     * @param categories список id категорий в которых будет вестись поиск (optional)
     * @param rangeStart дата и время не раньше которых должно произойти событие (optional)
     * @param rangeEnd   дата и время не позже которых должно произойти событие (optional)
     * @param from       количество событий, которые нужно пропустить для формирования текущего набора (optional, default to 0)
     * @param size       количество событий в наборе (optional, default to 10)
     * @return События найдены (status code 200)
     * or Запрос составлен некорректно (status code 400)
     */
    @Operation(
            operationId = "adminSearchEvents",
            summary = "Поиск событий",
            description = "Эндпоинт возвращает полную информацию обо всех событиях подходящих под переданные условия  В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список",
            tags = {"Admin: События"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "События найдены", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EventFullDto.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/admin/events",
            produces = {"application/json"}
    )
    public ResponseEntity<List<EventFullDto>> adminSearchEvents(
            @Parameter(name = "users", description = "список id пользователей, чьи события нужно найти", in = ParameterIn.QUERY) @Valid @RequestParam(value = "users", required = false) List<Long> users,
            @Parameter(name = "states", description = "список состояний в которых находятся искомые события", in = ParameterIn.QUERY) @Valid @RequestParam(value = "states", required = false) List<EventStatusRequestEnum> states,
            @Parameter(name = "categories", description = "список id категорий в которых будет вестись поиск", in = ParameterIn.QUERY) @Valid @RequestParam(value = "categories", required = false) List<Long> categories,
            @Parameter(name = "rangeStart", description = "дата и время не раньше которых должно произойти событие", in = ParameterIn.QUERY) @Valid @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @Parameter(name = "rangeEnd", description = "дата и время не позже которых должно произойти событие", in = ParameterIn.QUERY) @Valid @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @Min(0) @Parameter(name = "from", description = "количество событий, которые нужно пропустить для формирования текущего набора", in = ParameterIn.QUERY) @Valid @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Min(1) @Parameter(name = "size", description = "количество событий в наборе", in = ParameterIn.QUERY) @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(adminService.adminSearchEvents(users, states, categories, rangeStart, rangeEnd, from, size));
    }


    /**
     * GET /admin/users : Получение информации о пользователях
     * Возвращает информацию обо всех пользователях (учитываются параметры ограничения выборки), либо о конкретных (учитываются указанные идентификаторы)  В случае, если по заданным фильтрам не найдено ни одного пользователя, возвращает пустой список
     *
     * @param ids  id пользователей (optional)
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора (optional, default to 0)
     * @param size количество элементов в наборе (optional, default to 10)
     * @return Пользователи найдены (status code 200)
     * or Запрос составлен некорректно (status code 400)
     */
    @Operation(
            operationId = "adminSearchUsers",
            summary = "Получение информации о пользователях",
            description = "Возвращает информацию обо всех пользователях (учитываются параметры ограничения выборки), либо о конкретных (учитываются указанные идентификаторы)  В случае, если по заданным фильтрам не найдено ни одного пользователя, возвращает пустой список",
            tags = {"Admin: Пользователи"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пользователи найдены", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/admin/users",
            produces = {"application/json"}
    )
    public ResponseEntity<List<UserDto>> adminSearchUsers(
            @Parameter(name = "ids", description = "id пользователей", in = ParameterIn.QUERY) @Valid @RequestParam(value = "ids", required = false) List<Long> ids,
            @Min(0) @Parameter(name = "from", description = "количество элементов, которые нужно пропустить для формирования текущего набора", in = ParameterIn.QUERY) @Valid @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Min(1) @Parameter(name = "size", description = "количество элементов в наборе", in = ParameterIn.QUERY) @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(adminService.adminSearchUsers(ids, from, size));
    }


    /**
     * PATCH /admin/categories/{catId} : Изменение категории
     * Обратите внимание: имя категории должно быть уникальным
     *
     * @param catId       id категории (required)
     * @param categoryDto Данные категории для изменения (required)
     * @return Данные категории изменены (status code 200)
     * or Категория не найдена или недоступна (status code 404)
     * or Нарушение целостности данных (status code 409)
     */
    @Operation(
            operationId = "adminUpdateCategory",
            summary = "Изменение категории",
            description = "Обратите внимание: имя категории должно быть уникальным",
            tags = {"Admin: Категории"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные категории изменены", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Категория не найдена или недоступна", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "409", description = "Нарушение целостности данных", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/admin/categories/{catId}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<CategoryDto> adminUpdateCategory(
            @Parameter(name = "catId", description = "id категории", required = true, in = ParameterIn.PATH) @PathVariable("catId") Long catId,
            @Parameter(name = "CategoryDto", description = "Данные категории для изменения", required = true) @Valid @RequestBody CategoryDto categoryDto
    ) {
        return ResponseEntity.ok(adminService.adminUpdateCategory(catId, categoryDto));
    }


    /**
     * PATCH /admin/compilations/{compId} : Обновить информацию о подборке
     *
     * @param compId                   id подборки (required)
     * @param updateCompilationRequest данные для обновления подборки (required)
     * @return Подборка обновлена (status code 200)
     * or Подборка не найдена или недоступна (status code 404)
     */
    @Operation(
            operationId = "adminUpdateCompilation",
            summary = "Обновить информацию о подборке",
            tags = {"Admin: Подборки событий"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Подборка обновлена", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CompilationDto.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Подборка не найдена или недоступна", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.PATCH,
            value = "/admin/compilations/{compId}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<CompilationDto> adminUpdateCompilation(
            @Parameter(name = "compId", description = "id подборки", required = true, in = ParameterIn.PATH) @PathVariable("compId") Long compId,
            @Parameter(name = "UpdateCompilationRequest", description = "данные для обновления подборки", required = true) @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest
    ) {
        return ResponseEntity.ok(adminService.adminUpdateCompilation(compId, updateCompilationRequest));
    }


    /**
     * PATCH /admin/events/{eventId} : Редактирование данных события и его статуса (отклонение/публикация).
     * Редактирование данных любого события администратором. Валидация данных не требуется. Обратите внимание:  - дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409) - событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409) - событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
     *
     * @param eventId                 id события (required)
     * @param updateEventAdminRequest Данные для изменения информации о событии (required)
     * @return Событие отредактировано (status code 200)
     * or Событие не найдено или недоступно (status code 404)
     * or Событие не удовлетворяет правилам редактирования (status code 409)
     */
    @Operation(
            operationId = "adminUpdateEvent",
            summary = "Редактирование данных события и его статуса (отклонение/публикация).",
            description = "Редактирование данных любого события администратором. Валидация данных не требуется. Обратите внимание:  - дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409) - событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409) - событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)",
            tags = {"Admin: События"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Событие отредактировано", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = EventFullDto.class))
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
            value = "/admin/events/{eventId}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    public ResponseEntity<EventFullDto> adminUpdateEvent(
            @Parameter(name = "eventId", description = "id события", required = true, in = ParameterIn.PATH) @PathVariable("eventId") Long eventId,
            @Parameter(name = "UpdateEventAdminRequest", description = "Данные для изменения информации о событии", required = true) @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest
    ) {
        return ResponseEntity.ok(adminService.adminUpdateEvent(eventId, updateEventAdminRequest));
    }

}
