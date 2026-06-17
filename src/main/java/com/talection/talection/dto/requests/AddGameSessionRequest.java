package com.talection.talection.dto.requests;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public class AddGameSessionRequest {
  @NotNull
  private Long gameTemplateId;
  
  @NotNull
  private Date startedAt;

  @NotNull
  private Date endedAt;

  @NotNull
  private Long playerId;

  private String score;

  /**
   * Gets the game template ID.
   * 
   * @return the game template ID
   */
  public Long getGameTemplateId() {
    return gameTemplateId;
  }

  /**
   * Gets the start time of the game session.
   * 
   * @return the start time of the game session
   */
  public Date getStartedAt() {
    return startedAt;
  }

  /**
   * Gets the end time of the game session.
   * 
   * @return the end time of the game session
   */
  public Date getEndedAt() {
    return endedAt;
  }

  /**
   * Gets the player ID.
   * 
   * @return the player ID
   */
  public Long getPlayerId() {
    return playerId;
  }

  /**
   * Gets the score of the game session.
   * 
   * @return the score of the game session
   */
  public String getScore() {
    return score;
  }

  /**
   * Sets the game template ID.
   * 
   * @param gameTemplateId the game template ID to set
   */
  public void setGameTemplateId(Long gameTemplateId) {
    this.gameTemplateId = gameTemplateId;
  }

  /**
   * Sets the start time of the game session.
   * 
   * @param startedAt the start time of the game session to set
   */
  public void setStartedAt(Date startedAt) {
    this.startedAt = startedAt;
  }

  /**
   * Sets the end time of the game session.
   * 
   * @param endedAt the end time of the game session to set
   */
  public void setEndedAt(Date endedAt) {
    this.endedAt = endedAt;
  }

  /**
   * Sets the player ID.
   * 
   * @param playerId the player ID to set
   */
  public void setPlayerId(Long playerId) {
    this.playerId = playerId;
  }

  /**
   * Sets the score of the game session.
   * 
   * @param score the score of the game session to set
   */
  public void setScore(String score) {
    this.score = score;
  }
}
