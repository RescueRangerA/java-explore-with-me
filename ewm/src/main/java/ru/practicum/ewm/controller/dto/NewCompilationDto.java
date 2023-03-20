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

@Schema(name = "NewCompilationDto", description = "Подборка событий")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-03-20T18:18:39.930348+04:00[Europe/Saratov]")
public class NewCompilationDto {

    @JsonProperty("events")
    @Valid
    private Set<Long> events = null;

    @JsonProperty("pinned")
    private Boolean pinned = false;

    @JsonProperty("title")
    private String title;

    public NewCompilationDto events(Set<Long> events) {
        this.events = events;
        return this;
    }

    public NewCompilationDto addEventsItem(Long eventsItem) {
        if (this.events == null) {
            this.events = new LinkedHashSet<>();
        }
        this.events.add(eventsItem);
        return this;
    }

    /**
     * Список идентификаторов событий входящих в подборку
     *
     * @return events
     */

    @Schema(name = "events", example = "[1,2,3]", description = "Список идентификаторов событий входящих в подборку", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Set<Long> getEvents() {
        return events;
    }

    @JsonDeserialize(as = LinkedHashSet.class)
    public void setEvents(Set<Long> events) {
        this.events = events;
    }

    public NewCompilationDto pinned(Boolean pinned) {
        this.pinned = pinned;
        return this;
    }

    /**
     * Закреплена ли подборка на главной странице сайта
     *
     * @return pinned
     */

    @Schema(name = "pinned", example = "false", description = "Закреплена ли подборка на главной странице сайта", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public NewCompilationDto title(String title) {
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
        NewCompilationDto newCompilationDto = (NewCompilationDto) o;
        return Objects.equals(this.events, newCompilationDto.events) &&
                Objects.equals(this.pinned, newCompilationDto.pinned) &&
                Objects.equals(this.title, newCompilationDto.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(events, pinned, title);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NewCompilationDto {\n");
        sb.append("    events: ").append(toIndentedString(events)).append("\n");
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

