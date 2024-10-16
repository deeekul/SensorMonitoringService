package ru.vsu.cs.dto.response;

import java.time.LocalDateTime;

public record MeasurementResponse(

        Long id,

        Double value,

        LocalDateTime measurementDateTime,

        Boolean raining,

        SensorResponse sensor
) {
}
