package com.karthik.ai_chatbot_api.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));

        return ResponseEntity.badRequest().body(
                ErrorResponse.builder()
                        .status(400)
                        .error("Validation failed")
                        .details(errors.toString())
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntime(RuntimeException ex) {

        String message = ex.getMessage();

        if (message != null && message.contains("429")) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
                    ErrorResponse.builder()
                            .status(429)
                            .error("Rate limit reached")
                            .details("Too many requests to Gemini API. Please wait a moment and try again.")
                            .timestamp(Instant.now())
                            .build()
            );
        }

        if (message != null && message.contains("Retries exhausted")) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                    ErrorResponse.builder()
                            .status(503)
                            .error("AI service temporarily unavailable")
                            .details("Please try again in a few seconds.")
                            .timestamp(Instant.now())
                            .build()
            );
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorResponse.builder()
                        .status(500)
                        .error("AI service error")
                        .details(message)
                        .timestamp(Instant.now())
                        .build()
        );
    }

    @Data @Builder
    public static class ErrorResponse {
        private int status;
        private String error;
        private String details;
        private Instant timestamp;
    }
}
