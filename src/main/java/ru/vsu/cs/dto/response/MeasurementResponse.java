package ru.vsu.cs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record MeasurementResponse(

        @Schema(description = "Идентификатор измерения", example = "1")
        Long id,

        @Schema(description = "Температура воздуха", example = "19")
        Double value,

        @Schema(description = "Время измерения, сделанного сенсором", example = "2024-10-05 14:30:00")
        LocalDateTime measurementDateTime,

        @Schema(description = "Наличие дождя во время измерения (true - дождь идет, false - дождя нет)",
                example = "true")
        Boolean raining,

        @Schema(description = "Название сенсора", example = "ESM-10 Danfoss")
        SensorResponse sensor
) {
}
