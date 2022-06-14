package com.example.application.data.service;

import com.example.application.data.entity.Mitarbeiter;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<Mitarbeiter> get(UUID id) {
        return repository.findById(id);
    }

    public Mitarbeiter update(Mitarbeiter entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Mitarbeiter> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public MitarbeiterRepository getRepository() {
        return repository;
    }
}
