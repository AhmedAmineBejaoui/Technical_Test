package com.qartis.app.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Gestionnaire global des exceptions avec réponses bilingues
 * Intercepte toutes les exceptions et retourne une réponse JSON standardisée
 * Format: timestamp, status, errorCode, message{en,ar}, details[{field,
 * message{en,ar}, rejectedValue}]
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Gère les exceptions métier personnalisées (AppException)
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(
            AppException ex, HttpServletRequest request) {

        log.error("AppException [{}]: {}", ex.getErrorCode().getCode(), ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .status(ex.getStatus().value())
                .errorCode(ex.getErrorCode().getCode())
                .message(MessageBilingual.from(ex.getErrorCode()))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, ex.getStatus());
    }

    /**
     * Gère les exceptions NotFoundException
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(
            NotFoundException ex, HttpServletRequest request) {

        log.warn("NotFoundException [{}]: {}",
                ex.getErrorCode().getCode(), ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .status(ex.getStatus().value())
                .errorCode(ex.getErrorCode().getCode())
                .message(MessageBilingual.from(ex.getErrorCode()))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, ex.getStatus());
    }

    /**
     * Gère les exceptions EmailAlreadyExistsException
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex, HttpServletRequest request) {

        log.warn("EmailAlreadyExistsException: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .status(ex.getStatus().value())
                .errorCode(ex.getErrorCode().getCode())
                .message(MessageBilingual.from(ex.getErrorCode()))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, ex.getStatus());
    }

    /**
     * Gère les exceptions BadRequestException
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException ex, HttpServletRequest request) {

        log.warn("BadRequestException [{}]: {}",
                ex.getErrorCode().getCode(), ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .status(ex.getStatus().value())
                .errorCode(ex.getErrorCode().getCode())
                .message(MessageBilingual.from(ex.getErrorCode()))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, ex.getStatus());
    }

    /**
     * Gère les erreurs de validation des DTOs (MethodArgumentNotValidException)
     * Retourne les détails de chaque champ en erreur
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.warn("Validation Failed: {}", ex.getBindingResult().getErrorCount());

        // Construire la liste des erreurs de champ
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ErrorResponse.FieldError.builder()
                        .field(error.getField())
                        .message(MessageBilingual.of(
                                error.getDefaultMessage(),
                                getArabicErrorMessage(error)))
                        .rejectedValue(error.getRejectedValue())
                        .build())
                .collect(Collectors.toList());

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .errorCode(ErrorCode.VALIDATION_FAILED.getCode())
                .message(MessageBilingual.from(ErrorCode.VALIDATION_FAILED))
                .path(request.getRequestURI())
                .details(fieldErrors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère les erreurs d'authentification Spring Security
     */
    @ExceptionHandler({
            org.springframework.security.authentication.BadCredentialsException.class,
            org.springframework.security.core.AuthenticationException.class
    })
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            Exception ex, HttpServletRequest request) {

        log.warn("Authentication Error: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .errorCode(ErrorCode.BAD_CREDENTIALS.getCode())
                .message(MessageBilingual.from(ErrorCode.BAD_CREDENTIALS))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Gère les erreurs d'accès refusé
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {

        log.warn("Access Denied: {}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .errorCode(ErrorCode.ACCESS_DENIED.getCode())
                .message(MessageBilingual.from(ErrorCode.ACCESS_DENIED))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Gère toutes les autres exceptions non prévues
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {

        log.error("Unexpected Exception: {}", ex.getMessage(), ex);

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .errorCode(ErrorCode.INTERNAL_SERVER_ERROR.getCode())
                .message(MessageBilingual.from(ErrorCode.INTERNAL_SERVER_ERROR))
                .path(request.getRequestURI())
                .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Helper: Obtient le message d'erreur en arabe pour un FieldError
     * En production, vous pouvez mapper les messages de validation vers l'arabe
     */
    private String getArabicErrorMessage(FieldError error) {
        // Pour l'instant, retourner une traduction simple
        String message = error.getDefaultMessage();

        return switch (message) {
            case "must not be blank" -> "لا يجب أن يكون فارغ";
            case "must not be null" -> "لا يجب أن يكون فارغ";
            case "must be between" -> "يجب أن يكون بين";
            case "invalid email format" -> "صيغة البريد الإلكتروني غير صحيحة";
            default -> "بيانات غير صحيحة";
        };
    }
}
