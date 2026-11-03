package com.talection.talection.devtools;

import com.talection.talection.enums.GameType;
import com.talection.talection.model.games.GameTemplate;
import com.talection.talection.repository.games.GameTemplateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Seeds one game template per game type, so that game sessions can be saved
 * without an admin having to create the templates manually.
 */
@Component
@Profile("dev")
public class GameTemplateInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(GameTemplateInitializer.class);

    private record TemplateDef(String name, String description) {}

    private static final Map<GameType, TemplateDef> DEFAULT_TEMPLATES = Map.of(
        GameType.ATTENTION, new TemplateDef("Attention",
            "Identify the direction of the central shuttle in a group of space shuttles"),
        GameType.BALLOON, new TemplateDef("Balloon",
            "Inflate a number of balloons to the maximum to collect as many points as possible"),
        GameType.COGFLEX, new TemplateDef("CogFlex",
            "Identify pattern, apply pattern and detect pattern changes")
    );

    private final GameTemplateRepository gameTemplateRepository;

    public GameTemplateInitializer(GameTemplateRepository gameTemplateRepository) {
        this.gameTemplateRepository = gameTemplateRepository;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        DEFAULT_TEMPLATES.forEach((gameType, def) -> {
            if (gameTemplateRepository.findByGameType(gameType).isPresent()) {
                return;
            }

            GameTemplate template = new GameTemplate();
            template.setGameType(gameType);
            template.setName(def.name());
            template.setDescription(def.description());
            template.setActive(true);
            gameTemplateRepository.save(template);
            logger.info("Game template initialized for game type: {}", gameType);
        });
    }
}
