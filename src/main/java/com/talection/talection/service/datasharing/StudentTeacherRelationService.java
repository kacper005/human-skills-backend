package com.talection.talection.service.datasharing;

import com.talection.talection.dto.replies.TeacherRelationReply;
import com.talection.talection.dto.replies.TestSessionReply;
import com.talection.talection.enums.Role;
import com.talection.talection.model.datasharing.StudentTeacherRelation;
import com.talection.talection.model.userrelated.User;
import com.talection.talection.repository.datasharing.StudentTeacherRelationRepository;
import com.talection.talection.service.testpersistance.TestSessionService;
import com.talection.talection.service.userrelated.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentTeacherRelationService {
    private final StudentTeacherRelationRepository studentTeacherRelationRepository;
    private final TestSessionService testSessionService;
    private final UserService userService;

    public StudentTeacherRelationService(StudentTeacherRelationRepository studentTeacherRelationRepository,
                                         TestSessionService testSessionService, UserService userService) {
        this.studentTeacherRelationRepository = studentTeacherRelationRepository;
        this.testSessionService = testSessionService;
        this.userService = userService;
    }

    public Collection<TeacherRelationReply> getTeachersByStudentId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }

        Collection<StudentTeacherRelation> relations = studentTeacherRelationRepository.findAllByStudentId(id);
        ArrayList<TeacherRelationReply> replies = new ArrayList<>();
        for (StudentTeacherRelation relation : relations) {
            User user = userService.getUserById(relation.getTeacherId());
            TeacherRelationReply reply = new TeacherRelationReply();
            reply.setRelationId(relation.getId());
            reply.setTeacherEmail(user.getEmail());
            reply.setTeacherName(user.getFirstName());
            replies.add(reply);
        }

        return replies;
    }

    /**
     * Retrieves all teachers associated with a given student ID and test session ID.
     *
     * @param studentId the ID of the student
     * @param testSessionId the ID of the test session
     * @return a collection of TeacherReply objects containing teacher details
     * @throws IllegalArgumentException if either studentId or testSessionId is null
     */
    public Collection<TeacherRelationReply> getTeachersByStudentIdAndSessionId(Long studentId, Long testSessionId) {
        if (studentId == null || testSessionId == null) {
            throw new IllegalArgumentException("Student ID and Test Session ID must not be null");
        }

        Collection<StudentTeacherRelation> relations = studentTeacherRelationRepository.findAllByStudentIdAndTestSessionId(studentId, testSessionId);
        ArrayList<TeacherRelationReply> replies = new ArrayList<>();
        for (StudentTeacherRelation relation : relations) {
            User user = userService.getUserById(relation.getTeacherId());
            TeacherRelationReply reply = new TeacherRelationReply();
            reply.setTeacherEmail(user.getEmail());
            reply.setTeacherName(user.getFirstName());
            reply.setRelationId(relation.getId());
            replies.add(reply);
        }

        return replies;
    }

    /**
     * Adds a new Student-Teacher relation for a given student, teacher, and test session.
     *
     * @param studentId the ID of the student
     * @param teacherId the ID of the teacher
     * @param testSessionId the ID of the test session
     * @return the ID of the created StudentTeacherRelation
     * @throws IllegalArgumentException if any of the IDs are null or if the test session does not exist
     */
    public Long addStudentTeacherRelation(Long studentId, Long teacherId, Long testSessionId) {
        if (studentId == null || teacherId == null || testSessionId == null) {
            throw new IllegalArgumentException("Student ID, Teacher ID, and Test Session ID must not be null");
        }

        // Check if the test session exists
        TestSessionReply sessionReply = testSessionService.getTestSessionReplyById(testSessionId);
        if (!studentId.equals(sessionReply.getUserId())) {
            throw new IllegalArgumentException("Test session with ID " + testSessionId + " does not belong to student with ID " + studentId);
        }

        // Check if the student and teacher exist
        if (userService.getUserById(studentId) == null || userService.getUserById(teacherId) == null) {
            throw new IllegalArgumentException("Student or Teacher with the provided ID does not exist");
        }

        if (userService.getUserById(teacherId).getRole() != Role.TEACHER) {
            throw new IllegalArgumentException("User with ID " + teacherId + " is not a teacher");
        }

        if (studentTeacherRelationRepository.findByStudentIdAndTeacherIdAndTestSessionId(studentId, teacherId, testSessionId) != null) {
            throw new IllegalArgumentException("Relation between student and teacher already exists");
        }

        // Create and save the relation
        var relation = new com.talection.talection.model.datasharing.StudentTeacherRelation();
        relation.setStudentId(studentId);
        relation.setTeacherId(teacherId);
        relation.setTestSessionId(testSessionId);

        return studentTeacherRelationRepository.save(relation).getId();
    }

    /**
     * Retrieves all test sessions associated with a given teacher ID.
     *
     * @param userId the ID of the teacher
     * @return a collection of TestSessionReply objects associated with the teacher
     */
    public Collection<TestSessionReply> getTestSessionsByTeacherIdOrStudentId(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("Teacher ID must not be null");
        }

        return studentTeacherRelationRepository.findAllByStudentIdOrTeacherId(userId).stream()
            .map(relation -> testSessionService.getTestSessionReplyById(relation.getTestSessionId()))
            .filter(Objects::nonNull)
            .toList();
    }

    /**
     * Retrieves all test sessions associated with a given student ID.
     *
     * @param studentId the ID of the student
     * @return a collection of TestSessionReply objects associated with the student
     * @throws IllegalArgumentException if the student ID is null
     */
    public Collection<TestSessionReply> getTestSessionsByStudentId(Long studentId) {
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID must not be null");
        }

        return studentTeacherRelationRepository.findAllByStudentId(studentId).stream()
            .map(relation -> testSessionService.getTestSessionReplyById(relation.getTestSessionId()))
            .filter(Objects::nonNull)
            .toList();
    }

    /**
     * Retrieves a TestSessionReply by its ID and the associated student ID.
     *
     * @param testSessionId the ID of the test session
     * @param studentId the ID of the student
     * @return the TestSessionReply object if found
     * @throws IllegalArgumentException if either ID is null or if no relation is found
     */
    public TestSessionReply getTestSessionByIdAndTeacherId(Long testSessionId, Long studentId) {
        if (testSessionId == null || studentId == null) {
            throw new IllegalArgumentException("Test Session ID and Student ID must not be null");
        }

        Collection<StudentTeacherRelation> relations = studentTeacherRelationRepository.findAllByTeacherIdAndTestSessionId(studentId, testSessionId);
        if (relations.isEmpty()) {
            throw new IllegalArgumentException("No relation found for the given Student ID and Test Session ID");
        }

        return testSessionService.getTestSessionReplyById(testSessionId);
    }

    /**
     * Deletes a Student-Teacher relation based on the provided student ID and teacher ID.
     *
     * @param studentId the ID of the student
     * @param relationId the ID of the teacher
     * @throws IllegalArgumentException if either ID is null or if no relation is found
     */
    public void deleteStudentTeacherRelation(Long studentId, Long relationId) {
        if (studentId == null || relationId == null) {
            throw new IllegalArgumentException("Student ID and Teacher ID must not be null");
        }

        Optional<StudentTeacherRelation> relation = studentTeacherRelationRepository.findById(relationId);
        if (relation.isEmpty() || !relation.get().getStudentId().equals(studentId)) {
            throw new IllegalArgumentException("No relation found for the given relation id");
        }

        studentTeacherRelationRepository.delete(relation.get());
    }
}
