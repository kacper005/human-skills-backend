package com.talection.talection.repository.games;

import com.talection.talection.enums.GameType;
import com.talection.talection.model.games.GameTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface GameTemplateRepository extends JpaRepository<GameTemplate, Long> {
    Optional<GameTemplate> findByGameType(GameType gameType);

    Collection<GameTemplate> findAllByActiveTrue();
}