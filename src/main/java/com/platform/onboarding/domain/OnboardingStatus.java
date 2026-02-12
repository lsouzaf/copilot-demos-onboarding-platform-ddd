package com.platform.onboarding.domain;

/**
 * Represents the status of an onboarding process.
 * Tracks the progression through different onboarding stages.
 */
public enum OnboardingStatus {
    INITIATED,
    SCHEMA_CREATED,
    COMPANY_CREATED,
    REALM_CREATED,
    ADMIN_CREATED,
    EMAIL_SENT,
    COMPLETED,
    FAILED
}
