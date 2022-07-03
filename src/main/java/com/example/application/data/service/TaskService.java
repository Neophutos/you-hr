package com.example.application.data.service;

import com.example.application.data.entity.Antrag;
import com.example.application.data.repository.AntragRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final AntragRepository antragRepository;

    public TaskService(AntragRepository antragRepository) {

        this.antragRepository = antragRepository;
    }

    public List<Antrag> findAllProblems(String filterText) {
        if(filterText == null || filterText.isEmpty()) {
            return antragRepository.findAll();
        } else {
            return antragRepository.search(filterText);
        }
    }

    public long countMitarbeiter(){
        return antragRepository.count();
    }

    public void deleteMitarbeiter(Antrag antrag) {antragRepository.delete(antrag);
    }

}
