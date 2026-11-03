package com.talection.talection.dto.replies;

import com.talection.talection.enums.GameType;

import java.util.Date;
import java.util.Map;

/**
 * Data Transfer Object for game session replies.
 * This class encapsulates the necessary information
 * for a user's game session, including user details,
 * game metadata, and the timestamps for when the game
 * was started and ended.
 */
public class GameSessionReply {
  private Long id;
  private Long userId;
  private Long gameTemplateId;
  private GameType gameType;
  private String gameName;
  private Date startTime;
  private Date endTime;
  private Map<String, Object> score;
  
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
   * Gets the ID of the game template used in the game session.
   * 
   * @return the ID of the game template
   */
  public Long getGameTemplateId() {
    return gameTemplateId;
  }

  /**
   * Gets the type of the game session.
   * 
   * @return the type of the game session
   */
  public GameType getGameType() {
    return gameType;
  }

  /**
   * Gets the name of the game session.
   * 
   * @return the name of the game session
   */
  public String getGameName() {
    return gameName;
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
   * Sets the ID of the game session.
   *
   * @param id the ID to set for the game session
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Sets the ID of the user associated with the game session.
   *
   * @param userId the ID of the user to set for the game session
   */
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  /**
   * Sets the ID of the game template used in the game session.
   *
   * @param gameTemplateId the ID of the game template to set for the game session
   */
  public void setGameTemplateId(Long gameTemplateId) {
    this.gameTemplateId = gameTemplateId;
  }

  /**
   * Sets the type of the game session.
   *
   * @param gameType the type of the game session to set
   */
  public void setGameType(GameType gameType) {
    this.gameType = gameType;
  }

  /**
   * Sets the name of the game session.
   *
   * @param gameName the name of the game session to set
   */
  public void setGameName(String gameName) {
    this.gameName = gameName;
  }

  /**
   * Sets the start time of the game session.
   *
   * @param startTime the start time to set for the game session
   */
  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  /**
   * Sets the end time of the game session.
   *
   * @param endTime the end time to set for the game session
   */
  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  /**
   * Sets the game-specific result values of the game session.
   *
   * @param score the score values to set for the game session
   */
  public void setScore(Map<String, Object> score) {
    this.score = score;
  }
}
