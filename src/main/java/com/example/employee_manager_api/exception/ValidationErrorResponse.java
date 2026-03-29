package com.example.employee_manager_api.exception;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        int status,
        String message,
        Map<String, String> errors,
        LocalDateTime timestamp
) {
    public ValidationErrorResponse(int status, String message, Map<String, String> errors){
        this(status, message, errors, LocalDateTime.now());
    }
}