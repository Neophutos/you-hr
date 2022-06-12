package com.example.application.data.service;

import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.entity.Problem;
import com.example.application.data.repository.AdresseRepository;
import com.example.application.data.repository.MitarbeiterRepository;
import com.example.application.data.repository.ProblemformularRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final ProblemformularRepository problemformularRepository;

    public TaskService(ProblemformularRepository problemformularRepository) {

        this.problemformularRepository = problemformularRepository;
    }

    public List<Problem> findAllProblems(String filterText) {
        if(filterText == null || filterText.isEmpty()) {
            return problemformularRepository.findAll();
        } else {
            return problemformularRepository.search(filterText);
        }
    }

    public long countMitarbeiter(){
        return problemformularRepository.count();
    }

    public void deleteMitarbeiter(Problem problem) {
        problemformularRepository.delete(problem);
    }

}
