package ru.vsu.cs.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vsu.cs.dto.request.SensorRequest;
import ru.vsu.cs.entitiy.Sensor;
import ru.vsu.cs.exception.SensorNotFoundException;
import ru.vsu.cs.mapper.SensorMapper;
import ru.vsu.cs.repository.SensorRepository;
import ru.vsu.cs.service.SensorService;

@RequiredArgsConstructor
@Component
public class SensorValidator implements Validator {

    private final SensorRepository sensorRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Sensor.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        var sensorRequest = (SensorRequest) o;

        if (sensorRepository.findByName(sensorRequest.name()).isPresent()) {
            errors.rejectValue("name", "Сенсор с таким именем уже существует!");
        }
    }
}
