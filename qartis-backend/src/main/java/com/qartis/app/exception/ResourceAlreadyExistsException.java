package com.qartis.app.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception levée quand une ressource existe déjà (email, etc.)
 */
public class ResourceAlreadyExistsException extends AppException {

    public ResourceAlreadyExistsException(String resource, String field, Object value) {
        super(
                String.format("%s existe déjà avec %s: %s", resource, field, value),
                HttpStatus.CONFLICT,
                ErrorCode.RESOURCE_ALREADY_EXISTS);
    }

    public ResourceAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT, ErrorCode.RESOURCE_ALREADY_EXISTS);
    }
}
