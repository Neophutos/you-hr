package com.example.application.data.repository;

import com.example.application.data.entity.Abteilung;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbteilungRepository extends JpaRepository<Abteilung, Integer> {
}
