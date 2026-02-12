package com.platform.onboarding.domain;

import com.platform.shared.domain.ValueObject;
import com.platform.shared.domain.vo.Email;

import java.util.Objects;

/**
 * AdminInfo Value Object - represents administrator information.
 * Immutable and validated.
 */
public final class AdminInfo extends ValueObject {
    
    private static final int MAX_LENGTH = 100;
    
    private final String adminName;
    private final Email adminEmail;
    
    private AdminInfo(String adminName, Email adminEmail) {
        this.adminName = adminName;
        this.adminEmail = adminEmail;
    }
    
    public static AdminInfo of(String adminName, Email adminEmail) {
        if (adminName == null || adminName.isBlank()) {
            throw new IllegalArgumentException("Admin name cannot be null or empty");
        }
        
        if (adminEmail == null) {
            throw new IllegalArgumentException("Admin email cannot be null");
        }
        
        String trimmed = adminName.trim();
        
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Admin name cannot exceed %d characters", MAX_LENGTH)
            );
        }
        
        return new AdminInfo(trimmed, adminEmail);
    }
    
    public String getAdminName() {
        return adminName;
    }
    
    public Email getAdminEmail() {
        return adminEmail;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminInfo adminInfo = (AdminInfo) o;
        return Objects.equals(adminName, adminInfo.adminName) &&
               Objects.equals(adminEmail, adminInfo.adminEmail);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(adminName, adminEmail);
    }
    
    @Override
    public String toString() {
        return String.format("AdminInfo{name='%s', email='%s'}", adminName, adminEmail);
    }
}
