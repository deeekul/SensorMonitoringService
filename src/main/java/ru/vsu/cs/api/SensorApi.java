package ru.vsu.cs.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ru.vsu.cs.dto.request.SensorRequest;
import ru.vsu.cs.dto.response.SensorResponse;
import ru.vsu.cs.util.ErrorResponse;
import java.util.List;

@Tag(name = "Sensor API", description = "API для работы с сенсорами")
public interface SensorApi {

    @ApiResponse(
            responseCode = "200",
            description = "Успешное получение всех сенсоров",
            content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = SensorResponse.class))
                    )
            }
    )
    @Operation(summary = "Получить все сенсоры")
    ResponseEntity<List<SensorResponse>> getAllSensors();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение сенсора",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SensorResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сенсор по указанному идентификатору не найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    }
            )
    })
    @Operation(summary = "Получить сенсор по идентификатору")
    ResponseEntity<SensorResponse> getSensorById(@Parameter(description = "Идентификатор сенсора") Long sensorId);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Успешное добавление нового сенсора",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SensorResponse.class)
                            )
                    }

            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "В теле запроса указаны недопустимые поля " +
                                    "или отсутствуют обязательные для заполнения",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    }
            )
    })
    @Operation(summary = "Добавить новый сенсор")
    ResponseEntity<?> createSensor(
            @RequestBody(description = "Параметры для создания сенсора") SensorRequest sensorRequest,
            BindingResult bindingResult);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное обновление сенсора",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SensorResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сенсор по указанному идентификатору не найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    }
            )
    })
    @Operation(summary = "Обновить сенсор по идентификатору")
    ResponseEntity<?> updateSensor(@Parameter(description = "Идентификатор обновляемого сенсора") Long sensorId,
                                   @RequestBody(description = "Параметры для обновления") SensorRequest sensorRequest,
                                   BindingResult bindingResult);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное удаление сенсора",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SensorResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сенсор по указанному идентификатору не найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    }
            )
    })
    @Operation(summary = "Удалить сенсор по идентификатору")
    ResponseEntity<Void> deleteSensorById(@Parameter(description = "Идентификатор удаляемого сенсора") Long sensorId);
}
