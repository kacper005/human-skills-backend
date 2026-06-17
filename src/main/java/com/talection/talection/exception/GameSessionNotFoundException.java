package com.talection.talection.exception;

public class GameSessionNotFoundException extends RuntimeException {
  public GameSessionNotFoundException(String message) {
    super(message);    
  }

  public GameSessionNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public GameSessionNotFoundException(Long id) {
    super("Game session not found with ID: " + id);
  }
}