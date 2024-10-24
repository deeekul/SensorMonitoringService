package ru.vsu.cs.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.vsu.cs.dto.request.SensorRequest;
import ru.vsu.cs.dto.response.SensorResponse;
import ru.vsu.cs.entitiy.Sensor;
import ru.vsu.cs.exception.SensorNotFoundException;
import ru.vsu.cs.mapper.SensorMapper;
import ru.vsu.cs.repository.SensorRepository;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SensorServiceTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private SensorMapper sensorMapper;

    @InjectMocks
    private SensorService sensorService;

    @Test
    void getAllSensors_shouldReturnListOfSensorResponse_whenCalled() {

        // given
        var foundSensors = List.of(
                new Sensor(1L, "sensor1"),
                new Sensor(2L, "sensor2"),
                new Sensor(3L, "sensor3")
        );

        var mappedSensors = List.of(
                new SensorResponse(1L, "sensor1"),
                new SensorResponse(2L, "sensor2"),
                new SensorResponse(3L, "sensor3")
        );

        doReturn(foundSensors)
                .when(sensorRepository)
                .findAll();

        doReturn(mappedSensors.get(0))
                .when(sensorMapper)
                .map(foundSensors.get(0));
        doReturn(mappedSensors.get(1))
                .when(sensorMapper)
                .map(foundSensors.get(1));
        doReturn(mappedSensors.get(2))
                .when(sensorMapper)
                .map(foundSensors.get(2));

        // when
        var result = sensorService.getAllSensors();

        // then
        var expectedSensorResponses = List.of(
                new SensorResponse(1L, "sensor1"),
                new SensorResponse(2L, "sensor2"),
                new SensorResponse(3L, "sensor3")
        );
        assertThat(result).isEqualTo(expectedSensorResponses);

        verify(sensorRepository, times(1)).findAll();
        verify(sensorMapper, times(3)).map(any(Sensor.class));
        verifyNoMoreInteractions(sensorRepository);
        verifyNoMoreInteractions(sensorMapper);
    }

    @Test
    void getSensorById_shouldReturnSensorResponse_whenSensorExists() {

        // given
        final Long id = 1L;
        var foundSensor = new Sensor(id, "sensor1");
        var mappedSensor = new SensorResponse(id, "sensor1");

        doReturn(Optional.of(foundSensor))
                .when(sensorRepository)
                .findById(id);
        doReturn(mappedSensor)
                .when(sensorMapper)
                .map(foundSensor);

        // when
        var result = sensorService.getSensorById(id);

        // then
        var expectedResult = new SensorResponse(id, "sensor1");
        assertThat(result).isEqualTo(expectedResult);

        verify(sensorRepository, times(1)).findById(id);
        verify(sensorMapper, times(1)).map(foundSensor);
        verifyNoMoreInteractions(sensorRepository);
        verifyNoMoreInteractions(sensorMapper);
    }

    @Test
    void getSensorById_shouldThrowSensorNotFoundException_whenSensorDoesNotExist() {

        // given
        final Long id = 1L;
        var emptyEntity = Optional.empty();

        doReturn(emptyEntity)
                .when(sensorRepository)
                .findById(id);

        // then
        assertThrows(SensorNotFoundException.class, () -> sensorService.getSensorById(id));

        verify(sensorRepository, times(1)).findById(id);
    }

    @Test
    void getSensorByName_shouldReturnSensorResponse_whenSensorExists() {

        // given
        final Long id = 1L;
        final String sensorName = "ESM-10 Danfoss";

        var foundSensor = new Sensor(id, sensorName);
        var mappedSensor = new SensorResponse(id, sensorName);

        doReturn(Optional.of(foundSensor))
                .when(sensorRepository)
                .findByName(sensorName);

        doReturn(mappedSensor)
                .when(sensorMapper)
                .map(foundSensor);

        // when
        var result = sensorService.getSensorByName(sensorName);

        // then
        var expectedResult = new SensorResponse(1L, "ESM-10 Danfoss");
        assertThat(result).isEqualTo(expectedResult);

        verify(sensorRepository, times(1)).findByName(sensorName);
        verify(sensorMapper, times(1)).map(foundSensor);
        verifyNoMoreInteractions(sensorRepository);
        verifyNoMoreInteractions(sensorMapper);
    }

    @Test
    void getSensorByName_shouldThrowSensorNotFoundException_whenSensorDoesNotExist() {

        // given
        final String sensorName = "ESM-10 Danfoss";
        var emptyEntity = Optional.empty();

        doReturn(emptyEntity)
                .when(sensorRepository)
                .findByName(sensorName);

        // then
        assertThatThrownBy(() -> sensorService.getSensorByName(sensorName))
                .isInstanceOf(SensorNotFoundException.class)
                .hasMessageContaining("Сенсор с name = ESM-10 Danfoss не найден!");

        verify(sensorRepository, times(1)).findByName(sensorName);
        verifyNoMoreInteractions(sensorRepository);
    }

    @Test
    void registerSensor_shouldReturnSensorResponse_whenInputIsValid() {

        // given
        final String sensorName = "ESM-10 Danfoss";

        var sensorRequest = new SensorRequest(sensorName);
        var sensor = Sensor.builder()
                .id(null)
                .name(sensorName)
                .build();

        var savedSensor = Sensor.builder()
                .id(1L)
                .name(sensorName)
                .build();
        var sensorResponse = new SensorResponse(savedSensor.getId(), savedSensor.getName());

        doReturn(sensor)
                .when(sensorMapper)
                .map(sensorRequest);

        doReturn(savedSensor)
                .when(sensorRepository)
                .save(sensor);

        doReturn(sensorResponse)
                .when(sensorMapper)
                .map(savedSensor);

        // when
        var result = sensorService.registerSensor(sensorRequest);

        // then
        var expectedResult = new SensorResponse(1L, "ESM-10 Danfoss");
        assertThat(result).isEqualTo(expectedResult);

        verify(sensorMapper, times(1)).map(sensorRequest);
        verify(sensorRepository, times(1)).save(sensor);
        verify(sensorMapper, times(1)).map(savedSensor);
        verifyNoMoreInteractions(sensorRepository);
        verifyNoMoreInteractions(sensorMapper);
    }

    @Test
    void updateSensorById_shouldUpdateAndReturnSensorResponse_whenSensorExists() {

        // given
        final Long id = 1L;
        final String newSensorName = "TSensorEnc";

        var sensorRequest = new SensorRequest(newSensorName);
        var foundSensor = new Sensor(id, "ESM-10 Danfoss");
        var sensorResponse = new SensorResponse(id, newSensorName);

        doReturn(Optional.of(foundSensor))
                .when(sensorRepository)
                .findById(id);

        doReturn(sensorResponse)
                .when(sensorMapper)
                .map(foundSensor);

        // when
        var result = sensorService.updateSensorById(id, sensorRequest);

        // then
        var expectedResult = new SensorResponse(1L, "TSensorEnc");
        assertThat(result).isEqualTo(expectedResult);

        verify(sensorRepository, times(1)).findById(id);
        verify(sensorRepository, times(1)).save(foundSensor);
        verify(sensorMapper, times(1)).map(foundSensor);
        verifyNoMoreInteractions(sensorRepository);
        verifyNoMoreInteractions(sensorMapper);
    }

    @Test
    void updateSensorById_shouldThrowSensorNotFoundException_WhenSensorDoesNotExist() {

        // given
        final Long id = 5L;
        var sensorRequest = new SensorRequest("TSensorEnc");
        var emptyEntity = Optional.empty();

        doReturn(emptyEntity)
                .when(sensorRepository)
                .findById(id);

        // then
        assertThatThrownBy(() -> sensorService.updateSensorById(id, sensorRequest))
                .isInstanceOf(SensorNotFoundException.class)
                .hasMessageContaining("Сенсор с id = 5 не найден!");

        verify(sensorRepository, times(1)).findById(id);
        verifyNoMoreInteractions(sensorRepository);
    }

    @Test
    void deleteSensorById_shouldDeleteSensor_whenSensorExists() {

        // given
        final Long id = 1L;
        var foundSensor = new Sensor(id, "ESM-10 Danfoss");

        doReturn(Optional.of(foundSensor))
                .when(sensorRepository)
                .findById(id);

        // when
        sensorService.deleteSensorById(id);

        // then
        verify(sensorRepository, times(1)).findById(id);
        verify(sensorRepository, times(1)).delete(foundSensor);
        verifyNoMoreInteractions(sensorRepository);
    }

    @Test
    void deleteSensorById_shouldThrowSensorNotFoundException_whenSensorDoesNotExist() {

        // given
        final Long id = 1L;
        var emptyEntity = Optional.empty();

        doReturn(emptyEntity)
                .when(sensorRepository)
                .findById(id);

        // then
        assertThrows(SensorNotFoundException.class, () -> sensorService.deleteSensorById(id));

        verify(sensorRepository, times(1)).findById(id);
        verifyNoMoreInteractions(sensorRepository);
    }
}