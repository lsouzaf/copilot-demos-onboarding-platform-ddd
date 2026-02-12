package com.platform.onboarding.domain.events;

import com.platform.onboarding.domain.AdminInfo;
import com.platform.onboarding.domain.CompanyInfo;
import com.platform.shared.domain.DomainEvent;
import com.platform.shared.domain.vo.TenantId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Event emitted when an onboarding process is initiated.
 */
public class OnboardingInitiatedEvent implements DomainEvent {
    
    private final UUID eventId;
    private final Instant occurredOn;
    private final UUID onboardingId;
    private final TenantId tenantId;
    private final CompanyInfo companyInfo;
    private final AdminInfo adminInfo;
    
    public OnboardingInitiatedEvent(
            UUID onboardingId,
            TenantId tenantId,
            CompanyInfo companyInfo,
            AdminInfo adminInfo) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = Instant.now();
        this.onboardingId = onboardingId;
        this.tenantId = tenantId;
        this.companyInfo = companyInfo;
        this.adminInfo = adminInfo;
    }
    
    @Override
    public UUID getEventId() {
        return eventId;
    }
    
    @Override
    public Instant getOccurredOn() {
        return occurredOn;
    }
    
    @Override
    public String getEventType() {
        return this.getClass().getSimpleName();
    }
    
    public UUID getOnboardingId() {
        return onboardingId;
    }
    
    public TenantId getTenantId() {
        return tenantId;
    }
    
    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }
    
    public AdminInfo getAdminInfo() {
        return adminInfo;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnboardingInitiatedEvent that = (OnboardingInitiatedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
}
