package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Новое событие
 */
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Schema(name = "NewEventDto", description = "Новое событие")
public class NewEventDto {

    @JsonProperty("annotation")
    private String annotation;

    @JsonProperty("category")
    private Long category;

    @JsonProperty("description")
    private String description;

    @JsonProperty("eventDate")
    private String eventDate;

    @JsonProperty("location")
    private Location location;

    @JsonProperty("paid")
    private Boolean paid = false;

    @JsonProperty("participantLimit")
    private Long participantLimit = 0L;

    @JsonProperty("requestModeration")
    private Boolean requestModeration = true;

    @JsonProperty("title")
    private String title;

    public NewEventDto annotation(String annotation) {
        this.annotation = annotation;
        return this;
    }

    /**
     * Краткое описание события
     *
     * @return annotation
     */
    @NotNull
    @Size(min = 20, max = 2000)
    @Schema(name = "annotation", example = "Сплав на байдарках похож на полет.", description = "Краткое описание события", requiredMode = Schema.RequiredMode.REQUIRED)
    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public NewEventDto category(Long category) {
        this.category = category;
        return this;
    }

    /**
     * id категории к которой относится событие
     *
     * @return category
     */
    @NotNull
    @Schema(name = "category", example = "2", description = "id категории к которой относится событие", requiredMode = Schema.RequiredMode.REQUIRED)
    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public NewEventDto description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Полное описание события
     *
     * @return description
     */
    @NotNull
    @Size(min = 20, max = 7000)
    @Schema(name = "description", example = "Сплав на байдарках похож на полет. На спокойной воде — это парение. На бурной, порожистой — выполнение фигур высшего пилотажа. И то, и другое дарят чувство обновления, феерические эмоции, яркие впечатления.", description = "Полное описание события", requiredMode = Schema.RequiredMode.REQUIRED)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NewEventDto eventDate(String eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    /**
     * Дата и время на которые намечено событие. Дата и время указываются в формате \"yyyy-MM-dd HH:mm:ss\"
     *
     * @return eventDate
     */
    @NotNull
    @Schema(name = "eventDate", example = "2024-12-31 15:10:05", description = "Дата и время на которые намечено событие. Дата и время указываются в формате \"yyyy-MM-dd HH:mm:ss\"", requiredMode = Schema.RequiredMode.REQUIRED)
    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public NewEventDto location(Location location) {
        this.location = location;
        return this;
    }

    /**
     * Get location
     *
     * @return location
     */
    @NotNull
    @Valid
    @Schema(name = "location", requiredMode = Schema.RequiredMode.REQUIRED)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public NewEventDto paid(Boolean paid) {
        this.paid = paid;
        return this;
    }

    /**
     * Нужно ли оплачивать участие в событии
     *
     * @return paid
     */

    @Schema(name = "paid", example = "true", description = "Нужно ли оплачивать участие в событии", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public NewEventDto participantLimit(Long participantLimit) {
        this.participantLimit = participantLimit;
        return this;
    }

    /**
     * Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
     *
     * @return participantLimit
     */

    @Schema(name = "participantLimit", example = "10", description = "Ограничение на количество участников. Значение 0 - означает отсутствие ограничения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Long getParticipantLimit() {
        return participantLimit;
    }

    public void setParticipantLimit(Long participantLimit) {
        this.participantLimit = participantLimit;
    }

    public NewEventDto requestModeration(Boolean requestModeration) {
        this.requestModeration = requestModeration;
        return this;
    }

    /**
     * Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.
     *
     * @return requestModeration
     */

    @Schema(name = "requestModeration", example = "false", description = "Нужна ли пре-модерация заявок на участие. Если true, то все заявки будут ожидать подтверждения инициатором события. Если false - то будут подтверждаться автоматически.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Boolean getRequestModeration() {
        return requestModeration;
    }

    public void setRequestModeration(Boolean requestModeration) {
        this.requestModeration = requestModeration;
    }

    public NewEventDto title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Заголовок события
     *
     * @return title
     */
    @NotNull
    @Size(min = 3, max = 120)
    @Schema(name = "title", example = "Сплав на байдарках", description = "Заголовок события", requiredMode = Schema.RequiredMode.REQUIRED)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewEventDto newEventDto = (NewEventDto) o;
        return Objects.equals(this.annotation, newEventDto.annotation) &&
                Objects.equals(this.category, newEventDto.category) &&
                Objects.equals(this.description, newEventDto.description) &&
                Objects.equals(this.eventDate, newEventDto.eventDate) &&
                Objects.equals(this.location, newEventDto.location) &&
                Objects.equals(this.paid, newEventDto.paid) &&
                Objects.equals(this.participantLimit, newEventDto.participantLimit) &&
                Objects.equals(this.requestModeration, newEventDto.requestModeration) &&
                Objects.equals(this.title, newEventDto.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotation, category, description, eventDate, location, paid, participantLimit, requestModeration, title);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NewEventDto {\n");
        sb.append("    annotation: ").append(toIndentedString(annotation)).append("\n");
        sb.append("    category: ").append(toIndentedString(category)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    eventDate: ").append(toIndentedString(eventDate)).append("\n");
        sb.append("    location: ").append(toIndentedString(location)).append("\n");
        sb.append("    paid: ").append(toIndentedString(paid)).append("\n");
        sb.append("    participantLimit: ").append(toIndentedString(participantLimit)).append("\n");
        sb.append("    requestModeration: ").append(toIndentedString(requestModeration)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
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

