package com.qartis.app.controller;

import com.qartis.app.dto.client.ClientProfileUpdateRequest;
import com.qartis.app.dto.company.CompanyRequest;
import com.qartis.app.dto.company.CompanyResponse;
import com.qartis.app.dto.user.UserResponse;
import com.qartis.app.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
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
 * Controller pour les opérations CLIENT
 * Gère le profil utilisateur et l'entreprise associée
 * Accessible uniquement aux utilisateurs avec le rôle CLIENT
 */
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Tag(name = "Client", description = "Gestion du profil client et de l'entreprise (CLIENT uniquement)")
@SecurityRequirement(name = "bearerAuth")
public class ClientController {

    private final ClientService clientService;

    /**
     * Récupère le profil de l'utilisateur connecté
     */
    @GetMapping("/me")
    @Operation(summary = "Mon profil", description = "Récupère le profil de l'utilisateur connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil récupéré"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<UserResponse> getMyProfile() {
        UserResponse response = clientService.getCurrentUserProfile();
        return ResponseEntity.ok(response);
    }

    /**
     * Met à jour le profil de l'utilisateur connecté
     */
    @PutMapping("/me")
    @Operation(summary = "Modifier mon profil", description = "Met à jour firstName et lastName de l'utilisateur connecté")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil mis à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<UserResponse> updateMyProfile(
            @Valid @RequestBody ClientProfileUpdateRequest request) {
        UserResponse response = clientService.updateProfile(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Récupère l'entreprise de l'utilisateur connecté
     */
    @GetMapping("/me/company")
    @Operation(summary = "Mon entreprise", description = "Récupère l'entreprise associée à l'utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entreprise récupérée"),
            @ApiResponse(responseCode = "404", description = "Aucune entreprise associée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<CompanyResponse> getMyCompany() {
        CompanyResponse response = clientService.getMyCompany();
        return ResponseEntity.ok(response);
    }

    /**
     * Crée une nouvelle entreprise pour l'utilisateur connecté
     */
    @PostMapping("/me/company")
    @Operation(summary = "Créer mon entreprise", description = "Crée une nouvelle entreprise et l'associe à l'utilisateur (si pas déjà existante)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Entreprise créée"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou entreprise déjà existante"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<CompanyResponse> createMyCompany(
            @Valid @RequestBody CompanyRequest request) {
        CompanyResponse response = clientService.createCompany(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Met à jour l'entreprise de l'utilisateur connecté
     */
    @PutMapping("/me/company")
    @Operation(summary = "Modifier mon entreprise", description = "Met à jour l'entreprise existante de l'utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entreprise mise à jour"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou pas d'entreprise associée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    public ResponseEntity<CompanyResponse> updateMyCompany(
            @Valid @RequestBody CompanyRequest request) {
        CompanyResponse response = clientService.updateCompany(request);
        return ResponseEntity.ok(response);
    }
}
