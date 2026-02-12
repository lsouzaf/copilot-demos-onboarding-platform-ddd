package com.platform.shared.domain.vo;

import com.platform.shared.domain.ValueObject;

import java.util.Objects;
import java.util.UUID;

/**
 * TenantId Value Object - represents a unique tenant identifier.
 * Immutable and validated.
 */
public final class TenantId extends ValueObject {
    
    private final String value;
    
    private TenantId(String value) {
        this.value = value;
    }
    
    public static TenantId generate() {
        String tenantId = "tenant_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        return new TenantId(tenantId);
    }
    
    public static TenantId of(String tenantId) {
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException("TenantId cannot be null or empty");
        }
        
        String normalized = tenantId.trim().toLowerCase();
        
        if (!normalized.matches("^[a-z0-9_-]+$")) {
            throw new IllegalArgumentException("TenantId must contain only lowercase letters, numbers, underscore and hyphen");
        }
        
        return new TenantId(normalized);
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantId tenantId = (TenantId) o;
        return Objects.equals(value, tenantId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}
