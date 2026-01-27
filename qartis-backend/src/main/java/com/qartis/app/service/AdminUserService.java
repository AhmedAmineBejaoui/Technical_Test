package com.qartis.app.service;

import com.qartis.app.dto.common.PageResponse;
import com.qartis.app.dto.user.UserCreateRequest;
import com.qartis.app.dto.user.UserResponse;
import com.qartis.app.dto.user.UserUpdateRequest;
import com.qartis.app.entity.Company;
import com.qartis.app.entity.Role;
import com.qartis.app.entity.User;
import com.qartis.app.exception.ResourceAlreadyExistsException;
import com.qartis.app.exception.ResourceNotFoundException;
import com.qartis.app.mapper.UserMapper;
import com.qartis.app.repository.CompanyRepository;
import com.qartis.app.repository.UserRepository;
import com.qartis.app.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service de gestion des utilisateurs (côté Admin)
 * Fournit les opérations CRUD avec filtrage et pagination
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Récupère un utilisateur par son ID
     */
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = findUserById(id);
        return userMapper.toResponse(user);
    }

    /**
     * Récupère tous les utilisateurs avec pagination et filtrage
     */
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> getAllUsers(
            int page,
            int size,
            String email,
            Role role,
            String name,
            String companyName,
            Boolean active,
            String sortBy,
            String sortDirection) {
        // Configuration du tri
        Sort sort = Sort.by(
                sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                sortBy != null ? sortBy : "id");

        Pageable pageable = PageRequest.of(page, size, sort);

        // Construction de la spécification de filtrage
        Specification<User> spec = UserSpecification.buildSpecification(
                email, role, name, companyName, active);

        Page<User> userPage = userRepository.findAll(spec, pageable);

        // Conversion en DTOs
        List<UserResponse> content = userPage.getContent()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());

        return PageResponse.<UserResponse>builder()
                .content(content)
                .page(userPage.getNumber())
                .size(userPage.getSize())
                .totalElements(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .first(userPage.isFirst())
                .last(userPage.isLast())
                .build();
    }

    /**
     * Crée un nouvel utilisateur (Admin peut définir le rôle)
     */
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        log.info("Création d'un utilisateur par admin: {}", request.getEmail());

        // Vérifier unicité de l'email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Utilisateur", "email", request.getEmail());
        }

        // Mapper le DTO vers l'entité
        User user = userMapper.toEntity(request);

        // Hasher le password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Associer la company si fournie
        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", request.getCompanyId()));
            user.setCompany(company);
        }

        user = userRepository.save(user);
        log.info("Utilisateur créé: {} (ID: {}, Role: {})", user.getEmail(), user.getId(), user.getRole());

        return userMapper.toResponse(user);
    }

    /**
     * Met à jour un utilisateur existant
     * Note: Le password n'est pas modifiable via cet endpoint
     */
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        log.info("Mise à jour de l'utilisateur ID: {}", id);

        User user = findUserById(id);

        // Vérifier unicité de l'email si changé
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Utilisateur", "email", request.getEmail());
        }

        // Mettre à jour les champs via le mapper
        userMapper.updateEntityFromRequest(request, user);

        // Mettre à jour la company si le companyId a changé
        if (request.getCompanyId() != null) {
            if (user.getCompany() == null || !user.getCompany().getId().equals(request.getCompanyId())) {
                Company company = companyRepository.findById(request.getCompanyId())
                        .orElseThrow(() -> new ResourceNotFoundException("Company", "id", request.getCompanyId()));
                user.setCompany(company);
            }
        } else {
            // Si companyId est null dans la requête, on peut dissocier la company
            user.setCompany(null);
        }

        user = userRepository.save(user);
        log.info("Utilisateur mis à jour: {} (ID: {})", user.getEmail(), user.getId());

        return userMapper.toResponse(user);
    }

    /**
     * Supprime un utilisateur
     */
    @Transactional
    public void deleteUser(Long id) {
        log.info("Suppression de l'utilisateur ID: {}", id);

        User user = findUserById(id);
        userRepository.delete(user);

        log.info("Utilisateur supprimé: {} (ID: {})", user.getEmail(), id);
    }

    /**
     * Change le statut actif/inactif d'un utilisateur
     */
    @Transactional
    public UserResponse toggleUserStatus(Long id) {
        User user = findUserById(id);
        user.setIsActive(!user.getIsActive());
        user = userRepository.save(user);

        log.info("Statut utilisateur changé: {} -> {}", user.getEmail(), user.getIsActive());

        return userMapper.toResponse(user);
    }

    /**
     * Helper: Trouve un utilisateur par ID ou lève une exception
     */
    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
    }
}
