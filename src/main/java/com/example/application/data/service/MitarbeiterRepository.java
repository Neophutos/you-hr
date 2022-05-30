package com.example.application.data.service;

import com.example.application.data.entity.Mitarbeiter;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, UUID> {

}