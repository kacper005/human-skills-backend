package com.talection.talection.model.userrelated;

import com.talection.talection.enums.Campus;
import com.talection.talection.enums.StudyLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a study program in the system.
 * Contains information about the program's name, campus, and study level.
 */
@Entity
public class StudyProgram {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Campus campus;

    @NotNull
    private StudyLevel studyLevel;

    /**
     * Default constructor for JPA.
     * This constructor is required for JPA to create instances of this entity.
     */
    public StudyProgram() {
        // Default constructor for JPA
    }

    /**
     * Sets the ID of the study program.
     *
     * @param id the ID to set for the study program
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the name of the study program.
     *
     * @param name the name to set for the study program
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the campus of the study program.
     *
     * @param campus the campus to set for the study program
     */
    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    /**
     * Sets the study level of the study program.
     *
     * @param studyLevel the study level to set for the study program
     */
    public void setStudyLevel(StudyLevel studyLevel) {
        this.studyLevel = studyLevel;
    }

    /**
     * Gets the ID of the study program.
     *
     * @return the ID of the study program
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the name of the study program.
     *
     * @return the name of the study program
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the campus of the study program.
     *
     * @return the campus of the study program
     */
    public Campus getCampus() {
        return campus;
    }

    /**
     * Gets the study level of the study program.
     *
     * @return the study level of the study program
     */
    public StudyLevel getStudyLevel() {
        return studyLevel;
    }
}
