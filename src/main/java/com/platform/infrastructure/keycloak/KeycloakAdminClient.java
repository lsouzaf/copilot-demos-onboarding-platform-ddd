package com.platform.infrastructure.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmsResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Keycloak admin client for managing realms and users.
 */
@Component
public class KeycloakAdminClient {
    
    @Value("${keycloak.auth-server-url}")
    private String serverUrl;
    
    @Value("${keycloak.realm:master}")
    private String realm;
    
    @Value("${keycloak.admin-username:admin}")
    private String username;
    
    @Value("${keycloak.admin-password:admin}")
    private String password;
    
    @Value("${keycloak.admin-client-id:admin-cli}")
    private String clientId;
    
    private Keycloak keycloak;
    
    @PostConstruct
    public void init() {
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .username(username)
                .password(password)
                .clientId(clientId)
                .build();
    }
    
    public Keycloak getKeycloak() {
        return keycloak;
    }
    
    public RealmsResource getRealmsResource() {
        return keycloak.realms();
    }
}
