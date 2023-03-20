package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Пользователь
 */
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor

@Schema(name = "UserDto", description = "Пользователь")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-03-20T18:18:39.930348+04:00[Europe/Saratov]")
public class UserDto {

    @JsonProperty("email")
    private String email;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    public UserDto email(String email) {
        this.email = email;
        return this;
    }

    /**
     * Почтовый адрес
     *
     * @return email
     */
    @NotNull
    @Schema(name = "email", example = "petrov.i@practicummail.ru", description = "Почтовый адрес", requiredMode = Schema.RequiredMode.REQUIRED)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDto id(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Идентификатор
     *
     * @return id
     */

    @Schema(name = "id", accessMode = Schema.AccessMode.READ_ONLY, example = "1", description = "Идентификатор", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDto name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Имя
     *
     * @return name
     */
    @NotNull
    @Schema(name = "name", example = "Петров Иван", description = "Имя", requiredMode = Schema.RequiredMode.REQUIRED)
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
        UserDto userDto = (UserDto) o;
        return Objects.equals(this.email, userDto.email) &&
                Objects.equals(this.id, userDto.id) &&
                Objects.equals(this.name, userDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, id, name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserDto {\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

