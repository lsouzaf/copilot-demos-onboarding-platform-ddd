package com.platform.company.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository interface for CompanyEntity.
 * Provides standard CRUD operations and custom queries.
 */
public interface JpaCompanyRepository extends JpaRepository<CompanyEntity, UUID> {

    /**
     * Finds a company entity by name.
     *
     * @param name The company name
     * @return Optional containing the entity if found
     */
    Optional<CompanyEntity> findByName(String name);
}
