package com.example.application.data.repository;

import com.example.application.data.entity.Abteilung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @desc Repository für das Objekt Abteilung
 */
public interface AbteilungRepository extends JpaRepository<Abteilung, UUID> {
}
