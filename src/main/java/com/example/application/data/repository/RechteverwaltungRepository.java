package com.example.application.data.repository;

import com.example.application.data.entity.Rechteverwaltung;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc Repository für das Objekt Rechteverwaltung
 */
public interface RechteverwaltungRepository extends JpaRepository<Rechteverwaltung, UUID> {
}