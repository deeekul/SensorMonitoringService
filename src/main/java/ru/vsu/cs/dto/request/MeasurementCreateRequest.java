package ru.vsu.cs.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import ru.vsu.cs.entities.Sensor;

import java.time.LocalDateTime;

public record MeasurementCreateRequest(

        Long id,

        @Min(-100)
        @Max(100)
        @NotNull
        Double value,

        @NotNull
        LocalDateTime measurementDateTime,

        @NotNull
        Boolean raining,

        @NotNull
        Sensor sensor
) {
}
