package com.platform.infrastructure.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Flyway configuration for database migrations.
 */
@Configuration
public class FlywayConfig {
    
    @Value("${spring.flyway.enabled:true}")
    private boolean enabled;
    
    @Value("${spring.flyway.schemas:public}")
    private String schemas;
    
    @Value("${spring.flyway.locations:classpath:db/migration}")
    private String locations;
    
    @Bean
    public Flyway flyway(DataSource dataSource) {
        if (!enabled) {
            return null;
        }
        
        Flyway flyway = Flyway.configure()
            .dataSource(dataSource)
            .schemas(schemas.split(","))
            .locations(locations.split(","))
            .baselineOnMigrate(true)
            .load();
        
        flyway.migrate();
        
        return flyway;
    }
}
