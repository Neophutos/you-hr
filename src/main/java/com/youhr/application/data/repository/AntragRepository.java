package com.youhr.application.data.repository;

import com.youhr.application.data.entity.Antrag;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @desc Repository für das Objekt Antrag
 */
public interface AntragRepository extends JpaRepository<Antrag, UUID>  {
}
