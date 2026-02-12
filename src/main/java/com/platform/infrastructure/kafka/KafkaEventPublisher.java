package com.platform.infrastructure.kafka;

import com.platform.onboarding.application.ports.out.EventPublisher;
import com.platform.shared.domain.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Kafka implementation of EventPublisher.
 * Publishes domain events to Kafka topics.
 */
@Component
public class KafkaEventPublisher implements EventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaEventPublisher.class);
    private static final String TOPIC_PREFIX = "onboarding.";
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @Override
    public void publish(DomainEvent event) {
        String topic = TOPIC_PREFIX + event.getEventType();
        logger.info("Publishing event {} to topic {}", event.getEventType(), topic);
        
        kafkaTemplate.send(topic, event.getEventId().toString(), event);
        
        logger.debug("Event {} published successfully", event.getEventType());
    }
    
    @Override
    public void publishAll(List<DomainEvent> events) {
        events.forEach(this::publish);
    }
}
