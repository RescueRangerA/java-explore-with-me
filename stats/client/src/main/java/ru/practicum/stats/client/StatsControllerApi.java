package ru.practicum.stats.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.stats.client.invoke.ApiClient;
import ru.practicum.stats.controller.dto.EndpointHitDto;
import ru.practicum.stats.controller.dto.ViewStats;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class StatsControllerApi {
    private ApiClient apiClient;

    public StatsControllerApi() {
        this(new ApiClient());
    }

    @Autowired
    public StatsControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Получение статистики по посещениям. Обратите внимание: значение даты и времени нужно закодировать (например используя java.net.URLEncoder.encode)
     *
     * <p><b>200</b> - Статистика собрана
     *
     * @param start  Дата и время начала диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;)
     * @param end    Дата и время конца диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;)
     * @param uris   Список uri для которых нужно выгрузить статистику
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip)
     * @return List&lt;ViewStats&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getStatsRequestCreation(String start, String end, List<String> uris, Boolean unique) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'start' is set
        if (start == null) {
            throw new WebClientResponseException("Missing the required parameter 'start' when calling getStats", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'end' is set
        if (end == null) {
            throw new WebClientResponseException("Missing the required parameter 'end' when calling getStats", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));
        queryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("multi".toUpperCase(Locale.ROOT)), "uris", uris));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "unique", unique));

        final String[] localVarAccepts = {
                "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {};
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[]{};

        ParameterizedTypeReference<ViewStats> localVarReturnType = new ParameterizedTypeReference<ViewStats>() {
        };
        return apiClient.invokeAPI("/stats", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Получение статистики по посещениям. Обратите внимание: значение даты и времени нужно закодировать (например используя java.net.URLEncoder.encode)
     *
     * <p><b>200</b> - Статистика собрана
     *
     * @param start  Дата и время начала диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;)
     * @param end    Дата и время конца диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;)
     * @param uris   Список uri для которых нужно выгрузить статистику
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip)
     * @return List&lt;ViewStats&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Flux<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) throws WebClientResponseException {
        ParameterizedTypeReference<ViewStats> localVarReturnType = new ParameterizedTypeReference<ViewStats>() {
        };
        return getStatsRequestCreation(start, end, uris, unique).bodyToFlux(localVarReturnType);
    }

    /**
     * Получение статистики по посещениям. Обратите внимание: значение даты и времени нужно закодировать (например используя java.net.URLEncoder.encode)
     *
     * <p><b>200</b> - Статистика собрана
     *
     * @param start  Дата и время начала диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;)
     * @param end    Дата и время конца диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;)
     * @param uris   Список uri для которых нужно выгрузить статистику
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip)
     * @return ResponseEntity&lt;List&lt;ViewStats&gt;&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<List<ViewStats>>> getStatsWithHttpInfo(String start, String end, List<String> uris, Boolean unique) throws WebClientResponseException {
        ParameterizedTypeReference<ViewStats> localVarReturnType = new ParameterizedTypeReference<ViewStats>() {
        };
        return getStatsRequestCreation(start, end, uris, unique).toEntityList(localVarReturnType);
    }

    /**
     * Получение статистики по посещениям. Обратите внимание: значение даты и времени нужно закодировать (например используя java.net.URLEncoder.encode)
     *
     * <p><b>200</b> - Статистика собрана
     *
     * @param start  Дата и время начала диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;)
     * @param end    Дата и время конца диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;)
     * @param uris   Список uri для которых нужно выгрузить статистику
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip)
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getStatsWithResponseSpec(String start, String end, List<String> uris, Boolean unique) throws WebClientResponseException {
        return getStatsRequestCreation(start, end, uris, unique);
    }

    /**
     * Сохранение информации о том, что к эндпоинту был запрос
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем. Название сервиса, uri и ip пользователя указаны в теле запроса.
     * <p><b>201</b> - Информация сохранена
     *
     * @param endpointHitDto данные запроса
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec hitRequestCreation(EndpointHitDto endpointHitDto) throws WebClientResponseException {
        Object postBody = endpointHitDto;
        // verify the required parameter 'endpointHitDto' is set
        if (endpointHitDto == null) {
            throw new WebClientResponseException("Missing the required parameter 'endpointHitDto' when calling hit", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = {};
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {
                "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[]{};

        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {
        };
        return apiClient.invokeAPI("/hit", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Сохранение информации о том, что к эндпоинту был запрос
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем. Название сервиса, uri и ip пользователя указаны в теле запроса.
     * <p><b>201</b> - Информация сохранена
     *
     * @param endpointHitDto данные запроса
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Void> hit(EndpointHitDto endpointHitDto) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {
        };
        return hitRequestCreation(endpointHitDto).bodyToMono(localVarReturnType);
    }

    /**
     * Сохранение информации о том, что к эндпоинту был запрос
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем. Название сервиса, uri и ip пользователя указаны в теле запроса.
     * <p><b>201</b> - Информация сохранена
     *
     * @param endpointHitDto данные запроса
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Void>> hitWithHttpInfo(EndpointHitDto endpointHitDto) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {
        };
        return hitRequestCreation(endpointHitDto).toEntity(localVarReturnType);
    }

    /**
     * Сохранение информации о том, что к эндпоинту был запрос
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем. Название сервиса, uri и ip пользователя указаны в теле запроса.
     * <p><b>201</b> - Информация сохранена
     *
     * @param endpointHitDto данные запроса
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec hitWithResponseSpec(EndpointHitDto endpointHitDto) throws WebClientResponseException {
        return hitRequestCreation(endpointHitDto);
    }
}
