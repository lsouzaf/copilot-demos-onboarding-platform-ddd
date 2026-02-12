package com.platform.company.adapters.out.persistence;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * JPA Entity for Company persistence.
 * Maps to the companies table in the database.
 */
@Entity
@Table(name = "companies", schema = "public")
public class CompanyEntity {

    @Id
    private UUID id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private String status;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public CompanyEntity() {
    }

    public CompanyEntity(UUID id, String name, String status, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
