package com.talection.talection.service.games;

import com.talection.talection.enums.GameType;
import com.talection.talection.exception.GameTemplateNotFoundException;
import com.talection.talection.model.games.GameTemplate;
import com.talection.talection.repository.games.GameTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class GameTemplateService {
    private final GameTemplateRepository gameTemplateRepository;

    public GameTemplateService(GameTemplateRepository gameTemplateRepository) {
        this.gameTemplateRepository = gameTemplateRepository;
    }

    public Collection<GameTemplate> getAllGameTemplates() {
        return gameTemplateRepository.findAll();
    }

    public Collection<GameTemplate> getAllActiveGameTemplates() {
        return gameTemplateRepository.findAllByActiveTrue();
    }

    public GameTemplate getGameTemplateById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return gameTemplateRepository.findById(id)
                .orElseThrow(() -> new GameTemplateNotFoundException(id));
    }

    public GameTemplate getGameTemplateByGameType(GameType gameType) {
        if (gameType == null) {
            throw new IllegalArgumentException("Game type cannot be null");
        }
        return gameTemplateRepository.findByGameType(gameType)
                .orElseThrow(() -> new GameTemplateNotFoundException("Game template not found with game type: " + gameType));
    }

    public Long addGameTemplate(GameTemplate gameTemplate) {
        if (gameTemplate == null) {
            throw new IllegalArgumentException("Game template cannot be null");
        }
        if (gameTemplate.getName() == null || gameTemplate.getName().isBlank()) {
            throw new IllegalArgumentException("Game template name cannot be null or empty");
        }
        if (gameTemplate.getGameType() == null) {
            throw new IllegalArgumentException("Game type cannot be null");
        }
        if (gameTemplate.getActive() == null) {
            gameTemplate.setActive(true);
        }

        GameTemplate saved = gameTemplateRepository.save(gameTemplate);
        return saved.getId();
    }

    public void updateGameTemplateDescription(Long id, String description) {
        if (id == null || description == null || description.isBlank()) {
            throw new IllegalArgumentException("ID and description must not be null or empty");
        }
        GameTemplate gameTemplate = gameTemplateRepository.findById(id)
                .orElseThrow(() -> new GameTemplateNotFoundException(id));

        gameTemplate.setDescription(description);
        gameTemplateRepository.save(gameTemplate);
    }

    public void setGameTemplateActive(Long id, boolean active) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        GameTemplate gameTemplate = gameTemplateRepository.findById(id)
                .orElseThrow(() -> new GameTemplateNotFoundException(id));
        gameTemplate.setActive(active);
        gameTemplateRepository.save(gameTemplate);
    }
}