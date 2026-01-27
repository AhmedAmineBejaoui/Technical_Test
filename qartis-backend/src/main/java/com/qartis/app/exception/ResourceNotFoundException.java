package com.qartis.app.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception levée quand une ressource n'est pas trouvée
 */
public class ResourceNotFoundException extends AppException {

    public ResourceNotFoundException(String resource, String field, Object value) {
        super(
                String.format("%s non trouvé avec %s: %s", resource, field, value),
                HttpStatus.NOT_FOUND,
                ErrorCode.RESOURCE_NOT_FOUND);
    }

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND);
    }
}
