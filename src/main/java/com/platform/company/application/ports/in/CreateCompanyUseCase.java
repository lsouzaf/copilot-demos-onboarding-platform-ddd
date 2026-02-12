package com.platform.company.application.ports.in;

import java.util.UUID;

/**
 * Input Port (Use Case Interface) for creating a company.
 * Defines the contract for the application layer following Hexagonal Architecture.
 */
public interface CreateCompanyUseCase {

    /**
     * Command to create a company.
     *
     * @param name The name of the company (must not be null or blank, max 100 chars)
     */
    record CreateCompanyCommand(String name) {
        public CreateCompanyCommand {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Company name cannot be null or empty");
            }
            if (name.length() > 100) {
                throw new IllegalArgumentException("Company name cannot exceed 100 characters");
            }
        }
    }

    /**
     * Response after creating a company.
     *
     * @param id The unique identifier of the company
     * @param name The name of the company
     * @param status The status of the company
     */
    record CompanyResponse(UUID id, String name, String status) {}

    /**
     * Creates a new company.
     *
     * @param command The command containing company information
     * @return CompanyResponse with the created company details
     * @throws IllegalArgumentException if command validation fails
     */
    CompanyResponse create(CreateCompanyCommand command);
}
