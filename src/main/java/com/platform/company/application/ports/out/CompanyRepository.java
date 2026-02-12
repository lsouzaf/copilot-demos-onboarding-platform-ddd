package com.platform.company.application.ports.out;

import com.platform.company.domain.Company;

import java.util.Optional;
import java.util.UUID;

/**
 * Output Port (Repository Interface) for Company persistence.
 * Defines the contract for infrastructure adapters following Hexagonal Architecture.
 */
public interface CompanyRepository {

    /**
     * Saves a company.
     *
     * @param company The company to save
     * @return The saved company
     */
    Company save(Company company);

    /**
     * Finds a company by its unique identifier.
     *
     * @param id The unique identifier
     * @return Optional containing the company if found
     */
    Optional<Company> findById(UUID id);

    /**
     * Finds a company by its name.
     *
     * @param name The company name
     * @return Optional containing the company if found
     */
    Optional<Company> findByName(String name);
}
