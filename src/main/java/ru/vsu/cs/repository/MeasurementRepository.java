package ru.vsu.cs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.vsu.cs.entitiy.Measurement;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    @Query("select m from Measurement m join fetch m.sensor")
    List<Measurement> findAll();

    @Query(value = """
            select m from Measurement m inner join Sensor s
            on m.sensor.id = s.id
            where s.name = :sensorName
            """)
    List<Measurement> findMeasurementsBySensorName(@Param("sensorName") String sensorName);
}
