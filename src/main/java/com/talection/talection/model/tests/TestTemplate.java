package com.talection.talection.model.tests;

import com.talection.talection.enums.TestOptionType;
import com.talection.talection.enums.TestType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Represents a template for a test.
 * This entity stores the name, description, type of the test,
 * the options available for the test, and the questions included in the test.
 */
@Entity
public class TestTemplate {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(length = 1500)
    private String description;

    @NotNull
    @Column(unique = true)
    private TestType testType;

    @NotNull
    private TestOptionType optionType;

    @ManyToMany
    private List<TestQuestion> questions;

    /**
     * Default constructor for JPA.
     * This constructor is required for JPA to create instances of this entity.
     */
    public TestTemplate() {
        // Default constructor for JPA
    }

    /**
     * Sets the ID of the test template.
     *
     * @param id the ID to set for the test template
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the name of the test template.
     *
     * @param name the name to set for the test template
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the ID of the test template.
     *
     * @return the ID of the test template
     */
    public Long getId() {
        return id;
    }

    /**
     * Gets the name of the test template.
     *
     * @return the name of the test template
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the type of the test template.
     *
     * @param testType the type to set for the test template
     */
    public void setTestType(TestType testType) {
        this.testType = testType;
    }

    /**
     * Gets the type of the test template.
     *
     * @return the type of the test template
     */
    public TestType getTestType() {
        return testType;
    }

    /**
     * Sets the questions included in the test template.
     *
     * @param questions the list of questions to set for the test template
     */
    public void setQuestions(List<TestQuestion> questions) {
        this.questions = questions;
    }

    /**
     * Gets the questions included in the test template.
     *
     * @return the list of questions for the test template
     */
    public List<TestQuestion> getQuestions() {
        return questions;
    }

    /**
     * Sets the description of the test template.
     *
     * @param description the description to set for the test template
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the description of the test template.
     *
     * @return the description of the test template
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the option type for the test template.
     *
     * @param optionType the option type to set for the test template
     */
    public void setOptionType(TestOptionType optionType) {
        this.optionType = optionType;
    }

    /**
     * Gets the option type for the test template.
     *
     * @return the option type of the test template
     */
    public TestOptionType getOptionType() {
        return optionType;
    }
}
