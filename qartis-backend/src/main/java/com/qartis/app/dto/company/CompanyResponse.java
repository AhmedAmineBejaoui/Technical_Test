package com.qartis.app.dto.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de réponse contenant les informations d'une entreprise
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyResponse {

    private Long id;

    private String name;

    private String address;

    private String taxNumber;

    private String phone;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
