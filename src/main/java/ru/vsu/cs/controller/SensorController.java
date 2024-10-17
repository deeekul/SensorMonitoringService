package ru.vsu.cs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.api.SensorApi;
import ru.vsu.cs.dto.request.SensorRequest;
import ru.vsu.cs.dto.response.SensorResponse;
import ru.vsu.cs.exception.MeasurementNotFoundException;
import ru.vsu.cs.exception.SensorNotFoundException;
import ru.vsu.cs.service.SensorService;
import ru.vsu.cs.util.ErrorResponse;
import ru.vsu.cs.util.SensorValidator;

import java.util.List;
import static org.springframework.http.ResponseEntity.ok;
import static ru.vsu.cs.util.ErrorsUtil.returnErrorsToClient;

@RequiredArgsConstructor
@RequestMapping("/api/sensors")
@RestController
public class SensorController implements SensorApi {

    private final SensorService sensorService;

    private final SensorValidator sensorValidator;

    @GetMapping
    public ResponseEntity<List<SensorResponse>> getAllSensors() {
        return ok(sensorService.getAllSensors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorResponse> getSensorById(@PathVariable("id") Long sensorId) {
        return ok(sensorService.getSensorById(sensorId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSensor(@RequestBody @Valid SensorRequest sensorRequest,
                                                       BindingResult bindingResult) {
        sensorValidator.validate(sensorRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorsMessage = returnErrorsToClient(bindingResult);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(errorsMessage, 400));
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(sensorService.registerSensor(sensorRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSensor(@PathVariable(("id")) Long sensorId,
                                          @RequestBody @Valid SensorRequest sensorRequest,
                                          BindingResult bindingResult) {
        sensorValidator.validate(sensorRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorsMessage = returnErrorsToClient(bindingResult);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(errorsMessage, 400));
        }

        return ok(sensorService.updateSensorById(sensorId, sensorRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSensorById(@PathVariable("id") Long sensorId) {
        sensorService.deleteSensorById(sensorId);
        return ok().build();
    }
}
