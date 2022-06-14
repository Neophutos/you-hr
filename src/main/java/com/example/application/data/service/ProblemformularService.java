package com.example.application.data.service;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.entity.Problem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.application.data.repository.MitarbeiterRepository;
import com.example.application.data.repository.ProblemformularRepository;
import com.vaadin.flow.data.provider.DataProvider;
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

    public Optional<Problem> get(UUID id) {
        return repository.findById(id);
    }

    public Problem update(Problem entity) {
        return repository.save(entity);
    }

    public void delete(Problem id) {
        repository.delete(id);
    }

    public Page<Problem> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int countProblems() {
        return (int) repository.count();
    }

    public List<Problem> findAllProblems() {
        return repository.findAll();
    }
}
