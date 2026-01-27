package com.qartis.app.dto.auth;

import com.qartis.app.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de réponse après authentification réussie
 * Contient le token JWT et les informations utilisateur de base
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;

    private Role role;

    private Long userId;

    private String email;

    private String firstName;

    private String lastName;

    /**
     * Type de token (toujours "Bearer")
     */
    @Builder.Default
    private String tokenType = "Bearer";
}
