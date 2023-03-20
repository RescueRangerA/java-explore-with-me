package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Подборка событий
 */
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor

@Schema(name = "CompilationDto", description = "Подборка событий")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-03-20T18:18:39.930348+04:00[Europe/Saratov]")
public class CompilationDto {

    @JsonProperty("events")
    @Valid
    private Set<EventShortDto> events = null;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("pinned")
    private Boolean pinned;

    @JsonProperty("title")
    private String title;

    public CompilationDto events(Set<EventShortDto> events) {
        this.events = events;
        return this;
    }

    public CompilationDto addEventsItem(EventShortDto eventsItem) {
        if (this.events == null) {
            this.events = new LinkedHashSet<>();
        }
        this.events.add(eventsItem);
        return this;
    }

    /**
     * Список событий входящих в подборку
     *
     * @return events
     */
    @Valid
    @Schema(name = "events", example = "[{\"annotation\":\"Эксклюзивность нашего шоу гарантирует привлечение максимальной зрительской аудитории\",\"category\":{\"id\":1,\"name\":\"Концерты\"},\"confirmedRequests\":5,\"eventDate\":\"2024-03-10 14:30:00\",\"id\":1,\"initiator\":{\"id\":3,\"name\":\"Фёдоров Матвей\"},\"paid\":true,\"title\":\"Знаменитое шоу 'Летающая кукуруза'\",\"views\":999},{\"annotation\":\"За почти три десятилетия группа 'Java Core' закрепились на сцене как группа, объединяющая поколения.\",\"category\":{\"id\":1,\"name\":\"Концерты\"},\"confirmedRequests\":555,\"eventDate\":\"2025-09-13 21:00:00\",\"id\":1,\"initiator\":{\"id\":3,\"name\":\"Паша Петров\"},\"paid\":true,\"title\":\"Концерт рок-группы 'Java Core'\",\"views\":991}]", description = "Список событий входящих в подборку", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Set<EventShortDto> getEvents() {
        return events;
    }

    @JsonDeserialize(as = LinkedHashSet.class)
    public void setEvents(Set<EventShortDto> events) {
        this.events = events;
    }

    public CompilationDto id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Идентификатор
     *
     * @return id
     */
    @NotNull
    @Schema(name = "id", example = "1", description = "Идентификатор", requiredMode = Schema.RequiredMode.REQUIRED)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CompilationDto pinned(Boolean pinned) {
        this.pinned = pinned;
        return this;
    }

    /**
     * Закреплена ли подборка на главной странице сайта
     *
     * @return pinned
     */
    @NotNull
    @Schema(name = "pinned", example = "true", description = "Закреплена ли подборка на главной странице сайта", requiredMode = Schema.RequiredMode.REQUIRED)
    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public CompilationDto title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Заголовок подборки
     *
     * @return title
     */
    @NotNull
    @Schema(name = "title", example = "Летние концерты", description = "Заголовок подборки", requiredMode = Schema.RequiredMode.REQUIRED)
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
        CompilationDto compilationDto = (CompilationDto) o;
        return Objects.equals(this.events, compilationDto.events) &&
                Objects.equals(this.id, compilationDto.id) &&
                Objects.equals(this.pinned, compilationDto.pinned) &&
                Objects.equals(this.title, compilationDto.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(events, id, pinned, title);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CompilationDto {\n");
        sb.append("    events: ").append(toIndentedString(events)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    pinned: ").append(toIndentedString(pinned)).append("\n");
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

