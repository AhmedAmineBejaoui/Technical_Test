package com.qartis.app.specification;

import com.qartis.app.entity.Role;
import com.qartis.app.entity.User;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * Specifications pour le filtrage dynamique des utilisateurs
 * Utilise Spring Data JPA Specifications (Criteria API)
 */
public class UserSpecification {

    private UserSpecification() {
        // Utility class, pas d'instanciation
    }

    /**
     * Filtre par email (contient, insensible à la casse)
     */
    public static Specification<User> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(email)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")),
                    "%" + email.toLowerCase() + "%");
        };
    }

    /**
     * Filtre par rôle exact
     */
    public static Specification<User> hasRole(Role role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("role"), role);
        };
    }

    /**
     * Filtre par nom (firstName ou lastName contient)
     */
    public static Specification<User> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(name)) {
                return criteriaBuilder.conjunction();
            }
            String pattern = "%" + name.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), pattern));
        };
    }

    /**
     * Filtre par nom d'entreprise (contient, insensible à la casse)
     */
    public static Specification<User> hasCompanyName(String companyName) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(companyName)) {
                return criteriaBuilder.conjunction();
            }
            // Join avec Company pour filtrer
            var companyJoin = root.join("company", JoinType.LEFT);
            return criteriaBuilder.like(
                    criteriaBuilder.lower(companyJoin.get("name")),
                    "%" + companyName.toLowerCase() + "%");
        };
    }

    /**
     * Filtre par ID d'entreprise
     */
    public static Specification<User> hasCompanyId(Long companyId) {
        return (root, query, criteriaBuilder) -> {
            if (companyId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("company").get("id"), companyId);
        };
    }

    /**
     * Filtre par statut actif
     */
    public static Specification<User> isActive(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("isActive"), active);
        };
    }

    /**
     * Assure le fetch de la company pour éviter le problème N+1
     * À utiliser avec précaution (une seule fois par query)
     */
    public static Specification<User> fetchCompany() {
        return (root, query, criteriaBuilder) -> {
            // Éviter le fetch pour les count queries
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("company", JoinType.LEFT);
            }
            return criteriaBuilder.conjunction();
        };
    }

    /**
     * Combine plusieurs specifications pour un filtrage complet
     */
    public static Specification<User> buildSpecification(
            String email,
            Role role,
            String name,
            String companyName,
            Boolean active) {
        return Specification
                .where(fetchCompany())
                .and(hasEmail(email))
                .and(hasRole(role))
                .and(hasName(name))
                .and(hasCompanyName(companyName))
                .and(isActive(active));
    }
}
