package com.abranlezama.fullstackchatapplication.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Objects;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String TRACE = "trace";
    @Value("${stacktrace.trace:false}")
    private boolean printStackTrace;


    @ExceptionHandler(UsernameTakenException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleUserEmailTakenException(UsernameTakenException ex, WebRequest request) {
        return buildErrorResponse(ex, ex.getMessage(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return buildErrorResponse(ex, ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status,
                                                                  WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(status.value(),
                "Validation error. Check 'errors' field for details.");

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            exceptionResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.unprocessableEntity().body(exceptionResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,
                                                                     WebRequest request) {
        return buildErrorResponse(ex, ex.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    /*
    all the handlers in the ResponseEntityExceptionHandler use this function to build the ResponseEntity
    similar to our buildErrorResponse(). If we don’t override this then the clients will receive only the
    HTTP status in the response header but since we want to include the HTTP status in our response bodies as well,
    we have overridden the method.
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        return buildErrorResponse(ex, ex.getMessage(), statusCode, request);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(RuntimeException ex, WebRequest request) {
        log.error("Unknown error occurred", ex);
        return buildErrorResponse(ex, "Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception ex, String message,
                                                      HttpStatusCode statusCode, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(statusCode.value(), message);

        if (printStackTrace && isTraceOn(request)) {
            exceptionResponse.setStackTrace(Arrays.toString(ex.getStackTrace()));
        }

        return ResponseEntity.status(statusCode).body(exceptionResponse);
    }

    private boolean isTraceOn(WebRequest request) {
        String[] value = request.getParameterValues(TRACE);
        return Objects.nonNull(value) && value.length > 0 && value[0].contentEquals("true");
    }
}

