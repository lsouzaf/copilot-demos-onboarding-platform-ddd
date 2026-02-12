package com.platform.company.adapters.out.persistence;

import com.platform.company.application.ports.out.CompanyRepository;
import com.platform.company.domain.Company;
import com.platform.company.domain.CompanyStatus;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter implementing the CompanyRepository port using JPA.
 * Translates between domain objects and persistence entities.
 */
@Repository
public class CompanyRepositoryAdapter implements CompanyRepository {

    private final JpaCompanyRepository jpaRepository;

    public CompanyRepositoryAdapter(JpaCompanyRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Company save(Company company) {
        CompanyEntity entity = toEntity(company);
        CompanyEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Company> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public Optional<Company> findByName(String name) {
        return jpaRepository.findByName(name)
                .map(this::toDomain);
    }

    private CompanyEntity toEntity(Company company) {
        return new CompanyEntity(
            company.getId(),
            company.getName(),
            company.getStatus().name(),
            company.getCreatedAt()
        );
    }

    private Company toDomain(CompanyEntity entity) {
        return Company.reconstitute(
            entity.getId(),
            entity.getName(),
            CompanyStatus.valueOf(entity.getStatus()),
            entity.getCreatedAt()
        );
    }
}
