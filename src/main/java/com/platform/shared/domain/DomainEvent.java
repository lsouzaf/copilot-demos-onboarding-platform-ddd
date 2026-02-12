package com.platform.shared.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/**
 * Base interface for all Domain Events.
 * Domain Events represent something that happened in the domain that domain experts care about.
 */
public interface DomainEvent extends Serializable {
    
    UUID getEventId();
    
    Instant getOccurredOn();
    
    String getEventType();
}
