package com.qartis.app.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception levée lors d'une opération métier interdite
 */
public class BusinessException extends AppException {

    public BusinessException(String message) {
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.BUSINESS_ERROR);
    }

    public BusinessException(String message, String code) {
        super(message, HttpStatus.BAD_REQUEST, ErrorCode.BUSINESS_ERROR);
    }
}
