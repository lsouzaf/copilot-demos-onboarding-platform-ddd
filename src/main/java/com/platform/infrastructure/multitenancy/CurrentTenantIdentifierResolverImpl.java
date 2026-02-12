package com.platform.infrastructure.multitenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Hibernate resolver to identify the current tenant.
 */
@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
    
    @Value("${tenant.default-tenant:public}")
    private String defaultTenant;
    
    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getTenantId();
        return tenantId != null ? tenantId : defaultTenant;
    }
    
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
