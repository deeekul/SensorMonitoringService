package ru.vsu.cs.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class SensorNotFoundException extends RuntimeException {

    private final HttpStatus status;

    public SensorNotFoundException(String message) {
        super(message);
        status = HttpStatus.BAD_REQUEST;
    }

    public SensorNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }
}
