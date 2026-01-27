package com.qartis.app.service;

import com.qartis.app.dto.auth.AuthResponse;
import com.qartis.app.dto.auth.LoginRequest;
import com.qartis.app.dto.auth.SignupRequest;
import com.qartis.app.entity.Role;
import com.qartis.app.entity.User;
import com.qartis.app.exception.BadCredentialsException;
import com.qartis.app.exception.ResourceAlreadyExistsException;
import com.qartis.app.repository.UserRepository;
import com.qartis.app.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service d'authentification
 * Gère l'inscription et la connexion des utilisateurs
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    /**
     * Inscrit un nouvel utilisateur avec le rôle CLIENT
     * Le password est hashé avec BCrypt, company = null par défaut
     * 
     * @param request Données d'inscription
     * @return AuthResponse avec le token JWT
     */
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        log.info("Tentative d'inscription pour: {}", request.getEmail());

        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Utilisateur", "email", request.getEmail());
        }

        // Créer l'utilisateur avec rôle CLIENT forcé
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CLIENT) // Rôle forcé à CLIENT
                .company(null) // Pas de company à l'inscription
                .isActive(true)
                .build();

        user = userRepository.save(user);
        log.info("Utilisateur créé avec succès: {} (ID: {})", user.getEmail(), user.getId());

        // Générer le token JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails, user.getId(), user.getRole());

        return AuthResponse.builder()
                .token(token)
                .role(user.getRole())
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    /**
     * Authentifie un utilisateur et retourne un token JWT
     * 
     * @param request Credentials de connexion
     * @return AuthResponse avec le token JWT
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        log.info("Tentative de connexion pour: {}", request.getEmail());

        try {
            // Authentifier via Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        } catch (AuthenticationException e) {
            log.warn("Échec d'authentification pour: {}", request.getEmail());
            throw new BadCredentialsException();
        }

        // Récupérer l'utilisateur
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(BadCredentialsException::new);

        // Vérifier si le compte est actif
        if (!user.getIsActive()) {
            log.warn("Compte désactivé: {}", request.getEmail());
            throw new BadCredentialsException("Ce compte a été désactivé");
        }

        // Générer le token JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails, user.getId(), user.getRole());

        log.info("Connexion réussie pour: {} (Role: {})", user.getEmail(), user.getRole());

        return AuthResponse.builder()
                .token(token)
                .role(user.getRole())
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}
