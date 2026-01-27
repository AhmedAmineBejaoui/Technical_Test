package com.qartis.app.repository;

import com.qartis.app.entity.Role;
import com.qartis.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité User
 * Gère l'accès aux données des utilisateurs
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Recherche un utilisateur par email
     * 
     * @param email L'email de l'utilisateur
     * @return Optional contenant l'utilisateur si trouvé
     */
    Optional<User> findByEmail(String email);

    /**
     * Vérifie si un email existe déjà en base
     * 
     * @param email L'email à vérifier
     * @return true si l'email existe, false sinon
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un utilisateur avec un rôle spécifique existe
     * 
     * @param role Le rôle à vérifier
     * @return true si au moins un utilisateur avec ce rôle existe
     */
    boolean existsByRole(Role role);

    /**
     * Recherche tous les utilisateurs par rôle
     * 
     * @param role Le rôle à rechercher
     * @return Liste des utilisateurs ayant ce rôle
     */
    List<User> findByRole(Role role);

    /**
     * Recherche les utilisateurs actifs
     * 
     * @param isActive Statut d'activation
     * @return Liste des utilisateurs actifs/inactifs
     */
    List<User> findByIsActive(Boolean isActive);

    /**
     * Recherche un utilisateur par email et vérifie qu'il est actif
     * 
     * @param email    L'email de l'utilisateur
     * @param isActive Statut d'activation
     * @return Optional contenant l'utilisateur si trouvé et actif
     */
    Optional<User> findByEmailAndIsActive(String email, Boolean isActive);

    /**
     * Compte le nombre d'utilisateurs par rôle
     * 
     * @param role Le rôle à compter
     * @return Nombre d'utilisateurs ayant ce rôle
     */
    long countByRole(Role role);

    /**
     * Recherche les utilisateurs d'une entreprise
     * 
     * @param companyId L'ID de l'entreprise
     * @return Liste des utilisateurs de cette entreprise
     */
    @Query("SELECT u FROM User u WHERE u.company.id = :companyId")
    List<User> findByCompanyId(@Param("companyId") Long companyId);

    /**
     * Recherche les utilisateurs sans entreprise
     * 
     * @return Liste des utilisateurs sans entreprise associée
     */
    @Query("SELECT u FROM User u WHERE u.company IS NULL")
    List<User> findUsersWithoutCompany();
}
