package ru.vsu.cs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.vsu.cs.dto.request.MeasurementRequest;
import ru.vsu.cs.dto.response.MeasurementResponse;
import ru.vsu.cs.entitiy.Measurement;
import ru.vsu.cs.exception.MeasurementNotFoundException;
import ru.vsu.cs.exception.SensorNotFoundException;
import ru.vsu.cs.mapper.MeasurementMapper;
import ru.vsu.cs.repository.MeasurementRepository;
import ru.vsu.cs.repository.SensorRepository;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    private final SensorRepository sensorRepository;

    private final MeasurementMapper measurementMapper;

    public List<MeasurementResponse> getAllMeasurements() {
        log.info("Getting all measurements");
        var measurements =  measurementRepository.findAll();

        return measurements.stream()
                .map(measurementMapper::map)
                .toList();
    }

    public List<MeasurementResponse> getAllMeasurementsBySensorName(String sensorName) {
        log.info("Getting all measurements with sensorName={}", sensorName);
        var measurements = findMeasurementsBySensorNameOrThrowException(sensorName);

        return measurements.stream()
                .map(measurementMapper::map)
                .toList();
    }

    public MeasurementResponse getMeasurementById(Long measurementId) {
        log.info("Getting measurement with id={}", measurementId);
        var measurement = findMeasurementByIdOrThrowException(measurementId);

        return measurementMapper.map(measurement);
    }

    public Long getRainyDaysCount() {
        return measurementRepository.findAll().stream()
                .filter((Measurement::getRaining))
                .count();
    }

    public MeasurementResponse addMeasurement(MeasurementRequest measurementRequest) {
        log.info("Adding a measurement made by sensor with name={} ...",
                measurementRequest.sensor().name());
        var sensor = sensorRepository.findByName(measurementRequest.sensor().name()).get();

        var measurement = Measurement.builder()
                        .value(measurementRequest.value())
                        .raining(measurementRequest.raining())
                        .measurementDateTime(LocalDateTime.now())
                        .sensor(sensor)
                        .build();

        measurementRepository.save(measurement);

        log.info("Measurement with id={} was added!", measurement.getId());
        return measurementMapper.map(measurement);
    }

    private Measurement findMeasurementByIdOrThrowException(Long measurementId) {
        return measurementRepository.findById(measurementId)
                .orElseThrow(() -> new MeasurementNotFoundException("Измерение с id = "
                        + measurementId + " не найдено!"
                ));
    }

    private List<Measurement> findMeasurementsBySensorNameOrThrowException(String sensorName) {
        return measurementRepository.findMeasurementsBySensorName(sensorName)
                .orElseThrow(() -> new SensorNotFoundException("Сенсора с именем = "
                        + sensorName + " не найдено!"
                ));
    }
}
