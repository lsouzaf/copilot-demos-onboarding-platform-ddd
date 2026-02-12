package com.platform.onboarding.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository interface for OnboardingEntity.
 * Provides standard CRUD operations and custom queries.
 */
public interface JpaOnboardingRepository extends JpaRepository<OnboardingEntity, UUID> {

    /**
     * Finds an onboarding entity by tenant ID.
     *
     * @param tenantId The tenant identifier
     * @return Optional containing the entity if found
     */
    Optional<OnboardingEntity> findByTenantId(String tenantId);
}
