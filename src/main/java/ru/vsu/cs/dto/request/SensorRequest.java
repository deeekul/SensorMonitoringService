package ru.vsu.cs.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SensorRequest(

        @Schema(description = "Название сенсора", example = "ESM-10 Danfoss")
        @NotEmpty(message = "Название сенсора не должно быть пустым!")
        @Size(min = 3, max = 30, message = "Название сенсора должно быть от 3 до 30 символов!")
        String name
) {
}