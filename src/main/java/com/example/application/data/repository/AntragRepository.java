package com.example.application.data.repository;

import com.example.application.data.entity.Antrag;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @desc Repository für das Objekt Antrag
 */
public interface AntragRepository extends JpaRepository<Antrag, UUID>  {
    @Query("select p from Antrag p " +
            "where lower(p.id) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(p.antragsart) like lower(concat('%', :searchTerm, '%'))")
    List<Antrag> search(@Param("searchTerm") String searchTerm);
}
