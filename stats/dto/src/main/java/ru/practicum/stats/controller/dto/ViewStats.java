package ru.practicum.stats.controller.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;


@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class ViewStats {

    @JsonProperty("app")
    private String app;

    @JsonProperty("uri")
    private String uri;

    @JsonProperty("hits")
    private Long hits;

    public ViewStats app(String app) {
        this.app = app;
        return this;
    }

    /**
     * Название сервиса
     *
     * @return app
     */

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public ViewStats uri(String uri) {
        this.uri = uri;
        return this;
    }

    /**
     * URI сервиса
     *
     * @return uri
     */

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ViewStats hits(Long hits) {
        this.hits = hits;
        return this;
    }

    /**
     * Количество просмотров
     *
     * @return hits
     */

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ViewStats viewStats = (ViewStats) o;
        return Objects.equals(this.app, viewStats.app) &&
                Objects.equals(this.uri, viewStats.uri) &&
                Objects.equals(this.hits, viewStats.hits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri, hits);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ViewStats {\n");
        sb.append("    app: ").append(toIndentedString(app)).append("\n");
        sb.append("    uri: ").append(toIndentedString(uri)).append("\n");
        sb.append("    hits: ").append(toIndentedString(hits)).append("\n");
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

