package ru.vsu.cs.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.vsu.cs.exception.MeasurementNotFoundException;
import ru.vsu.cs.util.ErrorResponse;

@Log4j2
@RestControllerAdvice
public class MeasurementRestControllerAdvice {

    @ExceptionHandler(MeasurementNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(MeasurementNotFoundException customException) {
        log.error("The requested measurement is not found!");
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorMessage(customException.getMessage())
                .errorCode(customException.getStatus().value())
                .build(),
                HttpStatus.NOT_FOUND);
    }
}
