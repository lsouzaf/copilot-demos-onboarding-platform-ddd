package com.platform.onboarding.adapters.out.persistence;

import com.platform.onboarding.application.ports.out.OnboardingRepository;
import com.platform.onboarding.domain.OnboardingRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Adapter implementing the OnboardingRepository port using JPA.
 * Translates between domain objects and persistence entities.
 */
@Repository
public class OnboardingRepositoryAdapter implements OnboardingRepository {

    private final JpaOnboardingRepository jpaRepository;
    private final OnboardingMapper mapper;

    public OnboardingRepositoryAdapter(JpaOnboardingRepository jpaRepository, OnboardingMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public OnboardingRequest save(OnboardingRequest request) {
        OnboardingEntity entity = mapper.toEntity(request);
        OnboardingEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<OnboardingRequest> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<OnboardingRequest> findByTenantId(String tenantId) {
        return jpaRepository.findByTenantId(tenantId)
                .map(mapper::toDomain);
    }
}
