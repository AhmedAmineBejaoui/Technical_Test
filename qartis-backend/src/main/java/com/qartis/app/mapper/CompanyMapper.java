package com.qartis.app.mapper;

import com.qartis.app.dto.company.CompanyRequest;
import com.qartis.app.dto.company.CompanyResponse;
import com.qartis.app.entity.Company;
import org.mapstruct.*;

/**
 * Mapper MapStruct pour l'entité Company
 * Gère les conversions entre Company et ses DTOs
 */
@Mapper(componentModel = "spring")
public interface CompanyMapper {

    /**
     * Convertit une entité Company en CompanyResponse
     * 
     * @param company L'entité Company
     * @return CompanyResponse DTO
     */
    CompanyResponse toResponse(Company company);

    /**
     * Convertit un CompanyRequest en entité Company
     * 
     * @param request Le DTO CompanyRequest
     * @return L'entité Company
     */
    Company toEntity(CompanyRequest request);

    /**
     * Met à jour une entité Company existante à partir d'un CompanyRequest
     * 
     * @param request Le DTO avec les nouvelles données
     * @param company L'entité existante à mettre à jour
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CompanyRequest request, @MappingTarget Company company);
}
