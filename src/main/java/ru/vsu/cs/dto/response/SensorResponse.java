package ru.vsu.cs.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record SensorResponse(

        @Schema(description = "Идентификатор сенсора", example = "1")
        Long id,

        @Schema(description = "Название сенсора", example = "ESM-10 Danfoss")
        String name
) {
}
