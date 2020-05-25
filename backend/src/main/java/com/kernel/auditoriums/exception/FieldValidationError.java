package com.kernel.auditoriums.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FieldValidationError {

    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    public FieldValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
