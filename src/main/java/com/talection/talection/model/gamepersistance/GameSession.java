package com.talection.talection.model.gamepersistance;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.Map;

@Entity
public class GameSession {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private Long userId;

  @NotNull
  private Long gameTemplateId;

  @NotNull
  private Date startedAt;

  @NotNull
  private Date completedAt;

  // Game-specific result values, e.g. points and accuracy
  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private Map<String, Object> score;

  // Getters and setters

  /**
   * Gets the ID of the game session.
   * 
   * @return the ID of the game session
   */
  public Long getId() {
    return id;
  }

  /**
   * Gets the ID of the user associated with the game session.
   * 
   * @return the ID of the user
   */
  public Long getUserId() {
    return userId;
  }

  /**
   * Gets the start date of the game session.
   * 
   * @return the start date of the game session
   */
  public Date getStartedAt() {
    return startedAt;
  }

  /**
   * Gets the completion date of the game session.
   * 
   * @return the completion date of the game session
   */
  public Date getCompletedAt() {
    return completedAt;
  }

  /**
   * Gets the score of the game session.
   * 
   * @return the score of the game session
   */
  public Map<String, Object> getScore() {
    return score;
  }

  /**
   * Gets the ID of the game template associated with the game session.
   * 
   * @return the ID of the game template
   */
  public Long getGameTemplateId() {
    return gameTemplateId;
  }

  /**
   * Sets the ID of the game session.
   * 
   * @param id the ID of the game session
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Sets the ID of the user associated with the game session.
   * 
   * @param userId the ID of the user
   */
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  /**
   * Sets the start date of the game session.
   * 
   * @param startedAt the start date of the game session
   */
  public void setStartedAt(Date startedAt) {
    this.startedAt = startedAt;
  }

  /**
   * Sets the completion date of the game session.
   * 
   * @param completedAt the completion date of the game session
   */
  public void setCompletedAt(Date completedAt) {
    this.completedAt = completedAt;
  }

  /**
   * Sets the score of the game session.
   * 
   * @param score the score of the game session
   */
  public void setScore(Map<String, Object> score) {
    this.score = score;
  }

  /**
   * Sets the ID of the game template associated with the game session.
   * 
   * @param gameTemplateId the ID of the game template
   */
  public void setGameTemplateId(Long gameTemplateId) {
    this.gameTemplateId = gameTemplateId;
  }
}
