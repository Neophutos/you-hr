package com.example.application.data.repository;

import com.example.application.data.entity.Mitarbeiter;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MitarbeiterRepository extends JpaRepository<Mitarbeiter, UUID> {

    @Query("select m from Mitarbeiter m " +
            "where lower(m.vorname) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(m.nachname) like lower(concat('%', :searchTerm, '%'))")
    List<Mitarbeiter> search(@Param("searchTerm") String searchTerm);


    @Query("select m from Mitarbeiter m " +
            "where m.id = id")
    List<Mitarbeiter> getByID(@Param("id") Long id);

}