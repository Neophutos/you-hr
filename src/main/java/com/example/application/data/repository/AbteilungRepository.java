package com.example.application.data.repository;

import com.example.application.data.entity.Abteilung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AbteilungRepository extends JpaRepository<Abteilung, UUID> {
}
