package com.platform.identity.adapters.out;

import com.platform.identity.application.ports.out.KeycloakPort;
import com.platform.infrastructure.keycloak.KeycloakAdminClient;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Collections;

/**
 * Adapter implementing KeycloakPort for Keycloak operations.
 */
@Component
public class KeycloakAdapter implements KeycloakPort {
    
    private static final Logger logger = LoggerFactory.getLogger(KeycloakAdapter.class);
    
    private final KeycloakAdminClient keycloakAdminClient;
    
    public KeycloakAdapter(KeycloakAdminClient keycloakAdminClient) {
        this.keycloakAdminClient = keycloakAdminClient;
    }
    
    @Override
    public String createRealm(String realmName) {
        try {
            logger.info("Creating Keycloak realm: {}", realmName);
            
            RealmsResource realmsResource = keycloakAdminClient.getRealmsResource();
            
            RealmRepresentation realm = new RealmRepresentation();
            realm.setRealm(realmName);
            realm.setEnabled(true);
            realm.setDisplayName(realmName);
            
            realmsResource.create(realm);
            
            logger.info("Successfully created Keycloak realm: {}", realmName);
            return realmName;
        } catch (Exception e) {
            logger.error("Failed to create Keycloak realm: {}", realmName, e);
            throw new RuntimeException("Failed to create Keycloak realm: " + realmName, e);
        }
    }
    
    @Override
    public String createUser(String realm, String username, String email, String password) {
        try {
            logger.info("Creating user {} in realm {}", username, realm);
            
            RealmsResource realmsResource = keycloakAdminClient.getRealmsResource();
            RealmResource realmResource = realmsResource.realm(realm);
            UsersResource usersResource = realmResource.users();
            
            UserRepresentation user = new UserRepresentation();
            user.setUsername(username);
            user.setEmail(email);
            user.setEnabled(true);
            user.setEmailVerified(true);
            
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);
            credential.setTemporary(false);
            user.setCredentials(Collections.singletonList(credential));
            
            Response response = usersResource.create(user);
            
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create user. Status: " + response.getStatus());
            }
            
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            
            logger.info("Successfully created user {} in realm {}", username, realm);
            return userId;
        } catch (Exception e) {
            logger.error("Failed to create user {} in realm {}", username, realm, e);
            throw new RuntimeException("Failed to create user in Keycloak", e);
        }
    }
    
    @Override
    public void deleteRealm(String realmName) {
        try {
            logger.info("Deleting Keycloak realm: {}", realmName);
            
            RealmsResource realmsResource = keycloakAdminClient.getRealmsResource();
            realmsResource.realm(realmName).remove();
            
            logger.info("Successfully deleted Keycloak realm: {}", realmName);
        } catch (Exception e) {
            logger.error("Failed to delete Keycloak realm: {}", realmName, e);
            throw new RuntimeException("Failed to delete Keycloak realm: " + realmName, e);
        }
    }
}
