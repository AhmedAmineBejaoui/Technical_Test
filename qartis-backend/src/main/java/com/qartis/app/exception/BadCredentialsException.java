package com.qartis.app.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception levée lors d'un échec d'authentification
 */
public class BadCredentialsException extends AppException {

    public BadCredentialsException() {
        super(
                "Email ou mot de passe incorrect",
                HttpStatus.UNAUTHORIZED,
                ErrorCode.BAD_CREDENTIALS);
    }

    public BadCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, ErrorCode.BAD_CREDENTIALS);
    }
}
