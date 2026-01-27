package com.qartis.app.dto.user;

import com.qartis.app.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de réponse contenant les informations d'un utilisateur
 * Utilisé pour les listings et détails utilisateur
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private String companyName;

    private Long companyId;

    private Boolean isActive;

    /**
     * Nom complet de l'utilisateur
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
