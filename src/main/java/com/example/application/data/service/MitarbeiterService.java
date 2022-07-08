package com.example.application.data.service;

import com.example.application.data.Role;
import com.example.application.data.entity.Abteilung;
import com.example.application.data.entity.Mitarbeiter;

import java.util.List;
import java.util.Optional;

import com.example.application.data.entity.Team;
import com.example.application.data.repository.AbteilungRepository;
import com.example.application.data.repository.MitarbeiterRepository;
import com.example.application.data.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MitarbeiterService {

    private final MitarbeiterRepository mitarbeiterRepository;
    private final AbteilungRepository abteilungRepository;
    private final TeamRepository teamRepository;

    public MitarbeiterService(MitarbeiterRepository mitarbeiterRepository, AbteilungRepository abteilungRepository, TeamRepository teamRepository) {
        this.mitarbeiterRepository = mitarbeiterRepository;
        this.abteilungRepository = abteilungRepository;
        this.teamRepository = teamRepository;
    }

    public Optional<Mitarbeiter> get(Long id) {
        return mitarbeiterRepository.getByID(id).stream().findFirst();
    }

    public Mitarbeiter update(Mitarbeiter entity) {

        if(entity.getRechteverwaltung().isAdmin()) {
            if(entity.getUser().getRoles() != null && !entity.getUser().getRoles().contains(Role.ADMIN)) {
                entity.getUser().addRole(Role.ADMIN);
            }
        } else if (!entity.getRechteverwaltung().isAdmin()) {
            if (entity.getUser().getRoles() != null && entity.getUser().getRoles().contains(Role.ADMIN)) {
                entity.getUser().removeRole(Role.ADMIN);
            }

        }

        return mitarbeiterRepository.save(entity);
    }

    public void delete(Mitarbeiter mitarbeiter) {
        mitarbeiterRepository.delete(mitarbeiter);
    }

    public Page<Mitarbeiter> list(Pageable pageable) {
        return mitarbeiterRepository.findAll(pageable);
    }

    public int countMitarbeiter() {
        return (int) mitarbeiterRepository.count();
    }

    public List<Mitarbeiter> findAllByString(String filterText) {
        if(filterText == null || filterText.isEmpty()) {
            return mitarbeiterRepository.findAll();
        } else {
            return mitarbeiterRepository.search(filterText);
        }
    }

    public List<Abteilung> findAllAbteilungen() {
        return abteilungRepository.findAll();
    }

    public List<Team> findAllTeams() {
        return teamRepository.findAll();
    }

    public MitarbeiterRepository getRepository() {
        return mitarbeiterRepository;
    }
}
