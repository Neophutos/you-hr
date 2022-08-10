package com.youhr.application.data.repository;

import com.youhr.application.data.objekt.Abteilung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @desc Repository für das Objekt Abteilung
 */
public interface AbteilungRepository extends JpaRepository<Abteilung, UUID> {
}
