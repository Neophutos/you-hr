package com.example.application.data.service;

import com.example.application.data.entity.Rechteverwaltung;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RechteverwaltungService {

    private final RechteverwaltungRepository repository;

    @Autowired
    public RechteverwaltungService(RechteverwaltungRepository repository) {
        this.repository = repository;
    }

    public Optional<Rechteverwaltung> get(UUID id) {
        return repository.findById(id);
    }

    public Rechteverwaltung update(Rechteverwaltung entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Rechteverwaltung> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
