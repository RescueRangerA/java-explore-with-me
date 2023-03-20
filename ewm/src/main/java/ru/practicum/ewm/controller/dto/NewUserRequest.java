package ru.practicum.ewm.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.annotation.Generated;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Данные нового пользователя
 */
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor

@Schema(name = "NewUserRequest", description = "Данные нового пользователя")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2023-03-20T18:18:39.930348+04:00[Europe/Saratov]")
public class NewUserRequest {

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    public NewUserRequest email(String email) {
        this.email = email;
        return this;
    }

    /**
     * Почтовый адрес
     *
     * @return email
     */
    @NotNull
    @Schema(name = "email", example = "ivan.petrov@practicummail.ru", description = "Почтовый адрес", requiredMode = Schema.RequiredMode.REQUIRED)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NewUserRequest name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Имя
     *
     * @return name
     */
    @NotNull
    @Schema(name = "name", example = "Иван Петров", description = "Имя", requiredMode = Schema.RequiredMode.REQUIRED)
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
        NewUserRequest newUserRequest = (NewUserRequest) o;
        return Objects.equals(this.email, newUserRequest.email) &&
                Objects.equals(this.name, newUserRequest.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class NewUserRequest {\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
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

