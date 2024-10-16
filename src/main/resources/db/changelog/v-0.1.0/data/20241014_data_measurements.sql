--liquibase formatted sql

--changeset deekul:20241014_measurements_data context:local

-- measurements data

INSERT INTO measurements(value, raining, measurement_date_time, sensor) VALUES (10, true, NOW(),'sensor1'),
                                                                               (19, false, NOW(),'sensor2'),
                                                                               (5, false, NOW(), 'sensor3'),
                                                                               (21, true, NOW(), 'sensor1'),
                                                                               (-2, false, NOW(), 'sensor3'),
                                                                               (9, true, NOW(), 'sensor2'),
                                                                               (-5, true, NOW(), 'sensor3'),
                                                                               (17, false, NOW(), 'sensor1'),
                                                                               (3, true, NOW(), 'sensor2'),
                                                                               (0, true, NOW(), 'sensor1'),
                                                                               (-5, false, NOW(), 'sensor2'),
                                                                               (4, false, NOW(), 'sensor3'),
                                                                               (26, true, NOW(), 'sensor2'),
                                                                               (13, false, NOW(), 'sensor1'),
                                                                               (-10, true, NOW(), 'sensor1');