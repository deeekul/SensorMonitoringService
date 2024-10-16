package ru.vsu.cs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.cs.dto.request.SensorRequest;
import ru.vsu.cs.dto.response.SensorResponse;
import ru.vsu.cs.entitiy.Sensor;
import ru.vsu.cs.exception.SensorNotFoundException;
import ru.vsu.cs.mapper.SensorMapper;
import ru.vsu.cs.repository.SensorRepository;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    private final SensorMapper sensorMapper;

    public List<SensorResponse> getAllSensors() {
        log.info("Getting all sensors ...");
        var sensors = sensorRepository.findAll();

        return sensors.stream()
                .map(sensorMapper::map)
                .toList();
    }

    public SensorResponse getSensorById(Long sensorId) {
        log.info("Getting sensor with id={} ...", sensorId);
        var sensor = findSensorByIdOrThrowException(sensorId);

        return sensorMapper.map(sensor);
    }

    public SensorResponse getSensorByName(String sensorName) {
        log.info("Getting sensor with name={} ...", sensorName);
        var sensor = findSensorByNameOrThrowException(sensorName);

        return sensorMapper.map(sensor);
    }

    public SensorResponse registerSensor(SensorRequest sensorRequest) {
        log.info("Creating sensor {} ...", sensorRequest.name());
        var sensor = sensorRepository.save(sensorMapper.map(sensorRequest));

        log.info("Sensor with name={} was created!", sensor.getName());
        return sensorMapper.map(sensor);
    }

    @Transactional
    public SensorResponse updateSensorById(Long sensorId, SensorRequest sensorRequest) {
        log.info("Updating sensor with id={} ...", sensorId);
        var sensor = findSensorByIdOrThrowException(sensorId);

        sensor.setName(sensorRequest.name());
        sensorRepository.save(sensor);

        log.info("Sensor with id={} was updated!", sensorId);
        return sensorMapper.map(sensor);
    }

    public void deleteSensorById(Long sensorId) {
        sensorRepository.deleteById(sensorId);
        log.info("Sensor with id={} was deleted!", sensorId);
    }

    private Sensor findSensorByIdOrThrowException(Long sensorId) {
        return sensorRepository.findById(sensorId)
                .orElseThrow(
                        () -> new SensorNotFoundException("Сенсор с id = " +
                                sensorId + " не найден!", HttpStatus.NOT_FOUND));
    }

    private Sensor findSensorByNameOrThrowException(String sensorName) {
        return sensorRepository.findByName(sensorName)
                .orElseThrow(
                        () -> new SensorNotFoundException("Сенсор с name=" +
                                sensorName + " не найден!", HttpStatus.NOT_FOUND));
    }
}
