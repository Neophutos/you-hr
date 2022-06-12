package com.example.application.data.repository;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.entity.Problem;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProblemformularRepository extends JpaRepository<Problem, UUID>  {
    @Query("select p from Problem p " +
            "where lower(p.id) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(p.problemart) like lower(concat('%', :searchTerm, '%'))")
    List<Problem> search(@Param("searchTerm") String searchTerm);
}
