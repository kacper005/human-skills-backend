package com.talection.talection.service.userrelated;

import com.talection.talection.dto.replies.TeacherReply;
import com.talection.talection.dto.requests.UpdateUserRequest;
import com.talection.talection.enums.AuthProvider;
import com.talection.talection.enums.Role;
import com.talection.talection.exception.UserAlreadyExistsException;
import com.talection.talection.exception.UserNotFoundException;
import com.talection.talection.model.datasharing.StudentTeacherRelation;
import com.talection.talection.model.testpersistance.TestSession;
import com.talection.talection.model.userrelated.StudentProfile;
import com.talection.talection.model.userrelated.User;
import com.talection.talection.repository.datasharing.StudentTeacherRelationRepository;
import com.talection.talection.repository.testpersistance.TestSessionRepository;
import com.talection.talection.repository.userrelated.StudentProfileRepository;
import com.talection.talection.repository.userrelated.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Service class for managing users.
 */
@Service
public class UserService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    StudentProfileRepository studentProfileRepository;
    TestSessionRepository testSessionRepository;
    StudentTeacherRelationRepository studentTeacherRelationRepository;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       StudentProfileRepository studentProfileRepository,
                       TestSessionRepository testSessionRepository,
                       StudentTeacherRelationRepository studentTeacherRelationRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.studentProfileRepository = studentProfileRepository;
        this.testSessionRepository = testSessionRepository;
        this.studentTeacherRelationRepository = studentTeacherRelationRepository;
    }

    /**
     * Adds a new user to the system.
     *
     * @param user     the user to add
     * @param password the password for the user
     * @throws UserAlreadyExistsException if a user with the same email or google ID already exists
     */
    public Long addUser(User user, String password) throws UserAlreadyExistsException {
        if (password == null || password.isEmpty() || user == null) {
            logger.error("Invalid user or password provided");
            throw new IllegalArgumentException("User and password must not be null or empty");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.error("User with email {} already exists", user.getEmail());
            throw new UserAlreadyExistsException("User with the same email or Google ID already exists");
        }

        if (user.getCredentialId() != null && userRepository.findByCredentialId(user.getCredentialId()).isPresent()) {
            logger.error("User with Google ID {} already exists", user.getCredentialId());
            throw new UserAlreadyExistsException("User with the same email or Google ID already exists");
        }

        if (user.getAuthProvider() == AuthProvider.LOCAL) {
            user.setPassword(passwordEncoder.encode(password));
        } else {
            user.setPassword(null); // No password for OAuth users
        }

        userRepository.save(user);
        return user.getId();
    }

    /**
     * Updates an existing user.
     *
     * @param request the request containing updated user information
     * @param id      the ID of the user to update
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    public void updateUser(UpdateUserRequest request, Long id) {
        if (id == null) {
            logger.error("User ID must not be null");
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (request.getFirstName() == null || request.getFirstName().isEmpty()) {
            logger.error("First name must not be null or empty");
            throw new IllegalArgumentException("First name must not be null or empty");
        }
        if (request.getLastName() == null || request.getLastName().isEmpty()) {
            logger.error("Last name must not be null or empty");
            throw new IllegalArgumentException("Last name must not be null or empty");
        }
        if (request.getGender() == null) {
            logger.error("Gender must not be null");
            throw new IllegalArgumentException("Gender must not be null");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());

        userRepository.save(user);
    }

    /**
     * Retrieves the role of a user based on their Credential ID.
     *
     * @param credentialId the Credential ID of the user
     * @return the role of the user
     * @throws IllegalArgumentException if the Google ID is null or empty
     * @throws UserNotFoundException if the user is not found with the given Credential ID
     */
    public Role getRoleByCredentialId(String credentialId) {
        if (credentialId == null || credentialId.isEmpty()) {
            logger.error("Google ID must not be null or empty");
            throw new IllegalArgumentException("Google ID must not be null or empty");
        }
        User user = userRepository.findByCredentialId(credentialId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with Google ID: " + credentialId));

        return user.getRole();
    }

    /**
     * Retrieves the role of a user based on their email.
     *
     * @param email the email of the user
     * @return the role of the user
     * @throws IllegalArgumentException if the email is null or empty
     * @throws UserNotFoundException if the user is not found with the given email
     */
    public Role getRoleByEmail(String email) {
        if (email == null || email.isEmpty()) {
            logger.error("Email must not be null or empty");
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return user.getRole();
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user
     * @return the user with the specified email
     * @throws IllegalArgumentException if the email is null or empty, or if the user is not found
     */
    public User getUserByEmail(String email) {
        if (email == null || email.isEmpty()) {
            logger.error("Email must not be null or empty");
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    /**
     * Deletes a user by their email.
     *
     * @param email the email of the user to delete
     * @throws IllegalArgumentException if the email is null or empty
     * @throws UserNotFoundException if the user is not found with the given email
     */
    public void deleteUserByEmail(String email) {
        if (email == null || email.isEmpty()) {
            logger.error("Email must not be null or empty");
            throw new IllegalArgumentException("Email must not be null or empty");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        Optional<StudentProfile> studentProfile = studentProfileRepository.findById(user.getId());
        studentProfile.ifPresent(profile -> studentProfileRepository.delete(profile));

        Collection<TestSession> sessions = testSessionRepository.findAllByUserId(user.getId());
        if (sessions != null && !sessions.isEmpty()) {
            testSessionRepository.deleteAll(sessions);
        }

        Collection<StudentTeacherRelation> relations = studentTeacherRelationRepository.findAllByStudentId(user.getId());
        if (relations != null && !relations.isEmpty()) {
            studentTeacherRelationRepository.deleteAll(relations);
        }

        userRepository.delete(user);
    }

    /**
     * Retrieves all users in the system.
     *
     * @return a collection of all users
     */
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Updates the role of a user.
     *
     * @param id   the ID of the user to update
     * @param role the new role to assign to the user
     * @throws IllegalArgumentException if the ID or role is null
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    public void updateUserRole(Long id, Role role) {
        if (id == null || role == null) {
            logger.error("ID and role must not be null");
            throw new IllegalArgumentException("ID and role must not be null");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        user.setRole(role);
        userRepository.save(user);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user
     * @return the user with the specified ID
     * @throws IllegalArgumentException if the ID is null
     * @throws UserNotFoundException if the user is not found with the given ID
     */
    public User getUserById(Long id) {
        if (id == null) {
            logger.error("ID must not be null");
            throw new IllegalArgumentException("ID must not be null");
        }
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    /**
     * Retrieves all teachers in the system.
     *
     * @return a collection of all teachers
     */
    public Collection<TeacherReply> getAllTeachers() {
        Collection<User> users = userRepository.findAllByRole(Role.TEACHER);
        if (users == null) {
            logger.error("User repository returned null");
            throw new IllegalStateException("User repository returned null");
        }
        if (users.isEmpty()) {
            logger.warn("No teachers found in the system");
            return new ArrayList<>();
        }
        ArrayList<TeacherReply> replies = new ArrayList<>();
        for (User user : users) {
            TeacherReply reply = new TeacherReply();
            reply.setTeacherId(user.getId());
            reply.setTeacherName(user.getFirstName());
            reply.setTeacherEmail(user.getEmail());
            replies.add(reply);
        }
        logger.info("Retrieved {} teachers from the system", replies.size());
        return replies;
    }
}
