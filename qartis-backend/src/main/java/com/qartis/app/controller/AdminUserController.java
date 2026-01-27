package com.qartis.app.controller;

import com.qartis.app.dto.common.PageResponse;
import com.qartis.app.dto.user.UserCreateRequest;
import com.qartis.app.dto.user.UserResponse;
import com.qartis.app.dto.user.UserUpdateRequest;
import com.qartis.app.entity.Role;
import com.qartis.app.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller d'administration des utilisateurs
 * Accessible uniquement aux utilisateurs avec le rôle ADMIN
 */
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin - Users", description = "Gestion des utilisateurs (ADMIN uniquement)")
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * Récupère la liste paginée des utilisateurs avec filtres optionnels
     */
    @GetMapping
    @Operation(summary = "Liste des utilisateurs", description = "Récupère tous les utilisateurs avec pagination et filtrage")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès refusé (non ADMIN)")
    })
    public ResponseEntity<PageResponse<UserResponse>> getAllUsers(
            @Parameter(description = "Numéro de page (commence à 0)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Taille de la page") @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Filtre par email (contient)") @RequestParam(required = false) String email,

            @Parameter(description = "Filtre par rôle (ADMIN ou CLIENT)") @RequestParam(required = false) Role role,

            @Parameter(description = "Filtre par nom (firstName ou lastName)") @RequestParam(required = false) String name,

            @Parameter(description = "Filtre par nom d'entreprise") @RequestParam(required = false) String company,

            @Parameter(description = "Filtre par statut actif") @RequestParam(required = false) Boolean active,

            @Parameter(description = "Champ de tri") @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Direction du tri (asc ou desc)") @RequestParam(defaultValue = "asc") String sortDirection) {
        PageResponse<UserResponse> response = adminUserService.getAllUsers(
                page, size, email, role, name, company, active, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupère un utilisateur par son ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Détail utilisateur", description = "Récupère un utilisateur par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse response = adminUserService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Crée un nouvel utilisateur
     */
    @PostMapping
    @Operation(summary = "Créer un utilisateur", description = "Crée un nouvel utilisateur (Admin peut définir le rôle)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Utilisateur créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "409", description = "Email déjà utilisé")
    })
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        UserResponse response = adminUserService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Met à jour un utilisateur existant
     */
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un utilisateur", description = "Met à jour les informations d'un utilisateur (sauf password)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé"),
            @ApiResponse(responseCode = "409", description = "Email déjà utilisé")
    })
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = adminUserService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Supprime un utilisateur
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un utilisateur", description = "Supprime définitivement un utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Utilisateur supprimé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Active/Désactive un utilisateur
     */
    @PatchMapping("/{id}/toggle-status")
    @Operation(summary = "Activer/Désactiver", description = "Change le statut actif d'un utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Statut modifié"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    public ResponseEntity<UserResponse> toggleUserStatus(@PathVariable Long id) {
        UserResponse response = adminUserService.toggleUserStatus(id);
        return ResponseEntity.ok(response);
    }
}
