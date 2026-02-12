package com.platform.infrastructure.saga;

import com.platform.company.application.CompanyService;
import com.platform.company.application.ports.in.CreateCompanyCommand;
import com.platform.identity.application.ports.out.KeycloakPort;
import com.platform.infrastructure.kafka.KafkaEventPublisher;
import com.platform.infrastructure.multitenancy.SchemaService;
import com.platform.onboarding.application.ports.out.EventPublisher;
import com.platform.onboarding.domain.events.*;
import com.platform.shared.domain.vo.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Saga orchestrator for onboarding process.
 * Coordinates the multi-step onboarding workflow using event-driven saga pattern.
 */
@Component
public class OnboardingSagaOrchestrator {
    
    private static final Logger logger = LoggerFactory.getLogger(OnboardingSagaOrchestrator.class);
    private static final String TEMPLATE_SCHEMA = "public";
    
    private final SchemaService schemaService;
    private final CompanyService companyService;
    private final KeycloakPort keycloakPort;
    private final EmailService emailService;
    private final EventPublisher eventPublisher;
    
    public OnboardingSagaOrchestrator(
            SchemaService schemaService,
            CompanyService companyService,
            KeycloakPort keycloakPort,
            EmailService emailService,
            EventPublisher eventPublisher) {
        this.schemaService = schemaService;
        this.companyService = companyService;
        this.keycloakPort = keycloakPort;
        this.emailService = emailService;
        this.eventPublisher = eventPublisher;
    }
    
    @KafkaListener(topics = "onboarding.OnboardingInitiatedEvent", groupId = "onboarding-saga")
    public void onOnboardingInitiated(OnboardingInitiatedEvent event) {
        logger.info("Processing OnboardingInitiatedEvent for onboarding {}", event.getOnboardingId());
        
        try {
            String schemaName = event.getTenantId().getValue();
            schemaService.createSchema(schemaName, TEMPLATE_SCHEMA);
            
            SchemaCreatedEvent schemaCreatedEvent = new SchemaCreatedEvent(
                event.getOnboardingId(),
                event.getTenantId()
            );
            
            eventPublisher.publish(schemaCreatedEvent);
            logger.info("Schema created successfully for tenant {}", schemaName);
        } catch (Exception e) {
            logger.error("Failed to create schema for onboarding {}", event.getOnboardingId(), e);
            handleError(event.getOnboardingId(), event.getTenantId(), "Schema creation failed", e);
        }
    }
    
    @KafkaListener(topics = "onboarding.SchemaCreatedEvent", groupId = "onboarding-saga")
    public void onSchemaCreated(SchemaCreatedEvent event) {
        logger.info("Processing SchemaCreatedEvent for onboarding {}", event.getOnboardingId());
        
        try {
            OnboardingInitiatedEvent initiatedEvent = findInitiatedEvent(event.getOnboardingId());
            
            CreateCompanyCommand command = new CreateCompanyCommand(
                initiatedEvent.getCompanyInfo().getCompanyName()
            );
            
            companyService.create(command);
            
            CompanyCreatedEvent companyCreatedEvent = new CompanyCreatedEvent(
                event.getOnboardingId(),
                event.getTenantId(),
                initiatedEvent.getCompanyInfo()
            );
            
            eventPublisher.publish(companyCreatedEvent);
            logger.info("Company created successfully for tenant {}", event.getTenantId().getValue());
        } catch (Exception e) {
            logger.error("Failed to create company for onboarding {}", event.getOnboardingId(), e);
            handleError(event.getOnboardingId(), event.getTenantId(), "Company creation failed", e);
        }
    }
    
    @KafkaListener(topics = "onboarding.CompanyCreatedEvent", groupId = "onboarding-saga")
    public void onCompanyCreated(CompanyCreatedEvent event) {
        logger.info("Processing CompanyCreatedEvent for onboarding {}", event.getOnboardingId());
        
        try {
            String realmName = event.getTenantId().getValue();
            keycloakPort.createRealm(realmName);
            
            RealmCreatedEvent realmCreatedEvent = new RealmCreatedEvent(
                event.getOnboardingId(),
                event.getTenantId()
            );
            
            eventPublisher.publish(realmCreatedEvent);
            logger.info("Realm created successfully for tenant {}", realmName);
        } catch (Exception e) {
            logger.error("Failed to create realm for onboarding {}", event.getOnboardingId(), e);
            handleError(event.getOnboardingId(), event.getTenantId(), "Realm creation failed", e);
        }
    }
    
    @KafkaListener(topics = "onboarding.RealmCreatedEvent", groupId = "onboarding-saga")
    public void onRealmCreated(RealmCreatedEvent event) {
        logger.info("Processing RealmCreatedEvent for onboarding {}", event.getOnboardingId());
        
        try {
            OnboardingInitiatedEvent initiatedEvent = findInitiatedEvent(event.getOnboardingId());
            
            String realm = event.getTenantId().getValue();
            String username = initiatedEvent.getAdminInfo().getAdminName();
            String email = initiatedEvent.getAdminInfo().getAdminEmail().getValue();
            String password = generateSecurePassword();
            
            keycloakPort.createUser(realm, username, email, password);
            
            AdminCreatedEvent adminCreatedEvent = new AdminCreatedEvent(
                event.getOnboardingId(),
                event.getTenantId(),
                initiatedEvent.getAdminInfo()
            );
            
            eventPublisher.publish(adminCreatedEvent);
            logger.info("Admin user created successfully for tenant {}", realm);
        } catch (Exception e) {
            logger.error("Failed to create admin user for onboarding {}", event.getOnboardingId(), e);
            handleError(event.getOnboardingId(), event.getTenantId(), "Admin creation failed", e);
        }
    }
    
    @KafkaListener(topics = "onboarding.AdminCreatedEvent", groupId = "onboarding-saga")
    public void onAdminCreated(AdminCreatedEvent event) {
        logger.info("Processing AdminCreatedEvent for onboarding {}", event.getOnboardingId());
        
        try {
            String email = event.getAdminInfo().getAdminEmail().getValue();
            String tenantId = event.getTenantId().getValue();
            
            emailService.sendWelcomeEmail(email, tenantId);
            
            EmailSentEvent emailSentEvent = new EmailSentEvent(
                event.getOnboardingId(),
                event.getTenantId(),
                event.getAdminInfo().getAdminEmail()
            );
            
            eventPublisher.publish(emailSentEvent);
            logger.info("Welcome email sent successfully to {}", email);
        } catch (Exception e) {
            logger.error("Failed to send email for onboarding {}", event.getOnboardingId(), e);
            handleError(event.getOnboardingId(), event.getTenantId(), "Email sending failed", e);
        }
    }
    
    @KafkaListener(topics = "onboarding.EmailSentEvent", groupId = "onboarding-saga")
    public void onEmailSent(EmailSentEvent event) {
        logger.info("Processing EmailSentEvent for onboarding {}", event.getOnboardingId());
        
        try {
            OnboardingCompletedEvent completedEvent = new OnboardingCompletedEvent(
                event.getOnboardingId(),
                event.getTenantId()
            );
            
            eventPublisher.publish(completedEvent);
            logger.info("Onboarding completed successfully for tenant {}", event.getTenantId().getValue());
        } catch (Exception e) {
            logger.error("Failed to complete onboarding {}", event.getOnboardingId(), e);
            handleError(event.getOnboardingId(), event.getTenantId(), "Onboarding completion failed", e);
        }
    }
    
    private void handleError(java.util.UUID onboardingId, com.platform.shared.domain.vo.TenantId tenantId, String reason, Exception e) {
        logger.error("Saga error for onboarding {}: {}", onboardingId, reason, e);
        
        try {
            compensate(tenantId);
        } catch (Exception compensationError) {
            logger.error("Compensation failed for onboarding {}", onboardingId, compensationError);
        }
        
        OnboardingFailedEvent failedEvent = new OnboardingFailedEvent(onboardingId, tenantId, reason);
        eventPublisher.publish(failedEvent);
    }
    
    private void compensate(com.platform.shared.domain.vo.TenantId tenantId) {
        logger.info("Starting compensation for tenant {}", tenantId.getValue());
        
        try {
            schemaService.deleteSchema(tenantId.getValue());
        } catch (Exception e) {
            logger.warn("Failed to delete schema during compensation", e);
        }
        
        try {
            keycloakPort.deleteRealm(tenantId.getValue());
        } catch (Exception e) {
            logger.warn("Failed to delete realm during compensation", e);
        }
    }
    
    private OnboardingInitiatedEvent findInitiatedEvent(java.util.UUID onboardingId) {
        return null;
    }
    
    private String generateSecurePassword() {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*";
        String allChars = upperCase + lowerCase + digits + special;
        
        java.security.SecureRandom random = new java.security.SecureRandom();
        StringBuilder password = new StringBuilder(16);
        
        password.append(upperCase.charAt(random.nextInt(upperCase.length())));
        password.append(lowerCase.charAt(random.nextInt(lowerCase.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(special.charAt(random.nextInt(special.length())));
        
        for (int i = 4; i < 16; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }
        
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }
}
