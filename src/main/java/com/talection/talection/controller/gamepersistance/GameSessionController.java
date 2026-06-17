package com.talection.talection.controller.gamepersistance;

import com.talection.talection.dto.replies.GameSessionReply;
import com.talection.talection.dto.requests.AddGameSessionRequest;
import com.talection.talection.exception.GameSessionNotFoundException;
import com.talection.talection.exception.GameTemplateNotFoundException;
import com.talection.talection.exception.UserNotFoundException;
import com.talection.talection.model.gamepersistance.GameSession;
import com.talection.talection.security.AccessUserDetails;
import com.talection.talection.service.gamepersistance.GameSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("/gamesessions")
public class GameSessionController {
    private final GameSessionService gameSessionService;
    private final Logger logger = LoggerFactory.getLogger(GameSessionController.class);

    public GameSessionController(GameSessionService gameSessionService) {
        this.gameSessionService = gameSessionService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<Collection<GameSession>> getAllGameSessionsForCurrentUser() {
        AccessUserDetails userDetails = (AccessUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            return ResponseEntity.ok(gameSessionService.getAllGameSessionsForUser(userDetails.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<String> addGameSession(@RequestBody AddGameSessionRequest request) {
        AccessUserDetails userDetails = (AccessUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            Long id = gameSessionService.addGameSession(userDetails.getId(), request);
            return ResponseEntity.ok(id.toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNotFoundException | GameTemplateNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<GameSessionReply> getGameSessionById(@PathVariable Long id) {
        AccessUserDetails userDetails = (AccessUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            GameSessionReply reply = gameSessionService.getGameSessionById(id);
            if (!Objects.equals(reply.getUserId(), userDetails.getId())) {
                return ResponseEntity.status(403).build();
            }
            return ResponseEntity.ok(reply);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (GameSessionNotFoundException | GameTemplateNotFoundException e) {
            logger.error("Error retrieving game session: {}", e.getMessage());
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('STUDENT', 'TEACHER', 'ADMIN')")
    public ResponseEntity<String> deleteGameSession(@PathVariable Long id) {
        AccessUserDetails userDetails = (AccessUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            GameSessionReply reply = gameSessionService.getGameSessionById(id);
            if (!Objects.equals(reply.getUserId(), userDetails.getId())) {
                return ResponseEntity.status(403).body("Cannot delete game session you do not own");
            }
            gameSessionService.deleteGameSession(id);
            return ResponseEntity.ok("Game session deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (GameSessionNotFoundException | GameTemplateNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}