package com.youhr.application.data.service;

import com.youhr.application.data.entity.Abteilung;
import com.youhr.application.data.entity.Mitarbeiter;
import com.youhr.application.data.entity.Team;
import com.youhr.application.data.repository.AbteilungRepository;
import com.youhr.application.data.repository.MitarbeiterRepository;
import com.youhr.application.data.repository.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @desc MitarbeiterService stellt den Dienst zur Erstellung, Veränderung und Löschung von Mitarbeitern im System dar.
 *
 * @category Service
 * @author Ben Köppe, Tim Freund, Riccardo Prochnow
 * @version 1.0
 * @since 2022-08-02
 */
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

    public Mitarbeiter update(Mitarbeiter entity) {return mitarbeiterRepository.save(entity);}

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
