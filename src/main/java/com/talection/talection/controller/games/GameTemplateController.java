package com.talection.talection.controller.games;

import com.talection.talection.enums.GameType;
import com.talection.talection.exception.GameTemplateNotFoundException;
import com.talection.talection.model.games.GameTemplate;
import com.talection.talection.service.games.GameTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/game")
public class GameTemplateController {
    private final GameTemplateService gameTemplateService;
    private final Logger logger = LoggerFactory.getLogger(GameTemplateController.class);

    public GameTemplateController(GameTemplateService gameTemplateService) {
        this.gameTemplateService = gameTemplateService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<Collection<GameTemplate>> getAllGameTemplates() {
        return ResponseEntity.ok(gameTemplateService.getAllGameTemplates());
    }

    @GetMapping("/get-all-active")
    public ResponseEntity<Collection<GameTemplate>> getAllActiveGameTemplates() {
        return ResponseEntity.ok(gameTemplateService.getAllActiveGameTemplates());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<GameTemplate> getGameTemplateById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(gameTemplateService.getGameTemplateById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (GameTemplateNotFoundException e) {
            logger.error("Game template not found: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("/get-by-game-type/{gameType}")
    public ResponseEntity<GameTemplate> getGameTemplateByType(@PathVariable GameType gameType) {
        try {
            return ResponseEntity.ok(gameTemplateService.getGameTemplateByGameType(gameType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (GameTemplateNotFoundException e) {
            logger.error("Game template not found: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addGameTemplate(@RequestBody GameTemplate gameTemplate) {
        try {
            Long id = gameTemplateService.addGameTemplate(gameTemplate);
            return ResponseEntity.ok(id.toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update-description/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateDescription(@PathVariable Long id, @RequestBody String description) {
        try {
            gameTemplateService.updateGameTemplateDescription(id, description);
            return ResponseEntity.ok("Description updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (GameTemplateNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/set-active/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> setActive(@PathVariable Long id, @RequestParam boolean active) {
        try {
            gameTemplateService.setGameTemplateActive(id, active);
            return ResponseEntity.ok("Game template updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (GameTemplateNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}