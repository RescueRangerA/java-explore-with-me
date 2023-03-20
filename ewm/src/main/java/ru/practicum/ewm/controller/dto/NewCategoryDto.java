package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Данные для добавления новой категории
 */
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor

@Schema(name = "NewCategoryDto", description = "Данные для добавления новой категории")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-03-20T18:18:39.930348+04:00[Europe/Saratov]")
public class NewCategoryDto {

    @JsonProperty("name")
    private String name;

    public NewCategoryDto name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Название категории
     *
     * @return name
     */
    @NotNull
    @Schema(name = "name", example = "Концерты", description = "Название категории", requiredMode = Schema.RequiredMode.REQUIRED)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewCategoryDto newCategoryDto = (NewCategoryDto) o;
        return Objects.equals(this.name, newCategoryDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NewCategoryDto {\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

