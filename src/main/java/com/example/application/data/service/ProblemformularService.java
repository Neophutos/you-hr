package com.example.application.data.service;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.entity.Problemformular;
import java.util.Optional;
import java.util.UUID;

import com.example.application.data.repository.MitarbeiterRepository;
import com.example.application.data.repository.ProblemformularRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProblemformularService  {

    private final ProblemformularRepository repository;

    @Autowired
    public ProblemformularService(ProblemformularRepository repository) {
        this.repository = repository;
    }

    public Optional<Problemformular> get(UUID id) {
        return repository.findById(id);
    }

    public Problemformular update(Problemformular entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public Page<Problemformular> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }
}
