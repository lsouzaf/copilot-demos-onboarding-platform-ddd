package com.platform.onboarding.adapters.out.persistence;

import com.platform.onboarding.domain.AdminInfo;
import com.platform.onboarding.domain.CompanyInfo;
import com.platform.onboarding.domain.OnboardingRequest;
import com.platform.onboarding.domain.OnboardingStatus;
import com.platform.shared.domain.vo.Email;
import com.platform.shared.domain.vo.TenantId;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.UUID;

/**
 * Mapper component for converting between domain and persistence models.
 * Handles the transformation between OnboardingRequest and OnboardingEntity.
 */
@Component
public class OnboardingMapper {

    public OnboardingEntity toEntity(OnboardingRequest domain) {
        if (domain == null) {
            return null;
        }

        return new OnboardingEntity(
                domain.getId(),
                domain.getTenantId().getValue(),
                domain.getCompanyInfo().getCompanyName(),
                domain.getAdminInfo().getAdminName(),
                domain.getAdminInfo().getAdminEmail().getValue(),
                domain.getStatus().name(),
                domain.getFailureReason(),
                domain.getCreatedAt(),
                domain.getCompletedAt()
        );
    }

    public OnboardingRequest toDomain(OnboardingEntity entity) {
        if (entity == null) {
            return null;
        }

        try {
            TenantId tenantId = TenantId.of(entity.getTenantId());
            CompanyInfo companyInfo = CompanyInfo.of(entity.getCompanyName());
            AdminInfo adminInfo = AdminInfo.of(
                    entity.getAdminName(),
                    Email.of(entity.getAdminEmail())
            );
            OnboardingStatus status = OnboardingStatus.valueOf(entity.getStatus());

            Constructor<OnboardingRequest> constructor = OnboardingRequest.class
                    .getDeclaredConstructor(
                            UUID.class,
                            TenantId.class,
                            CompanyInfo.class,
                            AdminInfo.class,
                            OnboardingStatus.class,
                            Instant.class
                    );
            constructor.setAccessible(true);

            OnboardingRequest request = constructor.newInstance(
                    entity.getId(),
                    tenantId,
                    companyInfo,
                    adminInfo,
                    status,
                    entity.getCreatedAt()
            );

            if (entity.getFailureReason() != null) {
                Field failureReasonField = OnboardingRequest.class.getDeclaredField("failureReason");
                failureReasonField.setAccessible(true);
                failureReasonField.set(request, entity.getFailureReason());
            }

            if (entity.getCompletedAt() != null) {
                Field completedAtField = OnboardingRequest.class.getDeclaredField("completedAt");
                completedAtField.setAccessible(true);
                completedAtField.set(request, entity.getCompletedAt());
            }

            return request;

        } catch (Exception e) {
            throw new IllegalStateException("Failed to map entity to domain", e);
        }
    }
}
