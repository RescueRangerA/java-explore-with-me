package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Schema(name = "NewEventReactionDto", description = "Реакция")
public class NewEventReactionDto {
    @JsonProperty("eventId")
    private Long eventId;


    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("isLike")
    private Boolean isLike;

    @Schema(name = "eventId", example = "999", description = "ID события", requiredMode = Schema.RequiredMode.REQUIRED)
    public Long getEventId() {
        return eventId;
    }

    @Schema(name = "userId", example = "999", description = "ID юзера", requiredMode = Schema.RequiredMode.REQUIRED)
    public Long getUserId() {
        return userId;
    }

    @Schema(name = "isLike", example = "true", description = "Флаг определяющий лайк это или дизлайк", requiredMode = Schema.RequiredMode.REQUIRED)
    public Boolean getIsLike() {
        return isLike;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewEventReactionDto newEventDto = (NewEventReactionDto) o;
        return Objects.equals(this.eventId, newEventDto.eventId) &&
                Objects.equals(this.userId, newEventDto.userId) &&
                Objects.equals(this.isLike, newEventDto.isLike);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, userId, isLike);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NewEventReactionDto {\n");
        sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    isLike: ").append(toIndentedString(isLike)).append("\n");
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
