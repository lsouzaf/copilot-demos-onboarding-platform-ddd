package com.platform.infrastructure.multitenancy;

/**
 * Thread-local storage for tenant context.
 * Holds the current tenant ID for multi-tenant operations.
 */
public class TenantContext {
    
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    
    private TenantContext() {
    }
    
    /**
     * Sets the current tenant ID.
     *
     * @param tenantId The tenant ID to set
     */
    public static void setTenantId(String tenantId) {
        currentTenant.set(tenantId);
    }
    
    /**
     * Gets the current tenant ID.
     *
     * @return The current tenant ID
     */
    public static String getTenantId() {
        return currentTenant.get();
    }
    
    /**
     * Clears the current tenant context.
     */
    public static void clear() {
        currentTenant.remove();
    }
}
