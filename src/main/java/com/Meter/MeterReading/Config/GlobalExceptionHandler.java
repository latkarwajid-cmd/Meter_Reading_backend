package com.Meter.MeterReading.Config;
 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Map.of(
                        "success", false,
                        "error", ex.getMessage() != null ? ex.getMessage() : "Bad request",
                        "message", ex.getMessage() != null ? ex.getMessage() : "Bad request"
                )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of(
                        "success", false,
                        "error", ex.getMessage() != null ? ex.getMessage() : "Internal server error",
                        "message", ex.getMessage() != null ? ex.getMessage() : "Internal server error"
                )
        );
    }
}
