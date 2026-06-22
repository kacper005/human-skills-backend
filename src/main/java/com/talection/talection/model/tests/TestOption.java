package com.talection.talection.model.tests;

import com.talection.talection.enums.TestOptionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

/**
 * Represents an option in a test.
 * This entity stores information about the type of the option,
 * the text of the option, and the level of agreement associated with it.
 */
@Entity
public class TestOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private TestOptionType type;

    private String optionText;

    private int agreementLevel;

    /**
     * Default constructor for JPA.
     * This constructor is required for JPA to create instances of this entity.
     */
    public TestOption() {
        // Default constructor for JPA
    }

    /**
     * Gets the ID of the test option.
     *
     * @return the ID of the test option
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the test option.
     *
     * @param id the ID to set for the test option
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the type of the test option.
     *
     * @return the type of the test option
     */
    public TestOptionType getType() {
        return type;
    }

    /**
     * Sets the type of the test option.
     *
     * @param type the type to set for the test option
     */
    public void setType(TestOptionType type) {
        this.type = type;
    }

    /**
     * Gets the text of the test option.
     *
     * @return the text of the test option
     */
    public String getOptionText() {
        return optionText;
    }

    /**
     * Sets the text of the test option.
     *
     * @param optionText the text to set for the test option
     */
    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    /**
     * Gets the level of agreement associated with the test option.
     *
     * @return the level of agreement
     */
    public int getAgreementLevel() {
        return agreementLevel;
    }

    /**
     * Sets the level of agreement associated with the test option.
     *
     * @param agreementLevel the level of agreement to set
     */
    public void setAgreementLevel(int agreementLevel) {
        this.agreementLevel = agreementLevel;
    }
}