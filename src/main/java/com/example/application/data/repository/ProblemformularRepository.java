package com.example.application.data.repository;

import com.example.application.data.entity.Problemformular;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemformularRepository extends JpaRepository<Problemformular, UUID>  {
}
