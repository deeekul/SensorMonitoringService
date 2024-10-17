package ru.vsu.cs.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class MeasurementNotFoundException extends RuntimeException{

    private final HttpStatus status;

    public MeasurementNotFoundException(String message) {
        super(message);
        status = HttpStatus.BAD_REQUEST;
    }

    public MeasurementNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.status = httpStatus;
    }
}
