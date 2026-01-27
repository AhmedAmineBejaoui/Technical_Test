package com.qartis.app.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Structure standardisée pour toutes les réponses d'erreur
 * Format: timestamp, status, errorCode, message (bilingue), details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private int status;

    private String errorCode;

    private MessageBilingual message;

    private String path;

    private List<FieldError> details;

    /**
     * Détail d'erreur pour un champ de validation
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FieldError {

        private String field;

        private MessageBilingual message;

        private Object rejectedValue;

        /**
         * Helper pour créer une erreur de champ
         */
        public static FieldError of(String field, String messageEn, String messageAr, Object rejectedValue) {
            return FieldError.builder()
                    .field(field)
                    .message(MessageBilingual.of(messageEn, messageAr))
                    .rejectedValue(rejectedValue)
                    .build();
        }
    }
}
