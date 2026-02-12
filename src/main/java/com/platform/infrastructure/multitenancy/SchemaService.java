package com.platform.infrastructure.multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Service for managing database schemas for multi-tenancy.
 */
@Service
public class SchemaService {
    
    private static final Logger logger = LoggerFactory.getLogger(SchemaService.class);
    
    private final DataSource dataSource;
    
    public SchemaService(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    /**
     * Creates a new schema by copying a template schema.
     *
     * @param schemaName The name of the schema to create
     * @param templateSchema The template schema to copy from
     */
    public void createSchema(String schemaName, String templateSchema) {
        logger.info("Creating schema {} from template {}", schemaName, templateSchema);
        
        validateSchemaName(schemaName);
        validateSchemaName(templateSchema);
        
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            
            statement.execute("CREATE SCHEMA IF NOT EXISTS " + sanitizeIdentifier(schemaName));
            
            String copyTablesQuery = String.format(
                "DO $$ " +
                "DECLARE " +
                "  r RECORD; " +
                "BEGIN " +
                "  FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = '%s') LOOP " +
                "    EXECUTE 'CREATE TABLE %s.' || quote_ident(r.tablename) || ' (LIKE %s.' || quote_ident(r.tablename) || ' INCLUDING ALL)'; " +
                "  END LOOP; " +
                "END $$;",
                sanitizeString(templateSchema), sanitizeIdentifier(schemaName), sanitizeString(templateSchema)
            );
            
            statement.execute(copyTablesQuery);
            
            logger.info("Successfully created schema {}", schemaName);
        } catch (SQLException e) {
            logger.error("Failed to create schema {}", schemaName, e);
            throw new RuntimeException("Failed to create schema: " + schemaName, e);
        }
    }
    
    /**
     * Deletes a schema and all its contents.
     *
     * @param schemaName The name of the schema to delete
     */
    public void deleteSchema(String schemaName) {
        logger.info("Deleting schema {}", schemaName);
        
        if ("public".equals(schemaName)) {
            throw new IllegalArgumentException("Cannot delete public schema");
        }
        
        validateSchemaName(schemaName);
        
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            
            statement.execute("DROP SCHEMA IF EXISTS " + sanitizeIdentifier(schemaName) + " CASCADE");
            
            logger.info("Successfully deleted schema {}", schemaName);
        } catch (SQLException e) {
            logger.error("Failed to delete schema {}", schemaName, e);
            throw new RuntimeException("Failed to delete schema: " + schemaName, e);
        }
    }
    
    private void validateSchemaName(String schemaName) {
        if (schemaName == null || schemaName.isBlank()) {
            throw new IllegalArgumentException("Schema name cannot be null or empty");
        }
        
        if (!schemaName.matches("^[a-zA-Z0-9_-]+$")) {
            throw new IllegalArgumentException("Invalid schema name: must contain only alphanumeric characters, underscores, and hyphens");
        }
        
        if (schemaName.length() > 63) {
            throw new IllegalArgumentException("Schema name too long: maximum 63 characters");
        }
    }
    
    private String sanitizeIdentifier(String identifier) {
        return identifier.replaceAll("[^a-zA-Z0-9_-]", "");
    }
    
    private String sanitizeString(String value) {
        return value.replaceAll("'", "''");
    }
}
