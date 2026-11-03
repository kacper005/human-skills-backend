package com.talection.talection.dto.requests;

import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.Map;

public class AddGameSessionRequest {
  @NotNull
  private Long gameTemplateId;

  @NotNull
  private Date startTime;

  @NotNull
  private Date endTime;

  @NotNull
  private Map<String, Object> score;

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
  public Date getStartTime() {
    return startTime;
  }

  /**
   * Gets the end time of the game session.
   *
   * @return the end time of the game session
   */
  public Date getEndTime() {
    return endTime;
  }

  /**
   * Gets the game-specific result values of the game session.
   *
   * @return the score values of the game session
   */
  public Map<String, Object> getScore() {
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
   * @param startTime the start time of the game session to set
   */
  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  /**
   * Sets the end time of the game session.
   *
   * @param endTime the end time of the game session to set
   */
  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  /**
   * Sets the game-specific result values of the game session.
   *
   * @param score the score values of the game session to set
   */
  public void setScore(Map<String, Object> score) {
    this.score = score;
  }
}
