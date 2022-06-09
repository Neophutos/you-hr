package com.example.application.data.repository;

import com.example.application.data.entity.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdresseRepository extends JpaRepository<Adresse, UUID> {

}