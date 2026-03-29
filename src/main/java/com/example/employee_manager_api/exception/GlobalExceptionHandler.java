package com.example.employee_manager_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()) {
                    @Override
                    public HttpStatusCode getStatusCode() {
                        return null;
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return ErrorResponse.super.getHeaders();
                    }

                    @Override
                    public ProblemDetail getBody() {
                        return null;
                    }

                    @Override
                    public String getTypeMessageCode() {
                        return ErrorResponse.super.getTypeMessageCode();
                    }

                    @Override
                    public String getTitleMessageCode() {
                        return ErrorResponse.super.getTitleMessageCode();
                    }

                    @Override
                    public String getDetailMessageCode() {
                        return ErrorResponse.super.getDetailMessageCode();
                    }

                    @Override
                    public Object @Nullable [] getDetailMessageArguments() {
                        return ErrorResponse.super.getDetailMessageArguments();
                    }

                    @Override
                    public Object @Nullable [] getDetailMessageArguments(MessageSource messageSource, Locale locale) {
                        return ErrorResponse.super.getDetailMessageArguments(messageSource, locale);
                    }

                    @Override
                    public ProblemDetail updateAndGetBody(@Nullable MessageSource messageSource, Locale locale) {
                        return ErrorResponse.super.updateAndGetBody(messageSource, locale);
                    }
                });
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()) {
                    @Override
                    public HttpStatusCode getStatusCode() {
                        return null;
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return ErrorResponse.super.getHeaders();
                    }

                    @Override
                    public ProblemDetail getBody() {
                        return null;
                    }

                    @Override
                    public String getTypeMessageCode() {
                        return ErrorResponse.super.getTypeMessageCode();
                    }

                    @Override
                    public String getTitleMessageCode() {
                        return ErrorResponse.super.getTitleMessageCode();
                    }

                    @Override
                    public String getDetailMessageCode() {
                        return ErrorResponse.super.getDetailMessageCode();
                    }

                    @Override
                    public Object @Nullable [] getDetailMessageArguments() {
                        return ErrorResponse.super.getDetailMessageArguments();
                    }

                    @Override
                    public Object @Nullable [] getDetailMessageArguments(MessageSource messageSource, Locale locale) {
                        return ErrorResponse.super.getDetailMessageArguments(messageSource, locale);
                    }

                    @Override
                    public ProblemDetail updateAndGetBody(@Nullable MessageSource messageSource, Locale locale) {
                        return ErrorResponse.super.updateAndGetBody(messageSource, locale);
                    }
                });
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        for (FieldError field : ex.getBindingResult().getFieldErrors()) {
            errors.put(field.getField(), field.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorResponse(400, "Erro de validação", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500, "Erro interno do servidor") {
                    @Override
                    public HttpStatusCode getStatusCode() {
                        return null;
                    }

                    @Override
                    public HttpHeaders getHeaders() {
                        return ErrorResponse.super.getHeaders();
                    }

                    @Override
                    public ProblemDetail getBody() {
                        return null;
                    }

                    @Override
                    public String getTypeMessageCode() {
                        return ErrorResponse.super.getTypeMessageCode();
                    }

                    @Override
                    public String getTitleMessageCode() {
                        return ErrorResponse.super.getTitleMessageCode();
                    }

                    @Override
                    public String getDetailMessageCode() {
                        return ErrorResponse.super.getDetailMessageCode();
                    }

                    @Override
                    public Object @Nullable [] getDetailMessageArguments() {
                        return ErrorResponse.super.getDetailMessageArguments();
                    }

                    @Override
                    public Object @Nullable [] getDetailMessageArguments(MessageSource messageSource, Locale locale) {
                        return ErrorResponse.super.getDetailMessageArguments(messageSource, locale);
                    }

                    @Override
                    public ProblemDetail updateAndGetBody(@Nullable MessageSource messageSource, Locale locale) {
                        return ErrorResponse.super.updateAndGetBody(messageSource, locale);
                    }
                });
    }
}