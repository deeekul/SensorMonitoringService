package ru.vsu.cs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.vsu.cs.api.MeasurementApi;
import ru.vsu.cs.dto.request.MeasurementRequest;
import ru.vsu.cs.dto.response.MeasurementResponse;
import ru.vsu.cs.service.MeasurementService;
import ru.vsu.cs.util.MeasurementValidator;

import java.util.List;
import static org.springframework.http.ResponseEntity.ok;
import static ru.vsu.cs.util.ErrorsUtil.returnErrorsToClient;

@RequiredArgsConstructor
@RequestMapping("/api/measurements")
@RestController
public class MeasurementController {

    private final MeasurementService measurementService;

    private final MeasurementValidator measurementValidator;

    @GetMapping
    public ResponseEntity<List<MeasurementResponse>> getAllMeasurements() {
        return ok(measurementService.getAllMeasurements());
    }

    @GetMapping("/sensor")
    public ResponseEntity<List<MeasurementResponse>> getAllMeasurementsBySensorName(
            @RequestParam(value = "name") String sensorName) {
        return ok(measurementService.getAllMeasurementsBySensorName(sensorName));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasurementResponse> getMeasurementById(@PathVariable("id") Long measurementId) {
        return ok(measurementService.getMeasurementById(measurementId));
    }

    @GetMapping("/rainyDaysCount")
    public ResponseEntity<Long> getRainyDaysCount() {
        return ok(measurementService.getRainyDaysCount());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMeasurement(@RequestBody @Valid MeasurementRequest measurementRequest,
                                                              BindingResult bindingResult) {
        measurementValidator.validate(measurementRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            var errorsMessage = returnErrorsToClient(bindingResult);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorsMessage);
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(measurementService.addMeasurement(measurementRequest));
    }
}