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
import org.springframework.web.bind.annotation.RequestParam;
import ru.vsu.cs.dto.request.MeasurementRequest;
import ru.vsu.cs.dto.response.MeasurementResponse;
import ru.vsu.cs.util.ErrorResponse;

import java.util.List;

@Tag(name = "Measurement API", description = "API для работы с измерениями")
public interface MeasurementApi {

    @ApiResponse(
            responseCode = "200",
            description = "Успешное получение всех измерений",
            content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = MeasurementResponse.class))
                    )
            }
    )
    @Operation(summary = "Получить все измерения")
    ResponseEntity<List<MeasurementResponse>> getAllMeasurements();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение всех измерений",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MeasurementResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Сенсор с таким именем не найден",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = MeasurementResponse.class))
                            )
                    }
            )
    })
    @Operation(summary = "Получить все измерения по имени сенсора")
    ResponseEntity<List<MeasurementResponse>> getAllMeasurementsBySensorName(
            @Parameter(description = "Имя сенсора", required = true)
            @RequestParam(value = "name") String sensorName);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное получение измерения",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MeasurementResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Измерение по указанному идентификатору не найдено",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    }
            )
    })
    @Operation(summary = "Получить измерение по идентификатору")
    ResponseEntity<MeasurementResponse> getMeasurementById(
            @Parameter(description = "Идентификатор измерения") Long measurementId);


    @ApiResponse(
            responseCode = "200",
            description = "Успешное получение количества дождливых дней",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MeasurementResponse.class)
                    )
            }
    )
    @Operation(summary = "Получить количество дождливых дней")
    ResponseEntity<Long> getRainyDaysCount();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешное добавление измерения",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MeasurementResponse.class)
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
    @Operation(summary = "Добавить новое измерение")
    ResponseEntity<?> addMeasurement(
            @RequestBody(description = "Параметры для добавления измерения") MeasurementRequest measurementRequest,
            BindingResult bindingResult);
}
