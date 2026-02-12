package com.platform.onboarding.domain;

import com.platform.shared.domain.ValueObject;

import java.util.Objects;

/**
 * CompanyInfo Value Object - represents company information.
 * Immutable and validated.
 */
public final class CompanyInfo extends ValueObject {
    
    private static final int MAX_LENGTH = 100;
    
    private final String companyName;
    
    private CompanyInfo(String companyName) {
        this.companyName = companyName;
    }
    
    public static CompanyInfo of(String companyName) {
        if (companyName == null || companyName.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
        
        String trimmed = companyName.trim();
        
        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Company name cannot exceed %d characters", MAX_LENGTH)
            );
        }
        
        return new CompanyInfo(trimmed);
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyInfo that = (CompanyInfo) o;
        return Objects.equals(companyName, that.companyName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(companyName);
    }
    
    @Override
    public String toString() {
        return companyName;
    }
}
