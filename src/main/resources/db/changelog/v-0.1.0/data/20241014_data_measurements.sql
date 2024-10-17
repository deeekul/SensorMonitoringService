--liquibase formatted sql

--changeset deekul:20241014_measurements_data context:local

-- measurements data

INSERT INTO measurements(value, raining, measurement_date_time, sensor)

VALUES (10, true, '2024-10-01 08:30:21','sensor1'),
       (19, false, '2024-10-02 10:45:02','sensor2'),
       (5, false, '2024-10-03 11:22:09', 'sensor3'),
       (21, true, '2024-10-04 09:15:54', 'sensor1'),
       (-2, false, '2024-10-05 08:14:32', 'sensor3'),
       (9, true, '2024-10-06 09:00:07', 'sensor2'),
       (-5, true, '2024-10-07 07:34:51', 'sensor3'),
       (17, false, '2024-10-08 10:06:25', 'sensor1'),
       (3, true, '2024-10-09 11:11:48', 'sensor2'),
       (0, true, '2024-10-10 08:03:12', 'sensor1'),
       (-5, false, '2024-10-11 12:23:05', 'sensor2'),
       (4, false, '2024-10-12 09:50:39', 'sensor3'),
       (26, true, '2024-10-13 08:31:43', 'sensor2'),
       (13, false, '2024-10-14 11:40:13', 'sensor3'),
       (-10, true, '2024-10-15 10:10:56"', 'sensor1');