package com.platform.onboarding.application.ports.out;

import com.platform.onboarding.domain.OnboardingRequest;

import java.util.Optional;
import java.util.UUID;

/**
 * Output Port (Repository Interface) for OnboardingRequest persistence.
 * Defines the contract for infrastructure adapters following Hexagonal Architecture.
 */
public interface OnboardingRepository {
    
    /**
     * Saves an onboarding request.
     *
     * @param request The onboarding request to save
     * @return The saved onboarding request
     */
    OnboardingRequest save(OnboardingRequest request);
    
    /**
     * Finds an onboarding request by its unique identifier.
     *
     * @param id The unique identifier
     * @return Optional containing the onboarding request if found
     */
    Optional<OnboardingRequest> findById(UUID id);
    
    /**
     * Finds an onboarding request by tenant identifier.
     *
     * @param tenantId The tenant identifier
     * @return Optional containing the onboarding request if found
     */
    Optional<OnboardingRequest> findByTenantId(String tenantId);
}
