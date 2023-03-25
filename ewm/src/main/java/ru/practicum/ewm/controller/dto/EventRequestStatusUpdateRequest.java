package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Изменение статуса запроса на участие в событии текущего пользователя
 */
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor

@Schema(name = "EventRequestStatusUpdateRequest", description = "Изменение статуса запроса на участие в событии текущего пользователя")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-03-20T18:18:39.930348+04:00[Europe/Saratov]")
public class EventRequestStatusUpdateRequest {

    @JsonProperty("requestIds")
    @Valid
    private List<Long> requestIds = null;

    /**
     * Новый статус запроса на участие в событии текущего пользователя
     */
    public enum StatusEnum {
        CONFIRMED("CONFIRMED"),

        REJECTED("REJECTED");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static StatusEnum fromValue(String value) {
            for (StatusEnum b : StatusEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    @JsonProperty("status")
    private StatusEnum status;

    public EventRequestStatusUpdateRequest requestIds(List<Long> requestIds) {
        this.requestIds = requestIds;
        return this;
    }

    public EventRequestStatusUpdateRequest addRequestIdsItem(Long requestIdsItem) {
        if (this.requestIds == null) {
            this.requestIds = new ArrayList<>();
        }
        this.requestIds.add(requestIdsItem);
        return this;
    }

    /**
     * Идентификаторы запросов на участие в событии текущего пользователя
     *
     * @return requestIds
     */

    @Schema(name = "requestIds", example = "[1,2,3]", description = "Идентификаторы запросов на участие в событии текущего пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public List<Long> getRequestIds() {
        return requestIds;
    }

    public void setRequestIds(List<Long> requestIds) {
        this.requestIds = requestIds;
    }

    public EventRequestStatusUpdateRequest status(StatusEnum status) {
        this.status = status;
        return this;
    }

    /**
     * Новый статус запроса на участие в событии текущего пользователя
     *
     * @return status
     */

    @Schema(name = "status", example = "CONFIRMED", description = "Новый статус запроса на участие в событии текущего пользователя", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest = (EventRequestStatusUpdateRequest) o;
        return Objects.equals(this.requestIds, eventRequestStatusUpdateRequest.requestIds) &&
                Objects.equals(this.status, eventRequestStatusUpdateRequest.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestIds, status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EventRequestStatusUpdateRequest {\n");
        sb.append("    requestIds: ").append(toIndentedString(requestIds)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

