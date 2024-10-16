--liquibase formatted sql

--changeset deekul:20241014_sensors_data context:local

-- sensors data

INSERT INTO sensors(name) VALUES ('sensor1'),
                                 ('sensor2'),
                                 ('sensor3');
