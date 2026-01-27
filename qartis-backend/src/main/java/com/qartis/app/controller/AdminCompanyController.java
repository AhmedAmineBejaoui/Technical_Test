package com.qartis.app.controller;

import com.qartis.app.dto.company.CompanyResponse;
import com.qartis.app.entity.Company;
import com.qartis.app.exception.ResourceNotFoundException;
import com.qartis.app.mapper.CompanyMapper;
import com.qartis.app.repository.CompanyRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller d'administration des entreprises
 * Accessible uniquement aux utilisateurs avec le rôle ADMIN
 */
@RestController
@RequestMapping("/admin/companies")
@RequiredArgsConstructor
@Tag(name = "Admin - Companies", description = "Gestion des entreprises (ADMIN uniquement)")
@SecurityRequirement(name = "bearerAuth")
public class AdminCompanyController {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    /**
     * Récupère la liste de toutes les entreprises
     */
    @GetMapping
    @Operation(summary = "Liste des entreprises", description = "Récupère toutes les entreprises")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès refusé (non ADMIN)")
    })
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        List<CompanyResponse> companies = companyRepository.findAll()
                .stream()
                .map(companyMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(companies);
    }

    /**
     * Récupère une entreprise par son ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Détail entreprise", description = "Récupère une entreprise par son ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Entreprise trouvée"),
            @ApiResponse(responseCode = "404", description = "Entreprise non trouvée")
    })
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", id));
        return ResponseEntity.ok(companyMapper.toResponse(company));
    }
}
