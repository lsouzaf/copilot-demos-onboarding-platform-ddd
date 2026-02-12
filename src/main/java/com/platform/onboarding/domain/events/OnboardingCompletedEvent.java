package com.platform.onboarding.domain.events;

import com.platform.shared.domain.DomainEvent;
import com.platform.shared.domain.vo.TenantId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Event emitted when the onboarding process completes successfully.
 */
public class OnboardingCompletedEvent implements DomainEvent {
    
    private final UUID eventId;
    private final Instant occurredOn;
    private final UUID onboardingId;
    private final TenantId tenantId;
    
    public OnboardingCompletedEvent(UUID onboardingId, TenantId tenantId) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = Instant.now();
        this.onboardingId = onboardingId;
        this.tenantId = tenantId;
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OnboardingCompletedEvent that = (OnboardingCompletedEvent) o;
        return Objects.equals(eventId, that.eventId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
}
