package ru.vsu.cs.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.vsu.cs.dto.request.MeasurementRequest;
import ru.vsu.cs.mapper.MeasurementMapper;
import ru.vsu.cs.repository.SensorRepository;

@RequiredArgsConstructor
@Component
public class MeasurementValidator implements Validator {

    private final SensorRepository sensorRepository;

    private final MeasurementMapper measurementMapper;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        var measurement = measurementMapper.map((MeasurementRequest) o);

        if (measurement.getSensor() == null) {
            return;
        }

        if (sensorRepository.findByName(measurement.getSensor().getName()).isEmpty()) {
            errors.rejectValue("sensor", "Сенсора с таким именем не существует!");
        }
    }
}
