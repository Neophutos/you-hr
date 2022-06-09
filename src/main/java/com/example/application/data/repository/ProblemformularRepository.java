package com.example.application.data.repository;

import com.example.application.data.entity.Problem;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProblemformularRepository extends JpaRepository<Problem, UUID>  {
}
