package com.qartis.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception personnalisée pour les erreurs de requête (400)
 */
@Getter
public class BadRequestException extends AppException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.INVALID_REQUEST);
    }

    public BadRequestException(ErrorCode errorCode, String messageEn) {
        super(messageEn, HttpStatus.BAD_REQUEST, errorCode);
    }

    public BadRequestException(String messageEn, ErrorCode errorCode) {
        super(messageEn, HttpStatus.BAD_REQUEST, errorCode);
    }
}
