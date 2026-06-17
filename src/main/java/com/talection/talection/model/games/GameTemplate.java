package com.talection.talection.model.games;

import com.talection.talection.enums.GameType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class GameTemplate {
  @Id
  @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Column(name = "game_name")
  private String name;

  private String description;

  @NotNull
  @Enumerated(EnumType.STRING)
  private GameType gameType;

  @NotNull 
  private Boolean active = true;

  /**
   * Default constructor for JPA.
   * This constructor is required for JPA to create instances of this entity.
   */
  public GameTemplate() {
    // Default constructor for JPA
  }

  /**
   * Sets the ID of the game template.
   * 
   * @param id the ID to set for the game template
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Set the name of the game template.
   * 
   * @param name the name to set for the game template
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the description of the game template.
   * 
   * @param description the description to set for the game template
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the game type of the game template.
   * 
   * @param gameType the game type to set for the game template
    *
   */
  public void setGameType(GameType gameType) {
    this.gameType = gameType;
  }

  /**
   * Set the active status of the game template.
   * 
   * @param active the active status to set for the game template
   */
  public void setActive(Boolean active) {
    this.active = active;
  }

  /**
   * Gets the ID of the game template.
   * 
   * @return the ID of the game template
   */
  public Long getId() {
    return id;
  }

  /**
   * Gets the name of the game template.
   * 
   * @return the name of the game template
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the description of the game template.
   * 
   * @return the description of the game template
   */
  public String getDescription() {
    return description;
  }

  /**
  * Gets the game type of the game template.
  * 
  * @return the game type of the game template
  */
  public GameType getGameType() {
    return gameType;
  }
  
  /**
   * Gets the active status of the game template.
   * 
   * @return the active status of the game template
   */
  public Boolean getActive() {
    return active;
  }
}
