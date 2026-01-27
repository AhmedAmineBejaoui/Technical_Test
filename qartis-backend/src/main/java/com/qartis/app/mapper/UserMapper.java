package com.qartis.app.mapper;

import com.qartis.app.dto.user.UserCreateRequest;
import com.qartis.app.dto.user.UserResponse;
import com.qartis.app.dto.user.UserUpdateRequest;
import com.qartis.app.entity.User;
import org.mapstruct.*;

/**
 * Mapper MapStruct pour l'entité User
 * Gère les conversions entre User et ses DTOs
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Convertit une entité User en UserResponse
     * Mappe companyName et companyId depuis l'objet Company associé
     * 
     * @param user L'entité User
     * @return UserResponse DTO
     */
    @Mapping(source = "company.name", target = "companyName")
    @Mapping(source = "company.id", target = "companyId")
    UserResponse toResponse(User user);

    /**
     * Convertit un UserCreateRequest en entité User
     * Le password est ignoré car il sera hashé séparément dans le service
     * 
     * @param request Le DTO UserCreateRequest
     * @return L'entité User (sans password hashé)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // Géré séparément (hash BCrypt)
    @Mapping(target = "company", ignore = true) // Géré séparément via companyId
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserCreateRequest request);

    /**
     * Met à jour une entité User existante à partir d'un UserUpdateRequest
     * Le password n'est jamais mis à jour via cette méthode (endpoint séparé)
     * Les valeurs null sont ignorées pour ne pas écraser les données existantes
     * 
     * @param request Le DTO avec les nouvelles données
     * @param user    L'entité existante à mettre à jour
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true) // Jamais mis à jour ici
    @Mapping(target = "company", ignore = true) // Géré séparément via companyId
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UserUpdateRequest request, @MappingTarget User user);
}
