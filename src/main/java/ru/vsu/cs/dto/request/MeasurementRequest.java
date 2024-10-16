package ru.vsu.cs.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public record MeasurementRequest(

        @Min(-100)
        @Max(100)
        @NotNull
        Double value,

        @NotNull
        Boolean raining,

        @NotNull
        SensorRequest sensor
) {
}