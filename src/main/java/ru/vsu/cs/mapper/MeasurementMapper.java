package ru.vsu.cs.mapper;

import org.mapstruct.Mapper;
import ru.vsu.cs.dto.request.MeasurementRequest;
import ru.vsu.cs.dto.response.MeasurementResponse;
import ru.vsu.cs.entitiy.Measurement;

@Mapper(componentModel = "spring")
public interface MeasurementMapper {

    MeasurementResponse map(Measurement measurement);

    Measurement map (MeasurementRequest measurementRequest);
}
