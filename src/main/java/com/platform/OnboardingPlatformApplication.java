package com.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main application class for the Onboarding Platform.
 * 
 * This application implements a multi-tenant onboarding platform using:
 * - Domain-Driven Design (DDD)
 * - Hexagonal Architecture (Ports & Adapters)
 * - Multi-tenancy with Hibernate SCHEMA strategy
 * - Saga Pattern with Kafka for orchestration
 * - Event-Driven Architecture
 * 
 * Bounded Contexts:
 * - Onboarding: Manages the onboarding process
 * - Company: Manages company entities
 * - Identity: Integrates with Keycloak
 * - Shared: Common domain objects
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
@EnableKafka
public class OnboardingPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnboardingPlatformApplication.class, args);
    }
}
