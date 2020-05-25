package com.kernel.auditoriums.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class ApiError {

    private int status;
    private String error;
    private String message;

    public ApiError(HttpStatus status) {
        this.status = status.value();
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this(status);
        this.message = "Unexpected error";
        this.error = status.getReasonPhrase();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this(status, ex);
        this.message = message;
    }
}
