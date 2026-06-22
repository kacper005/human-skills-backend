package com.talection.talection.dto.requests;

import com.talection.talection.enums.AuthProvider;
import com.talection.talection.enums.Gender;
import com.talection.talection.enums.Role;
import jakarta.validation.constraints.NotNull;


/**
 * Data Transfer Object for user sign-up requests.
 * This class encapsulates the necessary information
 * for creating a new user account in the application
 */
public class SignUpRequest {

    @NotNull
    private AuthProvider authProvider;

    // Optional, used for Google and Feide authentication
    private String idToken;

    // Optional, used for local authentication
    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private Gender gender;

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
