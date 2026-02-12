package com.platform.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * JPA configuration.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.platform")
public class JpaConfig {
}
