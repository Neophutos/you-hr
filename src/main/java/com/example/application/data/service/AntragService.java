package com.example.application.data.service;

import com.example.application.data.entity.Antrag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.application.data.repository.AntragRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AntragService {

    private final AntragRepository repository;

    @Autowired
    public AntragService(AntragRepository repository) {
        this.repository = repository;
    }

    public Optional<Antrag> get(UUID id) {
        return repository.findById(id);
    }

    public Antrag update(Antrag entity) {
        return repository.save(entity);
    }

    public void delete(Antrag antrag) {
        repository.delete(antrag);
    }

    public Page<Antrag> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int countProblems() {
        return (int) repository.count();
    }

    public List<Antrag> findAll(){
        return repository.findAll();
    }

}