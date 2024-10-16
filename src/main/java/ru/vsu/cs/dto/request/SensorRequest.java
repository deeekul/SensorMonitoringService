package ru.vsu.cs.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record SensorRequest(

        @NotEmpty(message = "Название сенсора не должно быть пустым!")
        @Size(min = 3, max = 30, message = "Название сенсора должно быть от 3 до 30 символов!")
        String name
) {
}