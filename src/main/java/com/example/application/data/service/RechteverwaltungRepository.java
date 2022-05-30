package com.example.application.data.service;

import com.example.application.data.entity.Rechteverwaltung;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RechteverwaltungRepository extends JpaRepository<Rechteverwaltung, UUID> {

}