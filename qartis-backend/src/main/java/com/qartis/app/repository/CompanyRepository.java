package com.qartis.app.repository;

import com.qartis.app.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Company
 * Gère l'accès aux données des entreprises
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    /**
     * Recherche une entreprise par nom (insensible à la casse)
     * 
     * @param name Le nom de l'entreprise
     * @return Optional contenant l'entreprise si trouvée
     */
    Optional<Company> findByNameIgnoreCase(String name);

    /**
     * Vérifie si une entreprise avec ce nom existe
     * 
     * @param name Le nom à vérifier
     * @return true si une entreprise avec ce nom existe
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Recherche une entreprise par numéro fiscal
     * 
     * @param taxNumber Le numéro fiscal
     * @return Optional contenant l'entreprise si trouvée
     */
    Optional<Company> findByTaxNumber(String taxNumber);

    /**
     * Vérifie si un numéro fiscal existe déjà
     * 
     * @param taxNumber Le numéro fiscal à vérifier
     * @return true si le numéro fiscal existe
     */
    boolean existsByTaxNumber(String taxNumber);

    /**
     * Recherche les entreprises par nom partiel (like)
     * 
     * @param name Fragment du nom à rechercher
     * @return Liste des entreprises correspondantes
     */
    @Query("SELECT c FROM Company c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Company> searchByName(@Param("name") String name);

    /**
     * Recherche les entreprises créées après une certaine date
     * 
     * @param date La date de référence
     * @return Liste des entreprises créées après cette date
     */
    @Query("SELECT c FROM Company c WHERE c.createdAt > :date ORDER BY c.createdAt DESC")
    List<Company> findRecentCompanies(@Param("date") java.time.LocalDateTime date);

    /**
     * Compte le nombre d'entreprises
     * 
     * @return Nombre total d'entreprises
     */
    @Override
    long count();
}
