package com.qartis.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Exception personnalisée pour les ressources non trouvées (404)
 */
@Getter
public class NotFoundException extends AppException {

    public NotFoundException(String resourceName, String fieldName, Object value) {
        super(
                String.format("%s not found with %s: %s", resourceName, fieldName, value),
                HttpStatus.NOT_FOUND,
                ErrorCode.RESOURCE_NOT_FOUND);
    }

    public NotFoundException(ErrorCode errorCode, String messageEn, String messageAr) {
        super(messageEn, HttpStatus.NOT_FOUND, errorCode);
    }

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND);
    }
}
