package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Изменение информации о подборке событий. Если поле в запросе не указано (равно null) - значит изменение этих данных не треубется.
 */
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Schema(name = "UpdateCompilationRequest", description = "Изменение информации о подборке событий. Если поле в запросе не указано (равно null) - значит изменение этих данных не треубется.")
public class UpdateCompilationRequest {

    @JsonProperty("events")
    @Valid
    private Set<Long> events = null;

    @JsonProperty("pinned")
    private Boolean pinned;

    @JsonProperty("title")
    private String title;

    public UpdateCompilationRequest events(Set<Long> events) {
        this.events = events;
        return this;
    }

    public UpdateCompilationRequest addEventsItem(Long eventsItem) {
        if (this.events == null) {
            this.events = new LinkedHashSet<>();
        }
        this.events.add(eventsItem);
        return this;
    }

    /**
     * Список id событий подборки для полной замены текущего списка
     *
     * @return events
     */

    @Schema(name = "events", description = "Список id событий подборки для полной замены текущего списка", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Set<Long> getEvents() {
        return events;
    }

    @JsonDeserialize(as = LinkedHashSet.class)
    public void setEvents(Set<Long> events) {
        this.events = events;
    }

    public UpdateCompilationRequest pinned(Boolean pinned) {
        this.pinned = pinned;
        return this;
    }

    /**
     * Закреплена ли подборка на главной странице сайта
     *
     * @return pinned
     */

    @Schema(name = "pinned", example = "true", description = "Закреплена ли подборка на главной странице сайта", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public UpdateCompilationRequest title(String title) {
        this.title = title;
        return this;
    }

    /**
     * Заголовок подборки
     *
     * @return title
     */

    @Schema(name = "title", example = "Необычные фотозоны", description = "Заголовок подборки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
        UpdateCompilationRequest updateCompilationRequest = (UpdateCompilationRequest) o;
        return Objects.equals(this.events, updateCompilationRequest.events) &&
                Objects.equals(this.pinned, updateCompilationRequest.pinned) &&
                Objects.equals(this.title, updateCompilationRequest.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(events, pinned, title);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UpdateCompilationRequest {\n");
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

