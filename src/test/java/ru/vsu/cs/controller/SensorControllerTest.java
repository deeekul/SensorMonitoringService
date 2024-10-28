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
import ru.vsu.cs.dto.request.SensorRequest;
import ru.vsu.cs.entitiy.Sensor;
import ru.vsu.cs.repository.SensorRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class SensorControllerTest {

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        sensorRepository.deleteAll();
    }

    @Test
    void getAllSensors_whenGetSensors_thenStatusOk() throws Exception {
        //given
        var sensors = List.of(
                new Sensor(null, "Sensor 1"),
                new Sensor(null, "Sensor 2"),
                new Sensor(null, "Sensor 3")
        );
        sensorRepository.saveAll(sensors);

        // when
        ResultActions response = mockMvc.perform(get("/api/sensors")
                        .accept(MediaType.APPLICATION_JSON));
        // then
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$").isArray());
        response.andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void getSensorById_whenSensorExists_thenStatusOk() throws Exception {
        // given
        var sensor = new Sensor(null, "ESM-10 Danfoss");
        var savedSensor = sensorRepository.save(sensor);

        // when
        ResultActions response = mockMvc.perform(get("/api/sensors/{id}", savedSensor.getId())
                .accept(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.name", is("ESM-10 Danfoss")));
    }

    @Test
    void getSensorById_whenSensorDoesNotExist_thenStatusNotFound() throws Exception {
        // when
        ResultActions response = mockMvc.perform(get("/api/sensors/{id}", 50L)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNotFound());
    }

    @Test
    void createSensor_whenValidData_thenStatusCreated() throws Exception {
        // given
        var sensorRequest = new SensorRequest("ESM-10 Danfoss");
        String jsonRequest = objectMapper.writeValueAsString(sensorRequest);

        // when
        ResultActions response = mockMvc.perform(post("/api/sensors/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest));

        // then
        response.andExpect(status().isCreated());
        response.andExpect(jsonPath("$.name", is("ESM-10 Danfoss")));

        assertThat(sensorRepository.findByName(sensorRequest.name())).isPresent();
    }

    @Test
    void createSensor_whenInvalidData_thenStatusBadRequest() throws Exception {
        // given
        var sensorRequest = new SensorRequest("");
        String jsonRequest = objectMapper.writeValueAsString(sensorRequest);

        // when
        ResultActions response = mockMvc.perform(post("/api/sensors/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest));

        // then
        response.andExpect(status().isBadRequest());
        response.andExpect(jsonPath("$.errorMessage").isNotEmpty());
        response.andExpect(jsonPath("$.errorCode", is(400)));
    }

    @Test
    void updateSensorById_whenValidData_thenStatusOk() throws Exception {
        // given
        var sensor = new Sensor(null, "Old sensor");
        var savedSensor = sensorRepository.save(sensor);

        var sensorRequest = new SensorRequest("Updated Sensor");
        String jsonRequest = objectMapper.writeValueAsString(sensorRequest);

        // when
        ResultActions response = mockMvc.perform(put("/api/sensors/update/{id}", savedSensor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));

        // then
        response.andExpect(status().isOk());
        response.andExpect(jsonPath("$.id").value(sensor.getId()));
        response.andExpect(jsonPath("$.name").value("Updated Sensor"));
    }

    @Test
    void updateSensorById_whenInvalidData_thenBadRequest() throws Exception {
        // given
        var sensor = new Sensor(null, "Old sensor");
        var savedSensor = sensorRepository.save(sensor);

        SensorRequest sensorRequest = new SensorRequest("");
        String jsonRequest = objectMapper.writeValueAsString(sensorRequest);

        // when
        ResultActions response = mockMvc.perform(put("/api/sensors/update/{id}", savedSensor.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest));

        // then
        response.andExpect(status().isBadRequest());
        response.andExpect(jsonPath("$.errorMessage").isNotEmpty());
        response.andExpect(jsonPath("$.errorCode", is(400)));
    }

    @Test
    void deleteSensorById_whenSensorExists_thenStatusOk() throws Exception {
        // given
        var sensor = new Sensor(null, "Sensor to delete");
        Sensor savedSensor = sensorRepository.save(sensor);

        assertThat(sensorRepository.findByName(savedSensor.getName())).isPresent();

        // when
        ResultActions response = mockMvc.perform(delete("/api/sensors/delete/{id}", savedSensor.getId()));

        // then
        response.andExpect(status().isOk());
        assertThat(sensorRepository.findByName(savedSensor.getName())).isEmpty();
    }

    @Test
    void deleteSensorById_whenSensorDoesNotExist_thenBadRequest() throws Exception {
        // when
        ResultActions response = mockMvc.perform(delete("/api/sensors/delete/{id}", 100L));

        // then
        response.andExpect(status().isNotFound());
        response.andExpect(jsonPath("$.errorMessage", is("Сенсор с id = 100 не найден!")));
        response.andExpect(jsonPath("$.errorCode", is(404)));
    }
}