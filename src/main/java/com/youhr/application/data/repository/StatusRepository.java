package com.youhr.application.data.repository;

import com.youhr.application.data.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @desc Repository für das Objekt Status
 */
public interface StatusRepository extends JpaRepository<Status, UUID> {

}
