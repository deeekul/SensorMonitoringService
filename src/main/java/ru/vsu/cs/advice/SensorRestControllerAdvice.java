package ru.vsu.cs.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vsu.cs.exception.SensorNotFoundException;
import ru.vsu.cs.util.ErrorResponse;

@Log4j2
@RestControllerAdvice
public class SensorRestControllerAdvice {

    @ExceptionHandler(SensorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(SensorNotFoundException customException) {
        log.error("The requested sensor is not found!");
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMessage(customException.getMessage())
                .errorCode(customException.getStatus().value())
                .build(),
                HttpStatus.NOT_FOUND);
    }
}
