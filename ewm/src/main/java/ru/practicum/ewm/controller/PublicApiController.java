package ru.practicum.ewm.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.ewm.controller.dto.*;
import ru.practicum.ewm.service.PublicService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Controller
@Validated
public class PublicApiController {

    private final PublicService publicService;

    public PublicApiController(PublicService publicService) {
        this.publicService = publicService;
    }

    /**
     * GET /categories/{catId} : Получение информации о категории по её идентификатору
     * В случае, если категории с заданным id не найдено, возвращает статус код 404
     *
     * @param catId id категории (required)
     * @return Категория найдена (status code 200)
     * or Запрос составлен некорректно (status code 400)
     * or Категория не найдена или недоступна (status code 404)
     */
    @Operation(
            operationId = "publicGetCategory",
            summary = "Получение информации о категории по её идентификатору",
            description = "В случае, если категории с заданным id не найдено, возвращает статус код 404",
            tags = {"Public: Категории"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Категория найдена", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Категория не найдена или недоступна", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/categories/{catId}",
            produces = {"application/json"}
    )
    public ResponseEntity<CategoryDto> publicGetCategory(
            @Parameter(name = "catId", description = "id категории", required = true, in = ParameterIn.PATH) @PathVariable("catId") Long catId
    ) {
        return publicService.publicGetCategory(catId);
    }


    /**
     * GET /compilations/{compId} : Получение подборки событий по его id
     * В случае, если подборки с заданным id не найдено, возвращает статус код 404
     *
     * @param compId id подборки (required)
     * @return Подборка событий найдена (status code 200)
     * or Запрос составлен некорректно (status code 400)
     * or Подборка не найдена или недоступна (status code 404)
     */
    @Operation(
            operationId = "publicGetCompilation",
            summary = "Получение подборки событий по его id",
            description = "В случае, если подборки с заданным id не найдено, возвращает статус код 404",
            tags = {"Public: Подборки событий"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Подборка событий найдена", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CompilationDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Подборка не найдена или недоступна", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/compilations/{compId}",
            produces = {"application/json"}
    )
    public ResponseEntity<CompilationDto> publicGetCompilation(
            @Parameter(name = "compId", description = "id подборки", required = true, in = ParameterIn.PATH) @PathVariable("compId") Long compId
    ) {
        return publicService.publicGetCompilation(compId);
    }


    /**
     * GET /events/{id} : Получение подробной информации об опубликованном событии по его идентификатору
     * Обратите внимание: - событие должно быть опубликовано - информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики  В случае, если события с заданным id не найдено, возвращает статус код 404
     *
     * @param id id события (required)
     * @return Событие найдено (status code 200)
     * or Запрос составлен некорректно (status code 400)
     * or Событие не найдено или недоступно (status code 404)
     */
    @Operation(
            operationId = "publicGetEvent",
            summary = "Получение подробной информации об опубликованном событии по его идентификатору",
            description = "Обратите внимание: - событие должно быть опубликовано - информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики  В случае, если события с заданным id не найдено, возвращает статус код 404",
            tags = {"Public: События"},
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
            value = "/events/{id}",
            produces = {"application/json"}
    )
    public ResponseEntity<EventFullDto> publicGetEvent(
            @Parameter(name = "id", description = "id события", required = true, in = ParameterIn.PATH) @PathVariable("id") Long id
    ) {
        return publicService.publicGetEvent(id);
    }


    /**
     * GET /categories : Получение категорий
     * В случае, если по заданным фильтрам не найдено ни одной категории, возвращает пустой список
     *
     * @param from количество категорий, которые нужно пропустить для формирования текущего набора (optional, public to 0)
     * @param size количество категорий в наборе (optional, public to 10)
     * @return Категории найдены (status code 200)
     * or Запрос составлен некорректно (status code 400)
     */
    @Operation(
            operationId = "publicSearchCategories",
            summary = "Получение категорий",
            description = "В случае, если по заданным фильтрам не найдено ни одной категории, возвращает пустой список",
            tags = {"Public: Категории"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Категории найдены", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/categories",
            produces = {"application/json"}
    )
    public ResponseEntity<List<CategoryDto>> publicSearchCategories(
            @Min(0) @Parameter(name = "from", description = "количество категорий, которые нужно пропустить для формирования текущего набора", in = ParameterIn.QUERY) @Valid @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Min(1) @Parameter(name = "size", description = "количество категорий в наборе", in = ParameterIn.QUERY) @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        return publicService.publicSearchCategories(from, size);
    }


    /**
     * GET /compilations : Получение подборок событий
     * В случае, если по заданным фильтрам не найдено ни одной подборки, возвращает пустой список
     *
     * @param pinned искать только закрепленные/не закрепленные подборки (optional)
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора (optional, public to 0)
     * @param size   количество элементов в наборе (optional, public to 10)
     * @return Найдены подборки событий (status code 200)
     * or Запрос составлен некорректно (status code 400)
     */
    @Operation(
            operationId = "publicSearchCompilations",
            summary = "Получение подборок событий",
            description = "В случае, если по заданным фильтрам не найдено ни одной подборки, возвращает пустой список",
            tags = {"Public: Подборки событий"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Найдены подборки событий", content = {
                            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CompilationDto.class)))
                    }),
                    @ApiResponse(responseCode = "400", description = "Запрос составлен некорректно", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class))
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/compilations",
            produces = {"application/json"}
    )
    public ResponseEntity<List<CompilationDto>> publicSearchCompilations(
            @Parameter(name = "pinned", description = "искать только закрепленные/не закрепленные подборки", in = ParameterIn.QUERY) @Valid @RequestParam(value = "pinned", required = false) Boolean pinned,
            @Min(0) @Parameter(name = "from", description = "количество элементов, которые нужно пропустить для формирования текущего набора", in = ParameterIn.QUERY) @Valid @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Min(1) @Parameter(name = "size", description = "количество элементов в наборе", in = ParameterIn.QUERY) @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        return publicService.publicSearchCompilations(pinned, from, size);
    }


    /**
     * GET /events : Получение событий с возможностью фильтрации
     * Обратите внимание:  - это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события - текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв - если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени - информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики  В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
     *
     * @param text          текст для поиска в содержимом аннотации и подробном описании события (optional)
     * @param categories    список идентификаторов категорий в которых будет вестись поиск (optional)
     * @param paid          поиск только платных/бесплатных событий (optional)
     * @param rangeStart    дата и время не раньше которых должно произойти событие (optional)
     * @param rangeEnd      дата и время не позже которых должно произойти событие (optional)
     * @param onlyAvailable только события у которых не исчерпан лимит запросов на участие (optional, public to false)
     * @param sort          Вариант сортировки: по дате события или по количеству просмотров (optional)
     * @param from          количество событий, которые нужно пропустить для формирования текущего набора (optional, public to 0)
     * @param size          количество событий в наборе (optional, public to 10)
     * @return События найдены (status code 200)
     * or Запрос составлен некорректно (status code 400)
     */
    @Operation(
            operationId = "publicSearchEvents",
            summary = "Получение событий с возможностью фильтрации",
            description = "Обратите внимание:  - это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события - текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв - если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени - информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики  В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список",
            tags = {"Public: События"},
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
            value = "/events",
            produces = {"application/json"}
    )
    public ResponseEntity<List<EventShortDto>> publicSearchEvents(
            @Parameter(name = "text", description = "текст для поиска в содержимом аннотации и подробном описании события", in = ParameterIn.QUERY) @Valid @RequestParam(value = "text", required = false) String text,
            @Parameter(name = "categories", description = "список идентификаторов категорий в которых будет вестись поиск", in = ParameterIn.QUERY) @Valid @RequestParam(value = "categories", required = false) List<Long> categories,
            @Parameter(name = "paid", description = "поиск только платных/бесплатных событий", in = ParameterIn.QUERY) @Valid @RequestParam(value = "paid", required = false) Boolean paid,
            @Parameter(name = "rangeStart", description = "дата и время не раньше которых должно произойти событие", in = ParameterIn.QUERY) @Valid @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @Parameter(name = "rangeEnd", description = "дата и время не позже которых должно произойти событие", in = ParameterIn.QUERY) @Valid @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @Parameter(name = "onlyAvailable", description = "только события у которых не исчерпан лимит запросов на участие", in = ParameterIn.QUERY) @Valid @RequestParam(value = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
            @Parameter(name = "sort", description = "Вариант сортировки: по дате события или по количеству просмотров", in = ParameterIn.QUERY) @Valid @RequestParam(value = "sort", required = false) PublicEventSortEnum sort,
            @Min(0) @Parameter(name = "from", description = "количество событий, которые нужно пропустить для формирования текущего набора", in = ParameterIn.QUERY) @Valid @RequestParam(value = "from", required = false, defaultValue = "0") Integer from,
            @Min(1) @Parameter(name = "size", description = "количество событий в наборе", in = ParameterIn.QUERY) @Valid @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        return publicService.publicSearchEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }
}
