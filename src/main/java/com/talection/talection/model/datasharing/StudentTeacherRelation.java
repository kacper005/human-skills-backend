package com.talection.talection.model.datasharing;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class StudentTeacherRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long studentId;

    @NotNull
    private Long teacherId;

    @NotNull
    private Long testSessionId;

    /**
     * Default constructor for JPA.
     * This constructor is required for JPA to create instances of this entity.
     */
    public StudentTeacherRelation() {
        // Default constructor for JPA
    }

    /**
     * Sets the ID of the relation.
     *
     * @param id the ID to set for the relation
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the ID of the relation.
     *
     * @return the ID of the relation
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the student ID for the relation.
     *
     * @param studentId the student ID to set
     */
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    /**
     * Gets the student ID for the relation.
     *
     * @return the student ID
     */
    public Long getStudentId() {
        return studentId;
    }

    /**
     * Sets the teacher ID for the relation.
     *
     * @param teacherId the teacher ID to set
     */
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    /**
     * Gets the teacher ID for the relation.
     *
     * @return the teacher ID
     */
    public Long getTeacherId() {
        return teacherId;
    }

    /**
     * Sets the test session ID for the relation.
     *
     * @param testSessionId the test session ID to set
     */
    public void setTestSessionId(Long testSessionId) {
        this.testSessionId = testSessionId;
    }

    /**
     * Gets the test session ID for the relation.
     *
     * @return the test session ID
     */
    public Long getTestSessionId() {
        return testSessionId;
    }
}
