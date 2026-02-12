package com.platform.onboarding.application;

import com.platform.onboarding.application.ports.in.InitiateOnboardingUseCase;
import com.platform.onboarding.application.ports.out.EventPublisher;
import com.platform.onboarding.application.ports.out.OnboardingRepository;
import com.platform.onboarding.domain.AdminInfo;
import com.platform.onboarding.domain.CompanyInfo;
import com.platform.onboarding.domain.OnboardingRequest;
import com.platform.shared.domain.vo.Email;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service implementing the InitiateOnboardingUseCase.
 * Orchestrates the onboarding initiation process following Hexagonal Architecture principles.
 */
@Service
public class OnboardingService implements InitiateOnboardingUseCase {
    
    private final OnboardingRepository onboardingRepository;
    private final EventPublisher eventPublisher;
    
    public OnboardingService(
            OnboardingRepository onboardingRepository,
            EventPublisher eventPublisher) {
        this.onboardingRepository = onboardingRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Override
    @Transactional
    public OnboardingResponse initiate(InitiateOnboardingCommand command) {
        Email adminEmail = Email.of(command.adminEmail());
        
        CompanyInfo companyInfo = CompanyInfo.of(command.companyName());
        AdminInfo adminInfo = AdminInfo.of(command.adminName(), adminEmail);
        
        OnboardingRequest onboardingRequest = OnboardingRequest.initiate(companyInfo, adminInfo);
        
        OnboardingRequest savedRequest = onboardingRepository.save(onboardingRequest);
        
        eventPublisher.publishAll(savedRequest.pullDomainEvents());
        
        return new OnboardingResponse(
            savedRequest.getId(),
            savedRequest.getTenantId().getValue(),
            savedRequest.getStatus().name()
        );
    }
}
