package com.platform.onboarding.application.ports.in;

import java.util.UUID;

/**
 * Input Port (Use Case Interface) for initiating onboarding process.
 * Defines the contract for the application layer following Hexagonal Architecture.
 */
public interface InitiateOnboardingUseCase {
    
    /**
     * Command to initiate onboarding process.
     *
     * @param companyName The name of the company
     * @param adminName The name of the admin user
     * @param adminEmail The email of the admin user
     */
    record InitiateOnboardingCommand(
        String companyName,
        String adminName,
        String adminEmail
    ) {
        public InitiateOnboardingCommand {
            if (companyName == null || companyName.isBlank()) {
                throw new IllegalArgumentException("Company name cannot be null or empty");
            }
            if (adminName == null || adminName.isBlank()) {
                throw new IllegalArgumentException("Admin name cannot be null or empty");
            }
            if (adminEmail == null || adminEmail.isBlank()) {
                throw new IllegalArgumentException("Admin email cannot be null or empty");
            }
        }
    }
    
    /**
     * Response after initiating onboarding.
     *
     * @param onboardingId The unique identifier of the onboarding request
     * @param tenantId The tenant identifier assigned to this onboarding
     * @param status The current status of the onboarding
     */
    record OnboardingResponse(
        UUID onboardingId,
        String tenantId,
        String status
    ) {}
    
    /**
     * Initiates the onboarding process for a new company.
     *
     * @param command The command containing company and admin information
     * @return OnboardingResponse with the created onboarding details
     * @throws IllegalArgumentException if command validation fails
     */
    OnboardingResponse initiate(InitiateOnboardingCommand command);
}
