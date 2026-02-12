package com.platform.company.application;

import com.platform.company.application.ports.in.CreateCompanyUseCase;
import com.platform.company.application.ports.out.CompanyRepository;
import com.platform.company.domain.Company;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application Service implementing the CreateCompanyUseCase.
 * Orchestrates the company creation process following Hexagonal Architecture principles.
 */
@Service
public class CompanyService implements CreateCompanyUseCase {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional
    public CompanyResponse create(CreateCompanyCommand command) {
        Company company = Company.create(command.name());

        Company savedCompany = companyRepository.save(company);

        return new CompanyResponse(
            savedCompany.getId(),
            savedCompany.getName(),
            savedCompany.getStatus().name()
        );
    }
}
