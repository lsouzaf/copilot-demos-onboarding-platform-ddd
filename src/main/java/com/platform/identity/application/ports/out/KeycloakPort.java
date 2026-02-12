package com.platform.identity.application.ports.out;

/**
 * Output Port for Keycloak operations.
 * Defines the contract for identity management following Hexagonal Architecture.
 */
public interface KeycloakPort {
    
    /**
     * Creates a new realm in Keycloak.
     *
     * @param realmName The name of the realm to create
     * @return The realm ID
     */
    String createRealm(String realmName);
    
    /**
     * Creates a new user in a Keycloak realm.
     *
     * @param realm The realm name
     * @param username The username
     * @param email The user's email
     * @param password The user's password
     * @return The user ID
     */
    String createUser(String realm, String username, String email, String password);
    
    /**
     * Deletes a realm from Keycloak.
     *
     * @param realmName The name of the realm to delete
     */
    void deleteRealm(String realmName);
}
