package com.talection.talection.service.gamepersistance;

import com.talection.talection.dto.replies.GameSessionReply;
import com.talection.talection.dto.requests.AddGameSessionRequest;
import com.talection.talection.exception.GameSessionNotFoundException;
import com.talection.talection.exception.GameTemplateNotFoundException;
import com.talection.talection.model.gamepersistance.GameSession;
import com.talection.talection.model.games.GameTemplate;
import com.talection.talection.repository.gamepersistance.GameSessionRepository;
import com.talection.talection.repository.games.GameTemplateRepository;
import com.talection.talection.service.userrelated.UserService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class GameSessionService {
    private final GameSessionRepository gameSessionRepository;
    private final GameTemplateRepository gameTemplateRepository;
    private final UserService userService;

    public GameSessionService(GameSessionRepository gameSessionRepository,
                              GameTemplateRepository gameTemplateRepository,
                              UserService userService) {
        this.gameSessionRepository = gameSessionRepository;
        this.gameTemplateRepository = gameTemplateRepository;
        this.userService = userService;
    }

    public Long addGameSession(Long userId, AddGameSessionRequest request) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Request must not be null");
        }
        if (request.getGameTemplateId() == null) {
            throw new IllegalArgumentException("Game template ID must not be null");
        }
        if (request.getStartedAt() == null || request.getEndedAt() == null) {
            throw new IllegalArgumentException("Start and end time must not be null");
        }
        if (request.getScore() == null) {
            throw new IllegalArgumentException("Score must not be null");
        }

        userService.getUserById(userId);

        GameTemplate gameTemplate = gameTemplateRepository.findById(request.getGameTemplateId())
                .orElseThrow(() -> new GameTemplateNotFoundException(request.getGameTemplateId()));

        if (!Boolean.TRUE.equals(gameTemplate.getActive())) {
            throw new IllegalArgumentException("Game template is inactive");
        }

        GameSession gameSession = new GameSession();
        gameSession.setUserId(userId);
        gameSession.setGameTemplateId(request.getGameTemplateId());
        gameSession.setStartedAt(request.getStartedAt());
        gameSession.setCompletedAt(request.getEndedAt());
        gameSession.setScore(request.getScore());

        return gameSessionRepository.save(gameSession).getId();
    }

    public Collection<GameSessionReply> getAllGameSessionsForUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        userService.getUserById(userId);
        return gameSessionRepository.findAllByUserId(userId).stream()
                .map(this::toReply)
                .toList();
    }

    public GameSessionReply getGameSessionById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Session ID must not be null");
        }

        GameSession gameSession = gameSessionRepository.findById(id)
                .orElseThrow(() -> new GameSessionNotFoundException(id));

        return toReply(gameSession);
    }

    public void deleteGameSession(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Session ID must not be null");
        }

        GameSession gameSession = gameSessionRepository.findById(id)
                .orElseThrow(() -> new GameSessionNotFoundException(id));

        gameSessionRepository.delete(gameSession);
    }

    private GameSessionReply toReply(GameSession gameSession) {
        GameTemplate gameTemplate = gameTemplateRepository.findById(gameSession.getGameTemplateId())
                .orElseThrow(() -> new GameTemplateNotFoundException(gameSession.getGameTemplateId()));

        GameSessionReply reply = new GameSessionReply();
        reply.setId(gameSession.getId());
        reply.setUserId(gameSession.getUserId());
        reply.setGameTemplateId(gameSession.getGameTemplateId());
        reply.setGameName(gameTemplate.getName());
        reply.setGameType(gameTemplate.getGameType());
        reply.setStartedAt(gameSession.getStartedAt());
        reply.setEndedAt(gameSession.getCompletedAt());
        reply.setScore(gameSession.getScore());

        return reply;
    }
}