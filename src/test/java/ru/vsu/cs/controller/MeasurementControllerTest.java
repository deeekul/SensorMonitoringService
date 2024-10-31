package ru.vsu.cs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.vsu.cs.dto.request.MeasurementRequest;
import ru.vsu.cs.dto.request.SensorRequest;
import ru.vsu.cs.entitiy.Measurement;
import ru.vsu.cs.entitiy.Sensor;
import ru.vsu.cs.repository.MeasurementRepository;
import ru.vsu.cs.repository.SensorRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
class MeasurementControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        measurementRepository.deleteAll();
        sensorRepository.deleteAll();
    }

    @Test
    void getAllMeasurements_whenGetMeasurements_thenStatusOk() throws Exception {
        // given
        var sensor = new Sensor(null, "sensor1");
        sensorRepository.save(sensor);

        var measurements = List.of(
                new Measurement(
                        null,
                        10.0,
                        true,
                        null,
                        sensor
                ),
                new Measurement(
                        null,
                        -15.0,
                        false,
                        null,
                        sensor
                )
        );
        measurementRepository.saveAll(measurements);

        // when
        ResultActions response = mockMvc.perform(get("/api/measurements")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAllMeasurementsBySensorName_whenGetMeasurementsBySensorName_thenStatusOk() throws Exception {
        // given
        var sensors = List.of(
                new Sensor(null, "Sensor 1"),
                new Sensor(null, "Sensor 2")
        );
        sensorRepository.saveAll(sensors);

        var measurements = List.of(
                new Measurement(
                        null, 10.0, true, null, sensors.get(0)
                ),
                new Measurement(
                        null, -15.0, false, null, sensors.get(0)
                ),
                new Measurement(
                        null, 9.0, true, null, sensors.get(1)
                )
        );
        measurementRepository.saveAll(measurements);

        // when
        ResultActions response = mockMvc.perform(get("/api/measurements/sensor?name={name}", "Sensor 1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAllMeasurementsBySensorName_whenSensorDoesNotExist_thenStatusNotFound() throws Exception {
        // given
        final String sensorName = "Non existing sensor";

        // when
        ResultActions response = mockMvc.perform(get("/api/measurements/sensor?name={name}", sensorName)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNotFound());
        response.andExpect(jsonPath("$.errorMessage").isNotEmpty());
        response.andExpect(jsonPath("$.errorCode").value(404));
    }

    @Test
    void getMeasurementById_whenMeasurementExists_thenStatusOk() throws Exception {
        // given
        var sensor = new Sensor(null, "Sensor 1");
        var savedSensor = sensorRepository.save(sensor);

        var measurement = new Measurement(
                null,
                10.0,
                true,
                null,
                savedSensor
        );
        var savedMeasurement = measurementRepository.save(measurement);

        // when
        ResultActions response = mockMvc.perform(get("/api/measurements/{id}", savedMeasurement.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.value").value(10.0));
    }

    @Test
    void getMeasurementById_whenMeasurementDoesNotExist_thenStatusNotFound() throws Exception {
        // given
        final Long invalidId = 100L;

        // when
        ResultActions response = mockMvc.perform(get("/api/measurements/{id}", invalidId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNotFound());
        response.andExpect(jsonPath("$.errorMessage").isNotEmpty());
        response.andExpect(jsonPath("$.errorCode").value(404));
    }

    @Test
    void addMeasurement_whenValidData_thenStatusCreated() throws Exception {
        // given
        var sensor = new Sensor(null, "Sensor 1");
        sensorRepository.save(sensor);

        var measurementRequest = new MeasurementRequest(
                12.5,
                true,
                new SensorRequest(sensor.getName())
        );
        String jsonRequest = objectMapper.writeValueAsString(measurementRequest);

        // when
        ResultActions response = mockMvc.perform(post("/api/measurements/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));

        // then
        response.andExpect(status().isCreated());
        response.andExpect(jsonPath("$.value").value(12.5));

        assertThat(measurementRepository.findMeasurementsBySensorName("Sensor 1")).hasSize(1);
    }

    @Test
    void addMeasurement_whenInvalidData_thenStatusBadRequest() throws Exception {
        // given
        var sensor = new Sensor(null, "Sensor 1");
        sensorRepository.save(sensor);

        var measurementRequest = new MeasurementRequest(
                10.0,
                true,
                new SensorRequest("Non existing sensor")
        );
        String jsonRequest = objectMapper.writeValueAsString(measurementRequest);

        // when
        ResultActions response = mockMvc.perform(post("/api/measurements/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));

        // then

        response.andExpect(status().isBadRequest());
        assertThat(measurementRepository.findAll()).hasSize(0);
    }

    @Test
    void getRainyMeasurementCount_whenGetRainyDaysCount_thenStatusOk() throws Exception {
        // given
        var sensor = new Sensor(null, "Sensor 1");
        var savedSensor = sensorRepository.save(sensor);

        var measurements = List.of(
                new Measurement(
                        null,
                        10.0,
                        true,
                        null,
                        savedSensor),
                new Measurement(
                        null,
                        -15.0,
                        false,
                        null,
                        savedSensor),
                new Measurement(
                        null,
                        9.0,
                        true,
                        null,
                        savedSensor)
        );
        System.out.println(measurementRepository.saveAll(measurements));

        // when
        ResultActions response = mockMvc.perform(get("/api/measurements/rainyMeasurementCount")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$").value(2));
    }
}