package com.platform.onboarding.domain.events;

import com.platform.shared.domain.DomainEvent;
import com.platform.shared.domain.vo.Email;
import com.platform.shared.domain.vo.TenantId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Event emitted when a welcome email is sent to the administrator.
 */
public class EmailSentEvent implements DomainEvent {
    
    private final UUID eventId;
    private final Instant occurredOn;
    private final UUID onboardingId;
    private final TenantId tenantId;
    private final Email recipientEmail;
    
    public EmailSentEvent(
            UUID onboardingId,
            TenantId tenantId,
            Email recipientEmail) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = Instant.now();
        this.onboardingId = onboardingId;
        this.tenantId = tenantId;
        this.recipientEmail = recipientEmail;
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
    
    public Email getRecipientEmail() {
        return recipientEmail;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailSentEvent that = (EmailSentEvent) o;
        return Objects.equals(eventId, that.eventId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(eventId);
    }
}
