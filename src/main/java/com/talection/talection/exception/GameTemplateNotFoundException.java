package com.talection.talection.exception;

public class GameTemplateNotFoundException extends RuntimeException {
  public GameTemplateNotFoundException(String message) {
    super(message);
  }

  public GameTemplateNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public GameTemplateNotFoundException(Long id) {
    super("Game template not found with ID: " + id);
  }
}