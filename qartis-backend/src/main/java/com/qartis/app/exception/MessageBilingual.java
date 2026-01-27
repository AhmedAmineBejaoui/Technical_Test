package com.qartis.app.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Conteneur pour les messages bilingues (EN/AR)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageBilingual {

    private String en;
    private String ar;

    /**
     * Factory pour créer un message bilingue depuis un ErrorCode
     */
    public static MessageBilingual from(ErrorCode errorCode) {
        return MessageBilingual.builder()
                .en(errorCode.getMessageEn())
                .ar(errorCode.getMessageAr())
                .build();
    }

    /**
     * Factory pour créer un message personnalisé bilingue
     */
    public static MessageBilingual of(String messageEn, String messageAr) {
        return MessageBilingual.builder()
                .en(messageEn)
                .ar(messageAr)
                .build();
    }
}
