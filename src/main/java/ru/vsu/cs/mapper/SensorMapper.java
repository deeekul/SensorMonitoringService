package ru.vsu.cs.mapper;

import org.mapstruct.Mapper;
import ru.vsu.cs.dto.request.SensorRequest;
import ru.vsu.cs.dto.response.SensorResponse;
import ru.vsu.cs.entitiy.Sensor;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    SensorResponse map(Sensor sensor);

    Sensor map(SensorRequest sensorCreateRequest);
}
