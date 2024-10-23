package ru.vsu.cs.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vsu.cs.dto.request.MeasurementRequest;
import ru.vsu.cs.dto.request.SensorRequest;
import ru.vsu.cs.dto.response.MeasurementResponse;
import ru.vsu.cs.dto.response.SensorResponse;
import ru.vsu.cs.entitiy.Measurement;
import ru.vsu.cs.entitiy.Sensor;
import ru.vsu.cs.exception.MeasurementNotFoundException;
import ru.vsu.cs.exception.SensorNotFoundException;
import ru.vsu.cs.mapper.MeasurementMapper;
import ru.vsu.cs.repository.MeasurementRepository;
import ru.vsu.cs.repository.SensorRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class MeasurementServiceTest {

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private MeasurementMapper measurementMapper;

    @Mock
    private SensorService sensorService;

    @InjectMocks
    private MeasurementService measurementService;

    @Test
    void getAllMeasurements_shouldReturnListOfMeasurementResponses_whenCalled() {

        // given
        var now = LocalDateTime.now();
        var foundMeasurements = List.of(
                new Measurement(
                        1L,
                        10.0,
                        true,
                        now,
                        new Sensor(1L, "sensor1")
                ),
                new Measurement(
                        2L,
                        19.0,
                        false,
                        now,
                        new Sensor(2L, "sensor2")
                )
        );

        var mappedMeasurements = List.of(
                new MeasurementResponse(
                        1L,
                        10.0,
                        true,
                        now,
                        new SensorResponse(1L, "sensor1")
                ),
                new MeasurementResponse(
                        2L,
                        19.0,
                        false,
                        now,
                        new SensorResponse(2L, "sensor2")
                )
        );

        doReturn(foundMeasurements)
                .when(measurementRepository)
                .findAll();

        doReturn(mappedMeasurements.get(0))
                .when(measurementMapper)
                .map(foundMeasurements.get(0));
        doReturn(mappedMeasurements.get(1))
                .when(measurementMapper)
                .map(foundMeasurements.get(1));

        // when
        var result = measurementService.getAllMeasurements();

        // then
        var expectedResult = List.of(
                new MeasurementResponse(
                        1L,
                        10.0,
                        true,
                        now,
                        new SensorResponse(1L, "sensor1")
                ),
                new MeasurementResponse(
                        2L,
                        19.0,
                        false,
                        now,
                        new SensorResponse(2L, "sensor2")
                )
        );
        assertThat(result).isEqualTo(expectedResult);

        verify(measurementRepository, times(1)).findAll();
        verify(measurementMapper, times(2)).map(any(Measurement.class));
        verifyNoMoreInteractions(measurementRepository);
        verifyNoMoreInteractions(measurementMapper);
    }

    @Test
    void getAllMeasurementsBySensorName_shouldReturnListOfMeasurementResponses_whenSensorExists() {

        // given
        final Long id = 1L;
        var sensorName = "ESM-10 Danfoss";
        var now = LocalDateTime.now();

        var mappedSensor = new SensorResponse(id, sensorName);

        var foundMeasurements = List.of(
                new Measurement(
                        1L,
                        10.0,
                        true,
                        now,
                        new Sensor(1L, "ESM-10 Danfoss")
                ),
                new Measurement(
                        2L,
                        19.0,
                        false,
                        now,
                        new Sensor(1L, "ESM-10 Danfoss")
                )
        );

        var mappedMeasurements = List.of(
                new MeasurementResponse(
                        1L,
                        10.0,
                        true,
                        now,
                        new SensorResponse(1L, "ESM-10 Danfoss")
                ),
                new MeasurementResponse(
                        2L,
                        19.0,
                        false,
                        now,
                        new SensorResponse(1L, "ESM-10 Danfoss")
                )
        );

        doReturn(mappedSensor)
                .when(sensorService)
                .getSensorByName(sensorName);

        doReturn(foundMeasurements)
                .when(measurementRepository)
                .findMeasurementsBySensorName(sensorName);

        doReturn(mappedMeasurements.get(0))
                .when(measurementMapper)
                .map(foundMeasurements.get(0));
        doReturn(mappedMeasurements.get(1))
                .when(measurementMapper)
                .map(foundMeasurements.get(1));

        // when
        var result = measurementService.getAllMeasurementsBySensorName("ESM-10 Danfoss");

        // then
        var expectedResult = List.of(
                new MeasurementResponse(
                        1L,
                        10.0,
                        true,
                        now,
                        new SensorResponse(1L, "ESM-10 Danfoss")
                ),
                new MeasurementResponse(
                        2L,
                        19.0,
                        false,
                        now,
                        new SensorResponse(1L, "ESM-10 Danfoss")
                )
        );
        assertThat(result).isEqualTo(expectedResult);

        verify(sensorService, times(1)).getSensorByName(sensorName);
        verify(measurementRepository, times(1)).findMeasurementsBySensorName(sensorName);
        verify(measurementMapper, times(2)).map(any(Measurement.class));
        verifyNoMoreInteractions(sensorService);
        verifyNoMoreInteractions(measurementRepository);
        verifyNoMoreInteractions(measurementMapper);
    }

    @Test
    void getAllMeasurementsBySensorName_shouldThrowSensorNotFoundException_whenSensorDoesNotExist() {

        // given
        final String sensorName = "Non-existent sensor";

        doThrow(SensorNotFoundException.class)
                .when(sensorService)
                .getSensorByName(sensorName);

        // then
        assertThrows(SensorNotFoundException.class, () -> measurementService.getAllMeasurementsBySensorName(sensorName));

        verify(sensorService, times(1)).getSensorByName(sensorName);
        verifyNoMoreInteractions(sensorService);
    }

    @Test
    void getMeasurementById_shouldReturnMeasurementResponse_whenMeasurementExists() {

        // given
        final Long id = 1L;
        var now = LocalDateTime.now();

        var foundMeasurement  = new Measurement(
                id,
                10.0,
                true,
                now,
                new Sensor(1L, "ESM-10 Danfoss")
        );

        var mappedMeasurement = new MeasurementResponse(
                id,
                10.0,
                true,
                now,
                new SensorResponse(1L, "ESM-10 Danfoss")
        );

        doReturn(Optional.of(foundMeasurement))
                .when(measurementRepository)
                .findById(id);

        doReturn(mappedMeasurement)
                .when(measurementMapper)
                .map(foundMeasurement);

        // when
        var result = measurementService.getMeasurementById(id);

        // then
        var expectedResult = new MeasurementResponse(
                1L,
                10.0,
                true,
                now,
                new SensorResponse(1L, "ESM-10 Danfoss")
        );
        assertThat(result).isEqualTo(expectedResult);

        verify(measurementRepository, times(1)).findById(id);
        verify(measurementMapper, times(1)).map(foundMeasurement);
        verifyNoMoreInteractions(measurementRepository);
        verifyNoMoreInteractions(measurementMapper);
    }

    @Test
    void getMeasurementById_shouldThrowMeasurementNotFoundException_whenMeasurementDoesNotExist() {

        // given
        final Long id = 1L;
        var emptyEntity = Optional.empty();

        doReturn(emptyEntity)
                .when(measurementRepository)
                .findById(id);

        // then
        assertThatThrownBy(() -> measurementService.getMeasurementById(id))
                .isInstanceOf(MeasurementNotFoundException.class)
                .hasMessageContaining("Измерение с id = 1 не найдено!");

        verify(measurementRepository, times(1)).findById(id);
        verifyNoMoreInteractions(measurementRepository);
    }

    @Test
    void getRainyDaysCount_shouldReturnRainyDaysCount_whenCalled() {

        // given
        var now = LocalDateTime.now();
        var foundMeasurements = List.of(
                new Measurement(
                        1L,
                        10.0,
                        true,
                        now,
                        new Sensor(1L, "ESM-10 Danfoss")
                ),
                new Measurement(
                        2L,
                        19.0,
                        false,
                        now,
                        new Sensor(2L, "TSensorEnc")
                ),
                new Measurement(
                        3L,
                        -7.0,
                        true,
                        now,
                        new Sensor(1L, "ESM-10 Danfoss")
                )
        );

        doReturn(foundMeasurements)
                .when(measurementRepository)
                .findAll();

        // when
        var result = measurementService.getRainyDaysCount();

        // then
        assertThat(result).isEqualTo(2L);

        verify(measurementRepository, times(1)).findAll();
        verifyNoMoreInteractions(measurementRepository);
    }

    @Test
    void addMeasurement_shouldReturnMeasurementResponse_whenCalled() {

        // given
        final Long sensorId = 1L;
        var sensorName = "ESM-10 Danfoss";
        var sensor = new Sensor(sensorId, sensorName);

        var measurementRequest = new MeasurementRequest(
                10.0,
                true,
                new SensorRequest(sensorName)
        );

        var now = LocalDateTime.now();

        var measurement = Measurement.builder()
                .value(measurementRequest.value())
                .raining(measurementRequest.raining())
                .sensor(sensor)
                .build();

        var savedMeasurement = new Measurement(
                1L,
                10.0,
                true,
               now,
                sensor
        );
        var mappedMeasurement = new MeasurementResponse(
                savedMeasurement.getId(),
                savedMeasurement.getValue(),
                savedMeasurement.getRaining(),
                now,
                new SensorResponse(sensorId, sensorName)
        );

        doReturn(Optional.of(sensor))
                .when(sensorRepository)
                .findByName(sensorName);

        doReturn(savedMeasurement)
                .when(measurementRepository)
                .save(measurement);

        doReturn(mappedMeasurement)
                .when(measurementMapper)
                .map(savedMeasurement);

        // when
        var result = measurementService.addMeasurement(measurementRequest);

        // then
        var expectedResult = new MeasurementResponse(
                1L,
                10.0,
                true,
                now,
                new SensorResponse(sensorId, sensorName)
        );
        assertThat(result.measurementDateTime()).isEqualTo(expectedResult.measurementDateTime());

        verify(sensorRepository, times(1)).findByName(sensorName);
        verify(measurementRepository, times(1)).save(measurement);
        verify(measurementMapper, times(1)).map(savedMeasurement);
        verifyNoMoreInteractions(sensorRepository);
        verifyNoMoreInteractions(measurementRepository);
        verifyNoMoreInteractions(measurementMapper);
    }
}