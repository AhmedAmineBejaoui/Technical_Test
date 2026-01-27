package com.qartis.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception de base pour toutes les exceptions métier de l'application
 * Utilise ErrorCode pour un système multilingue
 */
@Getter
public class AppException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCode errorCode;

    public AppException(String message, HttpStatus status, ErrorCode errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public AppException(String message, HttpStatus status) {
        this(message, status, ErrorCode.INTERNAL_SERVER_ERROR);
    }
}

