package com.talection.talection.dto.requests;


/**
 * DTO for adding a student profile.
 */
public class AddStudentProfileRequest {
    private int yearOfStudy;
    private Long studyProgramId;

    /**
     * Sets the ID of the study program.
     *
     * @param studyProgramId the ID to set
     */
    public void setStudyProgramId(Long studyProgramId) {
        this.studyProgramId = studyProgramId;
    }

    /**
     * Gets the ID of the study program.
     *
     * @return the ID of the study program
     */
    public Long getStudyProgramId() {
        return studyProgramId;
    }

    /**
     * Sets the year of study.
     *
     * @param yearOfStudy the year of study to set
     */
    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    /**
     * Gets the year of study.
     *
     * @return the year of study
     */
    public int getYearOfStudy() {
        return yearOfStudy;
    }
}
