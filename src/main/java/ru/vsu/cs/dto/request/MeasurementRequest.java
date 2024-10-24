package ru.vsu.cs.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MeasurementRequest(

        @Schema(description = "Значение температуры воздуха", example = "19")
        @Min(-100)
        @Max(100)
        @NotNull
        Double value,

        @Schema(description = "Наличие дождя во время измерения (true - дождь идет, false - дождя нет)",
                example = "true")
        @NotNull
        Boolean raining,

        @Schema(description = "Сенсор, которым производилось измерение")
        @NotNull
        SensorRequest sensor
) {
}