package com.talection.talection.model.userrelated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.talection.talection.enums.AuthProvider;
import com.talection.talection.enums.Gender;
import com.talection.talection.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

/**
 * User entity representing a user in the application.
 * This class is used to store user information in the database.
 */
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String credentialId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Column(unique = true)
    private String email;

    private String profilePictureUrl;

    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.UNIDENTIFIED;

    @CreationTimestamp
    private Date createdAt;

    @JsonIgnore
    private String password;

    public User() {
        // Default constructor for JPA
    }

    /**
     * Returns the authentication provider for the user.
     *
     * @return the AuthProvider enum representing the authentication provider
     */
    public AuthProvider getAuthProvider() {
        return authProvider;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password of the user, null if third party authentication
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the email of the user.
     *
     * @return the email of the user
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the ID of the user.
     *
     * @return the ID of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns the credential ID of the user.
     *
     * @return the credential ID of the user, null if local authentication
     */
    public String getCredentialId() {
        return credentialId;
    }

    /**
     * Returns the first name of the user.
     *
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the user.
     *
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the profile picture URL of the user.
     *
     * @return the profile picture URL of the user, null if local authentication
     */
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    /**
     * Returns the role of the user.
     *
     * @return the Role enum representing the user's role
     */
    public Role getRole() {
        return role;
    }

    /**
     * Returns the users gender
     *
     * @return the users gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Returns the creation date of the user.
     *
     * @return the creation date of the user
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the credential ID for the user.
     *
     * @param credentialId the credential ID to set
     */
    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    /**
     * Sets the first name of the user.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the user.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the profile picture URL of the user.
     *
     * @param profilePictureUrl the profile picture URL to set
     */
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    /**
     * Sets the role of the user.
     *
     * @param role the Role enum representing the user's role
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Sets the users gender
     *
     * @param gender the users gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Sets the password for the user.
     *
     * @param password the password to set, null if third party authentication
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the authentication provider for the user.
     *
     * @param authProvider the AuthProvider enum representing the authentication provider
     */
    public void setAuthProvider(AuthProvider authProvider) {
        this.authProvider = authProvider;
    }

}
