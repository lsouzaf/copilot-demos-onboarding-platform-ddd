package com.platform.onboarding.domain;

import com.platform.onboarding.domain.events.*;
import com.platform.shared.domain.AggregateRoot;
import com.platform.shared.domain.vo.TenantId;

import java.time.Instant;
import java.util.UUID;

/**
 * OnboardingRequest Aggregate Root - manages the onboarding process lifecycle.
 * Enforces state transitions and emits domain events.
 */
public class OnboardingRequest extends AggregateRoot<UUID> {
    
    private final TenantId tenantId;
    private final CompanyInfo companyInfo;
    private final AdminInfo adminInfo;
    private OnboardingStatus status;
    private String failureReason;
    private final Instant createdAt;
    private Instant completedAt;
    
    private OnboardingRequest(
            UUID id,
            TenantId tenantId,
            CompanyInfo companyInfo,
            AdminInfo adminInfo,
            OnboardingStatus status,
            Instant createdAt) {
        super(id);
        this.tenantId = tenantId;
        this.companyInfo = companyInfo;
        this.adminInfo = adminInfo;
        this.status = status;
        this.createdAt = createdAt;
    }
    
    public static OnboardingRequest initiate(CompanyInfo companyInfo, AdminInfo adminInfo) {
        if (companyInfo == null) {
            throw new IllegalArgumentException("CompanyInfo cannot be null");
        }
        if (adminInfo == null) {
            throw new IllegalArgumentException("AdminInfo cannot be null");
        }
        
        UUID id = UUID.randomUUID();
        TenantId tenantId = TenantId.generate();
        Instant now = Instant.now();
        
        OnboardingRequest request = new OnboardingRequest(
            id,
            tenantId,
            companyInfo,
            adminInfo,
            OnboardingStatus.INITIATED,
            now
        );
        
        request.registerEvent(new OnboardingInitiatedEvent(id, tenantId, companyInfo, adminInfo));
        
        return request;
    }
    
    public void markSchemaCreated() {
        validateTransition(OnboardingStatus.INITIATED, "Schema creation");
        
        this.status = OnboardingStatus.SCHEMA_CREATED;
        registerEvent(new SchemaCreatedEvent(getId(), tenantId));
    }
    
    public void markCompanyCreated() {
        validateTransition(OnboardingStatus.SCHEMA_CREATED, "Company creation");
        
        this.status = OnboardingStatus.COMPANY_CREATED;
        registerEvent(new CompanyCreatedEvent(getId(), tenantId, companyInfo));
    }
    
    public void markRealmCreated() {
        validateTransition(OnboardingStatus.COMPANY_CREATED, "Realm creation");
        
        this.status = OnboardingStatus.REALM_CREATED;
        registerEvent(new RealmCreatedEvent(getId(), tenantId));
    }
    
    public void markAdminCreated() {
        validateTransition(OnboardingStatus.REALM_CREATED, "Admin creation");
        
        this.status = OnboardingStatus.ADMIN_CREATED;
        registerEvent(new AdminCreatedEvent(getId(), tenantId, adminInfo));
    }
    
    public void markEmailSent() {
        validateTransition(OnboardingStatus.ADMIN_CREATED, "Email sending");
        
        this.status = OnboardingStatus.EMAIL_SENT;
        registerEvent(new EmailSentEvent(getId(), tenantId, adminInfo.getAdminEmail()));
    }
    
    public void complete() {
        validateTransition(OnboardingStatus.EMAIL_SENT, "Completion");
        
        this.status = OnboardingStatus.COMPLETED;
        this.completedAt = Instant.now();
        registerEvent(new OnboardingCompletedEvent(getId(), tenantId));
    }
    
    public void fail(String reason) {
        if (this.status == OnboardingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot fail an already completed onboarding");
        }
        
        if (this.status == OnboardingStatus.FAILED) {
            throw new IllegalStateException("Onboarding is already in failed state");
        }
        
        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException("Failure reason cannot be null or empty");
        }
        
        this.status = OnboardingStatus.FAILED;
        this.failureReason = reason.trim();
        this.completedAt = Instant.now();
        registerEvent(new OnboardingFailedEvent(getId(), tenantId, reason.trim()));
    }
    
    private void validateTransition(OnboardingStatus expectedStatus, String operation) {
        if (this.status == OnboardingStatus.FAILED) {
            throw new IllegalStateException("Cannot proceed with " + operation + ": onboarding has failed");
        }
        
        if (this.status == OnboardingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot proceed with " + operation + ": onboarding is already completed");
        }
        
        if (this.status != expectedStatus) {
            throw new IllegalStateException(
                String.format("%s requires status %s, but current status is %s",
                    operation, expectedStatus, this.status)
            );
        }
    }
    
    public UUID getId() {
        return id;
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
    
    public OnboardingStatus getStatus() {
        return status;
    }
    
    public String getFailureReason() {
        return failureReason;
    }
    
    public Instant getCreatedAt() {
        return createdAt;
    }
    
    public Instant getCompletedAt() {
        return completedAt;
    }
}
