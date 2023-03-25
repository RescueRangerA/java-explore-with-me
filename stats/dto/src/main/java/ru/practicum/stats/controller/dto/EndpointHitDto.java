package ru.practicum.stats.controller.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class EndpointHitDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("app")
    private String app;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("timestamp")
    private String timestamp;

    public EndpointHitDto id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Идентификатор записи
     *
     * @return id
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EndpointHitDto app(String app) {
        this.app = app;
        return this;
    }

    /**
     * Идентификатор сервиса для которого записывается информация
     *
     * @return app
     */

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public EndpointHitDto uri(String uri) {
        this.uri = uri;
        return this;
    }

    /**
     * URI для которого был осуществлен запрос
     *
     * @return uri
     */

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public EndpointHitDto ip(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * IP-адрес пользователя, осуществившего запрос
     *
     * @return ip
     */

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public EndpointHitDto timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Дата и время, когда был совершен запрос к эндпоинту (в формате \"yyyy-MM-dd HH:mm:ss\")
     *
     * @return timestamp
     */

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EndpointHitDto endpointHitDto = (EndpointHitDto) o;
        return Objects.equals(this.id, endpointHitDto.id) &&
                Objects.equals(this.app, endpointHitDto.app) &&
                Objects.equals(this.uri, endpointHitDto.uri) &&
                Objects.equals(this.ip, endpointHitDto.ip) &&
                Objects.equals(this.timestamp, endpointHitDto.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, app, uri, ip, timestamp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EndpointHitDto {\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    app: ").append(toIndentedString(app)).append("\n");
        sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
        sb.append("    ip: ").append(toIndentedString(ip)).append("\n");
        sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

