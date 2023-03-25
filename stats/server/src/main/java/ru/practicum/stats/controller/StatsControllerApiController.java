package ru.practicum.stats.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.controller.dto.EndpointHitDto;
import ru.practicum.stats.controller.dto.ViewStats;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.practicum.stats.service.StatsService;

import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.List;

@Controller
@Validated
public class StatsControllerApiController {
    private final StatsService statsService;

    public StatsControllerApiController(StatsService statsService) {
        this.statsService = statsService;
    }

    /**
     * GET /stats : Получение статистики по посещениям. Обратите внимание: значение даты и времени нужно закодировать (например используя java.net.URLEncoder.encode)
     *
     * @param start  Дата и время начала диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;) (required)
     * @param end    Дата и время конца диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;) (required)
     * @param uris   Список uri для которых нужно выгрузить статистику (optional)
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip) (optional, default to false)
     * @return Статистика собрана (status code 200)
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/stats",
            produces = {"application/json"}
    )
    public ResponseEntity<List<ViewStats>> getStats(
            @NotNull @Valid @RequestParam(value = "start", required = true) String start,
            @NotNull @Valid @RequestParam(value = "end", required = true) String end,
            @Valid @RequestParam(value = "uris", required = false) List<String> uris,
            @Valid @RequestParam(value = "unique", required = false, defaultValue = "false") Boolean unique
    ) {
        return statsService.getStats(start, end, uris, unique);
    }


    /**
     * POST /hit : Сохранение информации о том, что к эндпоинту был запрос
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем. Название сервиса, uri и ip пользователя указаны в теле запроса.
     *
     * @param endpointHitDto данные запроса (required)
     * @return Информация сохранена (status code 201)
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/hit",
            consumes = {"application/json"}
    )
    public ResponseEntity<Void> hit(
            @Valid @RequestBody EndpointHitDto endpointHitDto
    ) {
        return statsService.hit(endpointHitDto);
    }

}
