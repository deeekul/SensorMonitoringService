package ru.vsu.cs.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SensorCreateRequest(

        Long id,

        @NotEmpty(message = "Название сенсора не должно быть пустым!")
        @Size(min = 5, max = 50, message = "Название сенсора должно быть от 5 до 50 символов!")
        String name
) {
}
