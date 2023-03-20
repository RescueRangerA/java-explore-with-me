package ru.practicum.stats.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import ru.practicum.stats.client.invoke.ApiClient;
import ru.practicum.stats.controller.dto.EndpointHitDto;
import ru.practicum.stats.controller.dto.ViewStats;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
     * @param start  Дата и время начала диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;) (required)
     * @param end    Дата и время конца диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;) (required)
     * @param uris   Список uri для которых нужно выгрузить статистику (optional)
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip) (optional, default to false)
     * @return List&lt;ViewStats&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) throws RestClientException {
        return getStatsWithHttpInfo(start, end, uris, unique).getBody();
    }

    /**
     * Получение статистики по посещениям. Обратите внимание: значение даты и времени нужно закодировать (например используя java.net.URLEncoder.encode)
     *
     * <p><b>200</b> - Статистика собрана
     *
     * @param start  Дата и время начала диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;) (required)
     * @param end    Дата и время конца диапазона за который нужно выгрузить статистику (в формате \&quot;yyyy-MM-dd HH:mm:ss\&quot;) (required)
     * @param uris   Список uri для которых нужно выгрузить статистику (optional)
     * @param unique Нужно ли учитывать только уникальные посещения (только с уникальным ip) (optional, default to false)
     * @return ResponseEntity&lt;List&lt;ViewStats&gt;&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<ViewStats>> getStatsWithHttpInfo(String start, String end, List<String> uris, Boolean unique) throws RestClientException {
        Object localVarPostBody = null;

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'start' when calling getStats");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'end' when calling getStats");
        }


        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "start", start));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "end", end));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("multi".toUpperCase(Locale.ROOT)), "uris", uris));
        localVarQueryParams.putAll(apiClient.parameterToMultiValueMap(null, "unique", unique));

        final String[] localVarAccepts = {
                "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {};
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[]{};

        ParameterizedTypeReference<List<ViewStats>> localReturnType = new ParameterizedTypeReference<List<ViewStats>>() {
        };
        return apiClient.invokeAPI("/stats", HttpMethod.GET, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }

    /**
     * Сохранение информации о том, что к эндпоинту был запрос
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем. Название сервиса, uri и ip пользователя указаны в теле запроса.
     * <p><b>201</b> - Информация сохранена
     *
     * @param endpointHitDto данные запроса (required)
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void hit(EndpointHitDto endpointHitDto) throws RestClientException {
        hitWithHttpInfo(endpointHitDto);
    }

    /**
     * Сохранение информации о том, что к эндпоинту был запрос
     * Сохранение информации о том, что на uri конкретного сервиса был отправлен запрос пользователем. Название сервиса, uri и ip пользователя указаны в теле запроса.
     * <p><b>201</b> - Информация сохранена
     *
     * @param endpointHitDto данные запроса (required)
     * @return ResponseEntity&lt;Void&gt;
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> hitWithHttpInfo(EndpointHitDto endpointHitDto) throws RestClientException {
        Object localVarPostBody = endpointHitDto;

        // verify the required parameter 'endpointHitDto' is set
        if (endpointHitDto == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'endpointHitDto' when calling hit");
        }


        final MultiValueMap<String, String> localVarQueryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders localVarHeaderParams = new HttpHeaders();
        final MultiValueMap<String, String> localVarCookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> localVarFormParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = {};
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {
                "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[]{};

        ParameterizedTypeReference<Void> localReturnType = new ParameterizedTypeReference<Void>() {
        };
        return apiClient.invokeAPI("/hit", HttpMethod.POST, Collections.<String, Object>emptyMap(), localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAccept, localVarContentType, localVarAuthNames, localReturnType);
    }
}
