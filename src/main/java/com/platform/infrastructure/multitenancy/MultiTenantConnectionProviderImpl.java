package com.platform.infrastructure.multitenancy;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Hibernate multi-tenant connection provider.
 * Provides tenant-specific database connections by switching schemas.
 */
@Component
public class MultiTenantConnectionProviderImpl implements MultiTenantConnectionProvider {
    
    private static final Logger logger = LoggerFactory.getLogger(MultiTenantConnectionProviderImpl.class);
    private static final long serialVersionUID = 1L;
    
    private final DataSource dataSource;
    
    public MultiTenantConnectionProviderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }
    
    @Override
    public Connection getConnection(Object tenantIdentifier) throws SQLException {
        String tenantId = tenantIdentifier.toString();
        logger.debug("Getting connection for tenant: {}", tenantId);
        
        validateTenantIdentifier(tenantId);
        
        Connection connection = getAnyConnection();
        
        try {
            connection.createStatement().execute("SET search_path TO " + sanitizeTenantIdentifier(tenantId));
        } catch (SQLException e) {
            logger.error("Failed to set search_path to {}", tenantId, e);
            throw e;
        }
        
        return connection;
    }
    
    @Override
    public void releaseConnection(Object tenantIdentifier, Connection connection) throws SQLException {
        String tenantId = tenantIdentifier.toString();
        logger.debug("Releasing connection for tenant: {}", tenantId);
        
        try {
            connection.createStatement().execute("SET search_path TO public");
        } catch (SQLException e) {
            logger.error("Failed to reset search_path", e);
        }
        
        connection.close();
    }
    
    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }
    
    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return false;
    }
    
    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }
    
    private void validateTenantIdentifier(String tenantIdentifier) {
        if (tenantIdentifier == null || tenantIdentifier.isBlank()) {
            throw new IllegalArgumentException("Tenant identifier cannot be null or empty");
        }
        
        if (!tenantIdentifier.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalArgumentException("Invalid tenant identifier: must contain only alphanumeric characters, underscores, and hyphens");
        }
    }
    
    private String sanitizeTenantIdentifier(String identifier) {
        return identifier.replaceAll("[^a-zA-Z0-9_-]", "");
    }
}
