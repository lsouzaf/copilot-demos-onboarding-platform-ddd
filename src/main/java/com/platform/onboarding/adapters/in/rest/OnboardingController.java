package com.platform.onboarding.adapters.in.rest;

import com.platform.onboarding.application.ports.in.InitiateOnboardingUseCase;
import com.platform.onboarding.application.ports.in.InitiateOnboardingUseCase.InitiateOnboardingCommand;
import com.platform.onboarding.application.ports.in.InitiateOnboardingUseCase.OnboardingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Onboarding API.
 * Implements the input adapter for initiating onboarding processes.
 */
@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

    private final InitiateOnboardingUseCase initiateOnboardingUseCase;

    public OnboardingController(InitiateOnboardingUseCase initiateOnboardingUseCase) {
        this.initiateOnboardingUseCase = initiateOnboardingUseCase;
    }

    @PostMapping
    public ResponseEntity<OnboardingResponse> initiateOnboarding(
            @RequestBody InitiateOnboardingCommand command) {
        
        OnboardingResponse response = initiateOnboardingUseCase.initiate(command);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = new ErrorResponse(
                "VALIDATION_ERROR",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        ErrorResponse error = new ErrorResponse(
                "INVALID_STATE",
                ex.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
                "INTERNAL_ERROR",
                "An unexpected error occurred"
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    record ErrorResponse(String code, String message) {}
}
