package com.platform.infrastructure.saga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Email service for sending emails.
 * This is a mock implementation that logs emails.
 */
@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    /**
     * Sends a welcome email to a new tenant admin.
     *
     * @param email The recipient's email address
     * @param tenantId The tenant ID
     */
    public void sendWelcomeEmail(String email, String tenantId) {
        logger.info("Sending welcome email to {} for tenant {}", email, tenantId);
        logger.info("Email content: Welcome to the platform! Your tenant ID is: {}", tenantId);
    }
}
