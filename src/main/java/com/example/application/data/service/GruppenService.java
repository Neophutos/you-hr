package com.example.application.data.service;

import com.example.application.data.entity.Abteilung;
import com.example.application.data.entity.Antrag;
import com.example.application.data.entity.Team;
import com.example.application.data.repository.AbteilungRepository;
import com.example.application.data.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GruppenService {

    private final AbteilungRepository abteilungRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public GruppenService(AbteilungRepository abteilungRepository, TeamRepository teamRepository, AbteilungRepository abteilungRepository1, TeamRepository teamRepository1){
        this.abteilungRepository = abteilungRepository1;
        this.teamRepository = teamRepository1;
    }

    public Optional<Abteilung> getAbteilung(UUID id) {return abteilungRepository.findById(id);}

    public Optional<Team> getTeam(UUID id) { return teamRepository.findById(id);}

    public Abteilung updateAbteilung(Abteilung entity) { return abteilungRepository.save(entity);}

    public Team updateTeam(Team entity) {
        return teamRepository.save(entity);
    }

    public void deleteAbteilung(Abteilung entity) {
        abteilungRepository.delete(entity);
    }

    public void deleteTeam(Team entity) { teamRepository.delete(entity);}

    public Page<Abteilung> listAbteilung(Pageable pageable) {
        return abteilungRepository.findAll(pageable);
    }

    public Page<Team> listTeam(Pageable pageable) { return teamRepository.findAll(pageable);}

    public int countAbteilungen() {
        return (int) abteilungRepository.count();
    }

    public int countTeams(){ return (int) teamRepository.count();}

    public List<Abteilung> findAllAbteilungen(){
        return abteilungRepository.findAll();
    }

    public List<Team> findAllTeams(){ return teamRepository.findAll();}

}