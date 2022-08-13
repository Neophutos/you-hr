package com.youhr.application.data.service;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.youhr.application.data.entity.Abteilung;
import com.youhr.application.data.entity.Team;
import com.youhr.application.data.repository.AbteilungRepository;
import com.youhr.application.data.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @desc GruppenService stellt den Dienst zur Erstellung, Veränderung und Löschung von Abteilungen und Teams im System dar.
 *
 * @category Service
 * @author Ben Köppe, Tim Freund
 * @version 1.0
 * @since 2022-07-15
 */
@Service
public class GruppenService {

    private final AbteilungRepository abteilungRepository;
    private final TeamRepository teamRepository;

    @Autowired
    public GruppenService(AbteilungRepository abteilungRepository, TeamRepository teamRepository){
        this.abteilungRepository = abteilungRepository;
        this.teamRepository = teamRepository    ;
    }

    public Optional<Abteilung> getAbteilung(UUID id) {return abteilungRepository.findById(id);}

    public Optional<Team> getTeam(UUID id) { return teamRepository.findById(id);}

    public void updateAbteilung(Abteilung entity) { abteilungRepository.save(entity);}

    public void updateTeam(Team entity) {
        teamRepository.save(entity);
    }

    public void deleteAbteilung(Abteilung entity) {
        try {
            abteilungRepository.delete(entity);
        } catch (DataIntegrityViolationException e) {
            Notification.show("Hinweis: Sie müssen alle Mitarbeiter aus einer Abteilung entfernen, bevor sie diese löschen").addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
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
