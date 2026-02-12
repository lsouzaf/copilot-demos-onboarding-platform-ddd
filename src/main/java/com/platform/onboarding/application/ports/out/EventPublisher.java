package com.platform.onboarding.application.ports.out;

import com.platform.shared.domain.DomainEvent;

import java.util.List;

/**
 * Output Port (Event Publisher Interface) for publishing domain events.
 * Defines the contract for infrastructure adapters following Hexagonal Architecture.
 */
public interface EventPublisher {
    
    /**
     * Publishes a single domain event.
     *
     * @param event The domain event to publish
     */
    void publish(DomainEvent event);
    
    /**
     * Publishes multiple domain events.
     *
     * @param events The list of domain events to publish
     */
    void publishAll(List<DomainEvent> events);
}
