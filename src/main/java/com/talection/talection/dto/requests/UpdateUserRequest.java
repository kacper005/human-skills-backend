package com.talection.talection.dto.requests;

import com.talection.talection.enums.Gender;

/**
 * Data Transfer Object for updating user information.
 * This class encapsulates the necessary information
 * for updating an existing user's profile in the application.
 */
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private Gender gender;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Gender getGender() {
        return this.gender;
    }
}
