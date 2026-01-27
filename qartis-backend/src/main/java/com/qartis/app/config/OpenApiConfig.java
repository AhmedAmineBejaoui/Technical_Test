package com.qartis.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration OpenAPI/Swagger pour la documentation API
 */
@Configuration
@OpenAPIDefinition(info = @Info(title = "Qartis API", version = "1.0.0", description = "API REST pour l'application Qartis avec authentification JWT et RBAC (Admin/Client)", contact = @Contact(name = "Qartis Support", email = "support@qartis.com"), license = @License(name = "Propriétaire", url = "https://qartis.com")), servers = {
        @Server(url = "http://localhost:8080/api", description = "Serveur de développement")
})
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", description = "Entrez le token JWT (sans 'Bearer ' devant)")
public class OpenApiConfig {
}
