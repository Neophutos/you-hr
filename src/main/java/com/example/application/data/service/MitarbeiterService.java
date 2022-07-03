package com.example.application.data.service;

import com.example.application.data.Role;
import com.example.application.data.entity.Mitarbeiter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.application.data.generator.DataGenerator;
import com.example.application.data.repository.MitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MitarbeiterService {

    private final MitarbeiterRepository repository;

    @Autowired
    public MitarbeiterService(MitarbeiterRepository repository) {
        this.repository = repository;
    }

    public Optional<Mitarbeiter> get(Long id) {
        return repository.getByID(id).stream().findFirst();
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

        return repository.save(entity);
    }

    public void delete(Mitarbeiter mitarbeiter) {
        repository.delete(mitarbeiter);
    }

    public Page<Mitarbeiter> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Mitarbeiter> findAllByString(String filterText) {
        if(filterText == null || filterText.isEmpty()) {
            return repository.findAll();
        } else {
            return repository.search(filterText);
        }
    }

    public MitarbeiterRepository getRepository() {
        return repository;
    }
}
