package com.talection.talection.controller.userrelated;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.talection.talection.config.GoogleTokenVerifier;
import com.talection.talection.dto.replies.TeacherReply;
import com.talection.talection.dto.requests.SignUpRequest;
import com.talection.talection.dto.requests.UpdateUserRequest;
import com.talection.talection.enums.AuthProvider;
import com.talection.talection.enums.Role;
import com.talection.talection.exception.UserAlreadyExistsException;
import com.talection.talection.exception.UserNotFoundException;
import com.talection.talection.model.userrelated.User;
import com.talection.talection.security.AccessUserDetails;
import com.talection.talection.service.userrelated.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Objects;


@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    private final GoogleTokenVerifier googleTokenVerifier;

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, GoogleTokenVerifier googleTokenVerifier) {
        this.userService = userService;
        this.googleTokenVerifier = googleTokenVerifier;
    }

    /**
     * Endpoint to add a new user.
     *
     * @param signUpRequest the request containing user details
     * @return ResponseEntity indicating success or failure
     */
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody SignUpRequest signUpRequest) {
        if (signUpRequest == null) {
            return ResponseEntity.badRequest().body("Sign-up request cannot be null");
        }
        if (signUpRequest.getRole() == Role.ADMIN) {
            return ResponseEntity.status(401).body("Unauthorized: Admin role cannot be used for sign-up");
        }
        if (signUpRequest.getRole() != Role.STUDENT && signUpRequest.getRole() != Role.TEACHER) {
            return ResponseEntity.badRequest().body("Invalid role for sign-up");
        }
        if (signUpRequest.getAuthProvider() == null) {
            return ResponseEntity.badRequest().body("Authentication provider must not be null");
        }

        try {
            return switch (signUpRequest.getAuthProvider()) {
                case LOCAL -> ResponseEntity.ok(addLocalUser(signUpRequest).toString());
                case GOOGLE -> ResponseEntity.ok(addGoogleUser(signUpRequest).toString());
                case FEIDE -> ResponseEntity.status(501).body("FEIDE authentication is not implemented yet");
            };
        } catch (UserAlreadyExistsException e) {
            logger.error("User already exists with email: {}", signUpRequest.getEmail());
            return ResponseEntity.status(409).body("User already exists with this email");
        } catch (IllegalArgumentException e) {
            logger.error("Invalid sign-up request: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (GeneralSecurityException | IOException e) {
            logger.error("Error verifying Google ID token: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid Google ID token");
        }
    }

    private Long addLocalUser(SignUpRequest request) throws UserAlreadyExistsException {
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required for local sign-up");
        }

        User user = buildBaseUser(request);
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setCredentialId(null); // No credential ID for local users

        return userService.addUser(user, request.getPassword());
    }

    private Long addGoogleUser(SignUpRequest request)
            throws UserAlreadyExistsException, GeneralSecurityException, IOException {
                if (request.getIdToken() == null || request.getIdToken().isBlank()) {
                    throw new IllegalArgumentException("ID token is required for Google sign-up");
                }

                GoogleIdToken.Payload payload = googleTokenVerifier.verifyToken(request.getIdToken());

                User user = buildBaseUser(request);
                user.setAuthProvider(AuthProvider.GOOGLE);
                user.setCredentialId(payload.getSubject()); // Use Google user ID as credential ID

                if (user.getEmail() == null || user.getEmail().isBlank()) {
                    user.setEmail(payload.getEmail()); // Fallback to email from token if not provided in request
                }

                return userService.addUser(user, null); // No password for Google users
            }

            private User buildBaseUser(SignUpRequest request) {
                User user = new User();
                user.setRole(request.getRole());
                user.setFirstName(request.getFirstName());
                user.setLastName(request.getLastName());
                user.setEmail(request.getEmail());
                user.setGender(request.getGender());
                return user;
            }
    /**
     * Endpoint to retrieve the current user's details.
     *
     * @return ResponseEntity containing the current user's details or an error status
     */
    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER', 'ADMIN')")
    @GetMapping("/get-me")
    public ResponseEntity<User> getCurrentUser() {
        AccessUserDetails userDetails = (AccessUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            return ResponseEntity.ok(userService.getUserByEmail(userDetails.getUsername()));
        } catch (IllegalArgumentException e) {
            logger.error("Error retrieving user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * Endpoint to update the current user's details.
     *
     * @param request the request containing updated user details
     * @return ResponseEntity indicating success or failure
     */
    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER', 'ADMIN')")
    @PutMapping("/update-me")
    public ResponseEntity<String> updateCurrentUser(@RequestBody UpdateUserRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body("Update request cannot be null");
        }
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            return ResponseEntity.badRequest().body("First name must not be null or empty");
        }
        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            return ResponseEntity.badRequest().body("Last name must not be null or empty");
        }
        if (request.getGender() == null) {
            return ResponseEntity.badRequest().body("Gender must not be null");
        }

        AccessUserDetails userDetails = (AccessUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try {
            userService.updateUser(request, userDetails.getId());
            logger.info("User updated successfully with id: {}", userDetails.getId());
            return ResponseEntity.ok("User updated successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Error updating user: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid update request");
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(404).body("User not found");
        }
    }

    /**
     * Endpoint to delete the current user.
     *
     * @return ResponseEntity indicating success or failure
     */
    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER', 'ADMIN')")
    @DeleteMapping("/delete-me")
    public ResponseEntity<String> deleteCurrentUser() {
        AccessUserDetails userDetails = (AccessUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            userService.deleteUserByEmail(userDetails.getUsername());
            return ResponseEntity.ok("User deleted successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting user: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid user deletion request");
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(404).body("User not found");
        }
    }

    /**
     * Endpoint to retrieve all users. This endpoint is restricted to users with ADMIN authority.
     *
     * @return ResponseEntity containing a collection of users or an error status
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<Collection<User>> getAllUsers() {
        try {
            Collection<User> users = userService.getAllUsers();
            logger.info("Retrieved all users successfully");
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error retrieving users: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER', 'ADMIN')")
    @GetMapping("/teachers")
    public ResponseEntity<Collection<TeacherReply>> getAllTeachers() {
        try {
            Collection<TeacherReply> teachers = userService.getAllTeachers();
            logger.info("Retrieved all teachers successfully");
            return ResponseEntity.ok(teachers);
        } catch (IllegalArgumentException e) {
            logger.error("Error retrieving teachers: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving teachers: {}", e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Endpoint to update a user's role. This endpoint is restricted to users with ADMIN authority.
     *
     * @param id the ID of the user to update
     * @param role the new role to assign to the user
     * @return ResponseEntity indicating success or failure
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/update-role/{id}")
    public ResponseEntity<String> updateRole(@PathVariable Long id, @RequestBody Role role) {
        if (id == null || role == null) {
            return ResponseEntity.badRequest().body("ID and role must not be null");
        }
        AccessUserDetails userDetails = (AccessUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (Objects.equals(userDetails.getId(), id)) {
            return ResponseEntity.badRequest().body("Cannot update your own role");
        }

        try {
            userService.updateUserRole(id, role);
            logger.info("User role updated successfully for user ID: {}", id);
            return ResponseEntity.ok("User role updated successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Error updating user role: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid user ID or role");
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(404).body("User not found");
        }
    }
}