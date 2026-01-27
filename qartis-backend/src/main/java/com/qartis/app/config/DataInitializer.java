package com.qartis.app.config;

import com.qartis.app.entity.Company;
import com.qartis.app.entity.Role;
import com.qartis.app.entity.User;
import com.qartis.app.repository.CompanyRepository;
import com.qartis.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Initialiseur de données au démarrage de l'application
 * Crée un admin par défaut si aucun ADMIN n'existe
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        // Créer l'admin par défaut si aucun ADMIN n'existe
        if (!userRepository.existsByRole(Role.ADMIN)) {
            User admin = User.builder()
                    .firstName("Admin")
                    .lastName("System")
                    .email("admin@demo.com")
                    .password(passwordEncoder.encode("Admin@12345"))
                    .role(Role.ADMIN)
                    .isActive(true)
                    .build();

            userRepository.save(admin);
            log.info("✅ Admin créé: admin@demo.com / Admin@12345");
        }

        log.info("═══════════════════════════════════════════");
        log.info("  Compte admin par défaut disponible       ");
        log.info("═══════════════════════════════════════════");
        log.info("  Email:    admin@demo.com                  ");
        log.info("  Password: Admin@12345                     ");
        log.info("═══════════════════════════════════════════");
    }
}
