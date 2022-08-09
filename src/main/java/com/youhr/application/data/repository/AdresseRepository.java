package com.youhr.application.data.repository;

import com.youhr.application.data.entity.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @desc Repository für das Objekt Adresse
 */
public interface AdresseRepository extends JpaRepository<Adresse, UUID> {
}
