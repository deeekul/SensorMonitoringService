package ru.vsu.cs.dto.response;

import ru.vsu.cs.entities.Sensor;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public record MeasurementResponse(
        Long id,

        Double value,

        LocalDateTime measurementDateTime,

        Boolean isRaining,

        Sensor sensor
) {
}
