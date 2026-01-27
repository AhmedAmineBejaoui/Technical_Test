package com.qartis.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception personnalisée quand un email est déjà utilisé (409)
 */
@Getter
public class EmailAlreadyExistsException extends AppException {

    public EmailAlreadyExistsException(String email) {
        super(
                String.format("Email address '%s' is already registered", email),
                HttpStatus.CONFLICT,
                ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    public EmailAlreadyExistsException(String email, String messageEn) {
        super(messageEn, HttpStatus.CONFLICT, ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}
