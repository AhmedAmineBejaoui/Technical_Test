package com.qartis.app.service;

import com.qartis.app.dto.client.ClientProfileUpdateRequest;
import com.qartis.app.dto.company.CompanyRequest;
import com.qartis.app.dto.company.CompanyResponse;
import com.qartis.app.dto.user.UserResponse;
import com.qartis.app.entity.Company;
import com.qartis.app.entity.User;
import com.qartis.app.exception.BusinessException;
import com.qartis.app.exception.ResourceNotFoundException;
import com.qartis.app.mapper.CompanyMapper;
import com.qartis.app.mapper.UserMapper;
import com.qartis.app.repository.CompanyRepository;
import com.qartis.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service pour les opérations CLIENT
 * Gère le profil utilisateur et l'entreprise associée
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserMapper userMapper;
    private final CompanyMapper companyMapper;

    /**
     * Récupère le profil de l'utilisateur connecté
     */
    @Transactional(readOnly = true)
    public UserResponse getCurrentUserProfile() {
        User user = getCurrentUser();
        log.debug("Récupération du profil: {}", user.getEmail());
        return userMapper.toResponse(user);
    }

    /**
     * Met à jour le profil de l'utilisateur connecté (firstName, lastName
     * uniquement)
     */
    @Transactional
    public UserResponse updateProfile(ClientProfileUpdateRequest request) {
        User user = getCurrentUser();
        log.info("Mise à jour du profil: {}", user.getEmail());

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        user = userRepository.save(user);
        log.info("Profil mis à jour: {}", user.getEmail());

        return userMapper.toResponse(user);
    }

    /**
     * Crée une nouvelle entreprise et l'associe à l'utilisateur connecté
     * 
     * @throws BusinessException si l'utilisateur a déjà une entreprise
     */
    @Transactional
    public CompanyResponse createCompany(CompanyRequest request) {
        User user = getCurrentUser();
        log.info("Création d'entreprise pour: {}", user.getEmail());

        // Vérifier que l'utilisateur n'a pas déjà une entreprise
        if (user.getCompany() != null) {
            throw new BusinessException(
                    "Vous avez déjà une entreprise associée. Utilisez PUT pour la modifier.",
                    "COMPANY_ALREADY_EXISTS");
        }

        // Créer la nouvelle entreprise
        Company company = companyMapper.toEntity(request);
        company = companyRepository.save(company);

        // Associer l'entreprise à l'utilisateur
        user.setCompany(company);
        userRepository.save(user);

        log.info("Entreprise créée et associée: {} -> {}", user.getEmail(), company.getName());

        return companyMapper.toResponse(company);
    }

    /**
     * Met à jour l'entreprise de l'utilisateur connecté
     * 
     * @throws BusinessException si l'utilisateur n'a pas d'entreprise
     */
    @Transactional
    public CompanyResponse updateCompany(CompanyRequest request) {
        User user = getCurrentUser();
        log.info("Mise à jour de l'entreprise pour: {}", user.getEmail());

        // Vérifier que l'utilisateur a une entreprise
        if (user.getCompany() == null) {
            throw new BusinessException(
                    "Vous n'avez pas d'entreprise associée. Utilisez POST pour en créer une.",
                    "NO_COMPANY");
        }

        // Mettre à jour l'entreprise existante
        Company company = user.getCompany();
        companyMapper.updateEntityFromRequest(request, company);
        company = companyRepository.save(company);

        log.info("Entreprise mise à jour: {}", company.getName());

        return companyMapper.toResponse(company);
    }

    /**
     * Récupère l'entreprise de l'utilisateur connecté
     */
    @Transactional(readOnly = true)
    public CompanyResponse getMyCompany() {
        User user = getCurrentUser();

        if (user.getCompany() == null) {
            throw new ResourceNotFoundException("Aucune entreprise associée à votre compte");
        }

        return companyMapper.toResponse(user.getCompany());
    }

    /**
     * Helper: Récupère l'utilisateur connecté depuis le SecurityContext
     */
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("Utilisateur non authentifié", "NOT_AUTHENTICATED");
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", email));
    }
}
