package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Сведения об ошибке
 */
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Schema(name = "ApiError", description = "Сведения об ошибке")
public class ApiError {

    @JsonProperty("errors")
    @Valid
    private List<String> errors = null;

    @JsonProperty("message")
    private String message;

    @JsonProperty("reason")
    private String reason;

    /**
     * Код статуса HTTP-ответа
     */
    public enum StatusEnum {
        _100_CONTINUE("100 CONTINUE"),

        _101_SWITCHING_PROTOCOLS("101 SWITCHING_PROTOCOLS"),

        _102_PROCESSING("102 PROCESSING"),

        _103_CHECKPOINT("103 CHECKPOINT"),

        _200_OK("200 OK"),

        _201_CREATED("201 CREATED"),

        _202_ACCEPTED("202 ACCEPTED"),

        _203_NON_AUTHORITATIVE_INFORMATION("203 NON_AUTHORITATIVE_INFORMATION"),

        _204_NO_CONTENT("204 NO_CONTENT"),

        _205_RESET_CONTENT("205 RESET_CONTENT"),

        _206_PARTIAL_CONTENT("206 PARTIAL_CONTENT"),

        _207_MULTI_STATUS("207 MULTI_STATUS"),

        _208_ALREADY_REPORTED("208 ALREADY_REPORTED"),

        _226_IM_USED("226 IM_USED"),

        _300_MULTIPLE_CHOICES("300 MULTIPLE_CHOICES"),

        _301_MOVED_PERMANENTLY("301 MOVED_PERMANENTLY"),

        _302_FOUND("302 FOUND"),

        _302_MOVED_TEMPORARILY("302 MOVED_TEMPORARILY"),

        _303_SEE_OTHER("303 SEE_OTHER"),

        _304_NOT_MODIFIED("304 NOT_MODIFIED"),

        _305_USE_PROXY("305 USE_PROXY"),

        _307_TEMPORARY_REDIRECT("307 TEMPORARY_REDIRECT"),

        _308_PERMANENT_REDIRECT("308 PERMANENT_REDIRECT"),

        _400_BAD_REQUEST("400 BAD_REQUEST"),

        _401_UNAUTHORIZED("401 UNAUTHORIZED"),

        _402_PAYMENT_REQUIRED("402 PAYMENT_REQUIRED"),

        _403_FORBIDDEN("403 FORBIDDEN"),

        _404_NOT_FOUND("404 NOT_FOUND"),

        _405_METHOD_NOT_ALLOWED("405 METHOD_NOT_ALLOWED"),

        _406_NOT_ACCEPTABLE("406 NOT_ACCEPTABLE"),

        _407_PROXY_AUTHENTICATION_REQUIRED("407 PROXY_AUTHENTICATION_REQUIRED"),

        _408_REQUEST_TIMEOUT("408 REQUEST_TIMEOUT"),

        _409_CONFLICT("409 CONFLICT"),

        _410_GONE("410 GONE"),

        _411_LENGTH_REQUIRED("411 LENGTH_REQUIRED"),

        _412_PRECONDITION_FAILED("412 PRECONDITION_FAILED"),

        _413_PAYLOAD_TOO_LARGE("413 PAYLOAD_TOO_LARGE"),

        _413_REQUEST_ENTITY_TOO_LARGE("413 REQUEST_ENTITY_TOO_LARGE"),

        _414_URI_TOO_LONG("414 URI_TOO_LONG"),

        _414_REQUEST_URI_TOO_LONG("414 REQUEST_URI_TOO_LONG"),

        _415_UNSUPPORTED_MEDIA_TYPE("415 UNSUPPORTED_MEDIA_TYPE"),

        _416_REQUESTED_RANGE_NOT_SATISFIABLE("416 REQUESTED_RANGE_NOT_SATISFIABLE"),

        _417_EXPECTATION_FAILED("417 EXPECTATION_FAILED"),

        _418_I_AM_A_TEAPOT("418 I_AM_A_TEAPOT"),

        _419_INSUFFICIENT_SPACE_ON_RESOURCE("419 INSUFFICIENT_SPACE_ON_RESOURCE"),

        _420_METHOD_FAILURE("420 METHOD_FAILURE"),

        _421_DESTINATION_LOCKED("421 DESTINATION_LOCKED"),

        _422_UNPROCESSABLE_ENTITY("422 UNPROCESSABLE_ENTITY"),

        _423_LOCKED("423 LOCKED"),

        _424_FAILED_DEPENDENCY("424 FAILED_DEPENDENCY"),

        _425_TOO_EARLY("425 TOO_EARLY"),

        _426_UPGRADE_REQUIRED("426 UPGRADE_REQUIRED"),

        _428_PRECONDITION_REQUIRED("428 PRECONDITION_REQUIRED"),

        _429_TOO_MANY_REQUESTS("429 TOO_MANY_REQUESTS"),

        _431_REQUEST_HEADER_FIELDS_TOO_LARGE("431 REQUEST_HEADER_FIELDS_TOO_LARGE"),

        _451_UNAVAILABLE_FOR_LEGAL_REASONS("451 UNAVAILABLE_FOR_LEGAL_REASONS"),

        _500_INTERNAL_SERVER_ERROR("500 INTERNAL_SERVER_ERROR"),

        _501_NOT_IMPLEMENTED("501 NOT_IMPLEMENTED"),

        _502_BAD_GATEWAY("502 BAD_GATEWAY"),

        _503_SERVICE_UNAVAILABLE("503 SERVICE_UNAVAILABLE"),

        _504_GATEWAY_TIMEOUT("504 GATEWAY_TIMEOUT"),

        _505_HTTP_VERSION_NOT_SUPPORTED("505 HTTP_VERSION_NOT_SUPPORTED"),

        _506_VARIANT_ALSO_NEGOTIATES("506 VARIANT_ALSO_NEGOTIATES"),

        _507_INSUFFICIENT_STORAGE("507 INSUFFICIENT_STORAGE"),

        _508_LOOP_DETECTED("508 LOOP_DETECTED"),

        _509_BANDWIDTH_LIMIT_EXCEEDED("509 BANDWIDTH_LIMIT_EXCEEDED"),

        _510_NOT_EXTENDED("510 NOT_EXTENDED"),

        _511_NETWORK_AUTHENTICATION_REQUIRED("511 NETWORK_AUTHENTICATION_REQUIRED");

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

    @JsonProperty("timestamp")
    private String timestamp;

    public ApiError errors(List<String> errors) {
        this.errors = errors;
        return this;
    }

    public ApiError addErrorsItem(String errorsItem) {
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(errorsItem);
        return this;
    }

    /**
     * Список стектрейсов или описания ошибок
     *
     * @return errors
     */

    @Schema(name = "errors", example = "[]", description = "Список стектрейсов или описания ошибок", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public ApiError message(String message) {
        this.message = message;
        return this;
    }

    /**
     * Сообщение об ошибке
     *
     * @return message
     */

    @Schema(name = "message", example = "Only pending or canceled events can be changed", description = "Сообщение об ошибке", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ApiError reason(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * Общее описание причины ошибки
     *
     * @return reason
     */

    @Schema(name = "reason", example = "For the requested operation the conditions are not met.", description = "Общее описание причины ошибки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ApiError status(StatusEnum status) {
        this.status = status;
        return this;
    }

    /**
     * Код статуса HTTP-ответа
     *
     * @return status
     */

    @Schema(name = "status", example = "FORBIDDEN", description = "Код статуса HTTP-ответа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public ApiError timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    /**
     * Дата и время когда произошла ошибка (в формате \"yyyy-MM-dd HH:mm:ss\")
     *
     * @return timestamp
     */

    @Schema(name = "timestamp", example = "2022-06-09 06:27:23", description = "Дата и время когда произошла ошибка (в формате \"yyyy-MM-dd HH:mm:ss\")", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
        ApiError apiError = (ApiError) o;
        return Objects.equals(this.errors, apiError.errors) &&
                Objects.equals(this.message, apiError.message) &&
                Objects.equals(this.reason, apiError.reason) &&
                Objects.equals(this.status, apiError.status) &&
                Objects.equals(this.timestamp, apiError.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errors, message, reason, status, timestamp);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ApiError {\n");
        sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
        sb.append("    message: ").append(toIndentedString(message)).append("\n");
        sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

