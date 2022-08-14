package com.youhr.application.data.service;

import com.youhr.application.data.entity.Antrag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.youhr.application.data.entity.Status;
import com.youhr.application.data.repository.AntragRepository;
import com.youhr.application.data.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @desc AntragService stellt den Dienst zur Erstellung, Veränderung und Löschung von Anträgen im System dar (inkl. der Statusänderung).
 *
 * @category Service
 * @author Ben Köppe, Tim Freund
 * @version 1.0
 * @since 2022-08-10
 */
@Service
public class AntragService {

    private final AntragRepository antragRepository;
    private final StatusRepository statusRepository;

    @Autowired
    public AntragService(AntragRepository antragRepository, StatusRepository statusRepository) {
        this.antragRepository = antragRepository;
        this.statusRepository = statusRepository;
    }

    public Optional<Antrag> get(UUID id) {
        return antragRepository.findById(id);
    }

    public Antrag update(Antrag entity) {
        return antragRepository.save(entity);
    }

    public void delete(Antrag antrag) {
        antragRepository.delete(antrag);
    }

    public Page<Antrag> list(Pageable pageable) {
        return antragRepository.findAll(pageable);
    }

    public int countProblems() {
        return (int) antragRepository.count();
    }

    public List<Antrag> findAll(){
        return antragRepository.findAll();
    }

    public List<Status> findAllStatuses(){ return statusRepository.findAll(); }

}
