package com.talection.talection.repository.gamepersistance;

import com.talection.talection.model.gamepersistance.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
  Collection<GameSession> findAllByUserId(Long userId);
}
