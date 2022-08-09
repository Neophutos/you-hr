package com.youhr.application.data.repository;

import com.youhr.application.data.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @desc Repository für das Objekt Team
 */
public interface TeamRepository extends JpaRepository<Team, UUID> {
}
