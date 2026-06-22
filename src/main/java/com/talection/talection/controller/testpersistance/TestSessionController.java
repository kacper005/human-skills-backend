package com.talection.talection.controller.testpersistance;

import com.talection.talection.dto.replies.TestSessionReply;
import com.talection.talection.exception.TestSessionNotFoundException;
import com.talection.talection.exception.TestTemplateNotFoundException;
import com.talection.talection.exception.UserNotFoundException;
import com.talection.talection.model.testpersistance.TestSession;
import com.talection.talection.security.AccessUserDetails;
import com.talection.talection.service.testpersistance.TestSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Controller for managing test sessions.
 */
@RestController
@RequestMapping("/testsessions")
public class TestSessionController {
    private final Logger logger = LoggerFactory.getLogger(TestSessionController.class);

    private final TestSessionService testSessionService;

    public TestSessionController(TestSessionService testSessionService) {
        this.testSessionService = testSessionService;
    }

    /**
     * Endpoint to retrieve all test sessions for the current user.
     *
     * @return ResponseEntity containing a collection of test sessions
     */
    @GetMapping()
    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<Collection<TestSession>> getAllTestSessionsForCurrentUser() {
        AccessUserDetails userDetails = (AccessUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Collection<TestSession> testSessions = testSessionService.findAllTestSessionsForUser(userDetails.getId());
            logger.info("Retrieved {} test sessions for user with ID {}", testSessions.size(), userDetails.getId());
            return ResponseEntity.ok(testSessions);
        } catch (IllegalArgumentException e) {
            logger.error("Error retrieving test sessions for user with ID {}: {}", userDetails.getId(), e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException e) {
            logger.error("User not found with ID {}: {}", userDetails.getId(), e.getMessage());
            return ResponseEntity.status(404).body(null);
        }
    }

    /**
     * Endpoint to retrieve all test sessions for the current user, formatted for a specific view.
     *
     * @return ResponseEntity containing a collection of formatted test sessions
     */
    @GetMapping("/formatted")
    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<Collection<TestSessionReply>> getAllFormattedTestSessionsForCurrentUser() {
        AccessUserDetails userDetails = (AccessUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Collection<TestSessionReply> testSessions = testSessionService.getAllTestSessionsForUserFormatted(userDetails.getId());
            logger.info("Retrieved {} formatted test sessions for user with ID {}", testSessions.size(), userDetails.getId());
            return ResponseEntity.ok(testSessions);
        } catch (IllegalArgumentException e) {
            logger.error("Error retrieving formatted test sessions for user with ID {}: {}", userDetails.getId(), e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException e) {
            logger.error("User not found with ID {}: {}", userDetails.getId(), e.getMessage());
            return ResponseEntity.status(404).body(null);
        }
    }

    /**
     * Open endpoint for unauthorized users to evaluate a test session.
     *
     * @param testSession the test session to evaluate
     * @return ResponseEntity containing the evaluation result
     */
    @PostMapping("/evaluate")
    public ResponseEntity<TestSessionReply> evaluateTestSession(@RequestBody TestSession testSession) {
        try {
            TestSessionReply reply = testSessionService.evaluateTestSession(testSession);
            logger.info("Test session evaluated successfully with ID {}", reply.getId());
            return ResponseEntity.ok(reply);
        } catch (IllegalArgumentException e) {
            logger.error("Error evaluating test session: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (TestTemplateNotFoundException e) {
            logger.error("Test template not found for evaluation: {}", e.getMessage());
            return ResponseEntity.status(404).body(null);
        }
    }

    /**
     * Endpoint to add a new test session for the current user.
     *
     * @param testSession the test session to add
     * @return ResponseEntity indicating success or failure
     */
    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<String> addTestSessionForCurrentUser(@RequestBody TestSession testSession) {
        AccessUserDetails userDetails = (AccessUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        testSession.setUserId(userDetails.getId());
        try {
            Long id = testSessionService.addTestSession(testSession);
            logger.info("Test session added successfully with ID {}", id);
            return ResponseEntity.ok(id.toString());
        } catch (IllegalArgumentException e) {
            logger.error("Error adding test session for user with ID {}: {}", userDetails.getId(), e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNotFoundException e) {
            logger.error("User not found with ID {}: {}", userDetails.getId(), e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (TestTemplateNotFoundException e) {
            logger.error("Test template not found for user with ID {}: {}", userDetails.getId(), e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    /**
     * Endpoint to retrieve a test session by its ID, formatted for a specific view.
     * The endpoint checks if the test session belongs to the current user before returning it.
     *
     * @param id the ID of the test session
     * @return ResponseEntity containing the formatted test session or an error status
     */
    @GetMapping("/formatted/{id}")
    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<TestSessionReply> getTestSessionReplyById(@PathVariable Long id) {
        AccessUserDetails userDetails = (AccessUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            TestSessionReply testSessionReply = testSessionService.getTestSessionReplyById(id);
            if (Objects.equals(testSessionReply.getUserId(), userDetails.getId())) { // Check if the test session belongs to the user
                return ResponseEntity.ok(testSessionReply);
            } else {
                logger.warn("User with ID {} attempted to access test session with ID {} that does not belong to them", userDetails.getId(), id);
                return ResponseEntity.status(403).build();
            }
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching test session with ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (TestTemplateNotFoundException e) {
            logger.error("Test template not found for test session with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * Deletes a test session by its ID.
     *
     * @param id the ID of the test session to delete
     * @return ResponseEntity indicating success or failure
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTestSessionById(@PathVariable Long id) {
        AccessUserDetails userDetails = (AccessUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            TestSessionReply reply = testSessionService.getTestSessionReplyById(id);
            if (!Objects.equals(reply.getUserId(), userDetails.getId())) {
                logger.warn("Cannot test session with ID {}: it does not belong to the user with ID {}", id, userDetails.getId());
                return ResponseEntity.status(403).body("Cannot delete test session you do not own");
            }
            testSessionService.deleteTestSession(id);
            return ResponseEntity.ok("Test session deleted successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting test session with ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TestSessionNotFoundException e) {
            logger.error("Test session not found with ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
