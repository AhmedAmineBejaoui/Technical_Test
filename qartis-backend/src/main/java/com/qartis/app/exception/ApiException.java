package com.qartis.app.exception;

/**
 * Alias pour retrocompatibilité
 * Utiliser AppException à la place
 */
public class ApiException extends AppException {
    public ApiException(String message) {
        super(message, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
