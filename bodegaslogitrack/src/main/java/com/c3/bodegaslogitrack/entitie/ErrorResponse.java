package com.c3.bodegaslogitrack.entitie;

import java.security.Timestamp;

public class ErrorResponse {
    private boolean success;
    private String error;
    private String message;
    private int code;

    public ErrorResponse(String error, String message, int code) {
        this.success = false;
        this.error = error;
        this.message = message;
        this.code = code;
    }

    // Getters y Setters
    public boolean isSuccess() { return success; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public int getCode() { return code; }
}
