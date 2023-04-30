package com.abranlezama.fullstackchatapplication.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    private int status;
    private String message;
    private String stackTrace;
    private List<ValidationError> errors;

    public ExceptionResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    private record ValidationError(String field, String message) {}

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) this.errors = new ArrayList<>();
        this.errors.add(new ValidationError(field, message));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExceptionResponse that = (ExceptionResponse) o;
        return status == that.status && Objects.equals(message, that.message) &&
                Objects.equals(stackTrace, that.stackTrace) && Objects.equals(errors, that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, message, stackTrace, errors);
    }
}
