--liquibase formatted sql

--changeset deekul:create_table_sensors

create table sensors
(
    id bigint generated by default as identity primary key,
    name varchar(255) not null unique
);