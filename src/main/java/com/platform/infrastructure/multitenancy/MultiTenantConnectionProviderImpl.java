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
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        logger.debug("Getting connection for tenant: {}", tenantIdentifier);
        
        Connection connection = getAnyConnection();
        
        try {
            connection.createStatement().execute("SET search_path TO " + tenantIdentifier);
        } catch (SQLException e) {
            logger.error("Failed to set search_path to {}", tenantIdentifier, e);
            throw e;
        }
        
        return connection;
    }
    
    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        logger.debug("Releasing connection for tenant: {}", tenantIdentifier);
        
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
}
