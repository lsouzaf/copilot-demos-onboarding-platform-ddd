package com.platform.company.domain;

import com.platform.shared.domain.AggregateRoot;

import java.time.Instant;
import java.util.UUID;

/**
 * Company Aggregate Root - manages the company lifecycle.
 * Enforces business rules and emits domain events for state changes.
 */
public class Company extends AggregateRoot<UUID> {

    private final String name;
    private CompanyStatus status;
    private final Instant createdAt;

    private Company(UUID id, String name, CompanyStatus status, Instant createdAt) {
        super(id);
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Company create(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }

        if (name.length() > 100) {
            throw new IllegalArgumentException("Company name cannot exceed 100 characters");
        }

        UUID id = UUID.randomUUID();
        Instant now = Instant.now();

        return new Company(id, name.trim(), CompanyStatus.ACTIVE, now);
    }

    public static Company reconstitute(UUID id, String name, CompanyStatus status, Instant createdAt) {
        return new Company(id, name, status, createdAt);
    }

    public void activate() {
        if (this.status == CompanyStatus.ACTIVE) {
            throw new IllegalStateException("Company is already active");
        }

        this.status = CompanyStatus.ACTIVE;
    }

    public void suspend() {
        if (this.status == CompanyStatus.SUSPENDED) {
            throw new IllegalStateException("Company is already suspended");
        }

        if (this.status == CompanyStatus.INACTIVE) {
            throw new IllegalStateException("Cannot suspend an inactive company");
        }

        this.status = CompanyStatus.SUSPENDED;
    }

    public void deactivate() {
        if (this.status == CompanyStatus.INACTIVE) {
            throw new IllegalStateException("Company is already inactive");
        }

        this.status = CompanyStatus.INACTIVE;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CompanyStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
