package ru.vsu.cs.util;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ErrorResponse(

        @Schema(description = "Сообщение ошибки", example = "")
        String errorMessage,

        @Schema(description = "Код ошибки", example = "400")
        Integer errorCode
) {
}
