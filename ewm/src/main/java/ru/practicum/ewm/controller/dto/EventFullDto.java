package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * EventFullDto
 */
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class EventFullDto {

    @JsonProperty("annotation")
    private String annotation;

    @JsonProperty("category")
    private CategoryDto category;

    @JsonProperty("confirmedRequests")
    private Long confirmedRequests;

    @JsonProperty("createdOn")
    private String createdOn;

    @JsonProperty("description")
    private String description;

    @JsonProperty("eventDate")
    private String eventDate;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("initiator")
    private UserShortDto initiator;

    @JsonProperty("location")
    private Location location;

    @JsonProperty("paid")
    private Boolean paid;

    @JsonProperty("participantLimit")
    private Long participantLimit = 0L;

    @JsonProperty("publishedOn")
    private String publishedOn;

    @JsonProperty("requestModeration")
    private Boolean requestModeration = true;

    /**
     * Список состояний жизненного цикла события
     */
    public enum StateEnum {
        PENDING("PENDING"),

        PUBLISHED("PUBLISHED"),

        CANCELED("CANCELED");

        private String value;

        StateEnum(String value) {
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
        public static StateEnum fromValue(String value) {
            for (StateEnum b : StateEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }

    @JsonProperty("state")
    private StateEnum state;

    @JsonProperty("title")
    private String title;

    @JsonProperty("views")
    private Long views;

    @JsonProperty("rating")
    private Long rating;

    public EventFullDto annotation(String annotation) {
        this.annotation = annotation;
        return this;
    }

    /**
     * Краткое описание
     *
     * @return annotation
     */
    @NotNull
    @Schema(name = "annotation", example = "Эксклюзивность нашего шоу гарантирует привлечение максимальной зрительской аудитории", description = "Краткое описание", requiredMode = Schema.RequiredMode.REQUIRED)
    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public EventFullDto category(CategoryDto category) {
        this.category = category;
        return this;
    }

    /**
     * Get category
     *
     * @return category
     */
    @NotNull
    @Valid
    @Schema(name = "category", requiredMode = Schema.RequiredMode.REQUIRED)
    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public EventFullDto confirmedRequests(Long confirmedRequests) {
        this.confirmedRequests = confirmedRequests;
        return this;
    }

    /**
     * Количество одобренных заявок на участие в данном событии
     *
     * @return confirmedRequests
     */

    @Schema(name = "confirmedRequests", example = "5", description = "Количество одобренных заявок на участие в данном событии", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Long getConfirmedRequests() {
        return confirmedRequests;
    }

    public void setConfirmedRequests(Long confirmedRequests) {
        this.confirmedRequests = confirmedRequests;
    }

    public EventFullDto createdOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    /**
     * Дата и время создания события (в формате \"yyyy-MM-dd HH:mm:ss\")
     *
     * @return createdOn
     */

    @Schema(name = "createdOn", example = "2022-09-06 11:00:23", description = "Дата и время создания события (в формате \"yyyy-MM-dd HH:mm:ss\")", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public EventFullDto description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Полное описание события
     *
     * @return description
     */

    @Schema(name = "description", example = "Что получится, если соединить кукурузу и полёт? Создатели 'Шоу летающей кукурузы' испытали эту идею на практике и воплотили в жизнь инновационный проект, предлагающий свежий взгляд на развлечения...", description = "Полное описание события", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EventFullDto eventDate(String eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    /**
     * Дата и время на которые намечено событие (в формате \"yyyy-MM-dd HH:mm:ss\")
     *
     * @return eventDate
     */
    @NotNull
    @Schema(name = "eventDate", example = "2024-12-31 15:10:05", description = "Дата и время на которые намечено событие (в формате \"yyyy-MM-dd HH:mm:ss\")", requiredMode = Schema.RequiredMode.REQUIRED)
    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public EventFullDto id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Идентификатор
     *
     * @return id
     */

    @Schema(name = "id", example = "1", description = "Идентификатор", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventFullDto initiator(UserShortDto initiator) {
        this.initiator = initiator;
        return this;
    }

    /**
     * Get initiator
     *
     * @return initiator
     */
    @NotNull
    @Valid
    @Schema(name = "initiator", requiredMode = Schema.RequiredMode.REQUIRED)
    public UserShortDto getInitiator() {
        return initiator;
    }

    public void setInitiator(UserShortDto initiator) {
        this.initiator = initiator;
    }

    public EventFullDto location(Location location) {
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

    public EventFullDto paid(Boolean paid) {
        this.paid = paid;
        return this;
    }

    /**
     * Нужно ли оплачивать участие
     *
     * @return paid
     */
    @NotNull
    @Schema(name = "paid", example = "true", description = "Нужно ли оплачивать участие", requiredMode = Schema.RequiredMode.REQUIRED)
    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public EventFullDto participantLimit(Long participantLimit) {
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

    public EventFullDto publishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
        return this;
    }

    /**
     * Дата и время публикации события (в формате \"yyyy-MM-dd HH:mm:ss\")
     *
     * @return publishedOn
     */

    @Schema(name = "publishedOn", example = "2022-09-06 15:10:05", description = "Дата и время публикации события (в формате \"yyyy-MM-dd HH:mm:ss\")", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public String getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(String publishedOn) {
        this.publishedOn = publishedOn;
    }

    public EventFullDto requestModeration(Boolean requestModeration) {
        this.requestModeration = requestModeration;
        return this;
    }

    /**
     * Нужна ли пре-модерация заявок на участие
     *
     * @return requestModeration
     */

    @Schema(name = "requestModeration", example = "true", description = "Нужна ли пре-модерация заявок на участие", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Boolean getRequestModeration() {
        return requestModeration;
    }

    public void setRequestModeration(Boolean requestModeration) {
        this.requestModeration = requestModeration;
    }

    public EventFullDto state(StateEnum state) {
        this.state = state;
        return this;
    }

    /**
     * Список состояний жизненного цикла события
     *
     * @return state
     */

    @Schema(name = "state", example = "PUBLISHED", description = "Список состояний жизненного цикла события", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public StateEnum getState() {
        return state;
    }

    public void setState(StateEnum state) {
        this.state = state;
    }

    public EventFullDto title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Заголовок
     *
     * @return title
     */
    @NotNull
    @Schema(name = "title", example = "Знаменитое шоу 'Летающая кукуруза'", description = "Заголовок", requiredMode = Schema.RequiredMode.REQUIRED)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public EventFullDto views(Long views) {
        this.views = views;
        return this;
    }

    /**
     * Количество просмотрев события
     *
     * @return views
     */

    @Schema(name = "views", example = "999", description = "Количество просмотрев события", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    @Schema(name = "rating", example = "999", description = "Рейтинг события", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventFullDto eventFullDto = (EventFullDto) o;
        return Objects.equals(this.annotation, eventFullDto.annotation) &&
                Objects.equals(this.category, eventFullDto.category) &&
                Objects.equals(this.confirmedRequests, eventFullDto.confirmedRequests) &&
                Objects.equals(this.createdOn, eventFullDto.createdOn) &&
                Objects.equals(this.description, eventFullDto.description) &&
                Objects.equals(this.eventDate, eventFullDto.eventDate) &&
                Objects.equals(this.id, eventFullDto.id) &&
                Objects.equals(this.initiator, eventFullDto.initiator) &&
                Objects.equals(this.location, eventFullDto.location) &&
                Objects.equals(this.paid, eventFullDto.paid) &&
                Objects.equals(this.participantLimit, eventFullDto.participantLimit) &&
                Objects.equals(this.publishedOn, eventFullDto.publishedOn) &&
                Objects.equals(this.requestModeration, eventFullDto.requestModeration) &&
                Objects.equals(this.state, eventFullDto.state) &&
                Objects.equals(this.title, eventFullDto.title) &&
                Objects.equals(this.views, eventFullDto.views) &&
                Objects.equals(this.rating, eventFullDto.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(annotation, category, confirmedRequests, createdOn, description, eventDate, id, initiator, location, paid, participantLimit, publishedOn, requestModeration, state, title, views, rating);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class EventFullDto {\n");
        sb.append("    annotation: ").append(toIndentedString(annotation)).append("\n");
        sb.append("    category: ").append(toIndentedString(category)).append("\n");
        sb.append("    confirmedRequests: ").append(toIndentedString(confirmedRequests)).append("\n");
        sb.append("    createdOn: ").append(toIndentedString(createdOn)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    eventDate: ").append(toIndentedString(eventDate)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    initiator: ").append(toIndentedString(initiator)).append("\n");
        sb.append("    location: ").append(toIndentedString(location)).append("\n");
        sb.append("    paid: ").append(toIndentedString(paid)).append("\n");
        sb.append("    participantLimit: ").append(toIndentedString(participantLimit)).append("\n");
        sb.append("    publishedOn: ").append(toIndentedString(publishedOn)).append("\n");
        sb.append("    requestModeration: ").append(toIndentedString(requestModeration)).append("\n");
        sb.append("    state: ").append(toIndentedString(state)).append("\n");
        sb.append("    title: ").append(toIndentedString(title)).append("\n");
        sb.append("    views: ").append(toIndentedString(views)).append("\n");
        sb.append("    rating: ").append(toIndentedString(rating)).append("\n");
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

