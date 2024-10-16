package ru.vsu.cs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.entitiy.Measurement;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    @Query(value = """
            select m from Measurement m inner join Sensor s
            on m.sensor.id = s.id
            where s.name = :sensorName
            """)
    Optional<List<Measurement>> findMeasurementsBySensorName(@Param("sensorName") String sensorName);
}
