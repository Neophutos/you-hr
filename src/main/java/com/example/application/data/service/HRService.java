package com.example.application.data.service;

import com.example.application.data.entity.Adresse;
import com.example.application.data.entity.Mitarbeiter;
import com.example.application.data.repository.AdresseRepository;
import com.example.application.data.repository.MitarbeiterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HRService {

    private final MitarbeiterRepository mitarbeiterRepository;
    private final AdresseRepository adresseRepository;

    public HRService(MitarbeiterRepository mitarbeiterRepository, AdresseRepository adresseRepository) {

        this.mitarbeiterRepository = mitarbeiterRepository;
        this.adresseRepository = adresseRepository;
    }

    public List<Mitarbeiter> findAllMitarbeiter(String filterText) {
        if(filterText == null || filterText.isEmpty()) {
            return mitarbeiterRepository.findAll();
        } else {
            return mitarbeiterRepository.search(filterText);
        }
    }

    public long countMitarbeiter(){
        return mitarbeiterRepository.count();
    }

    public void deleteMitarbeiter(Mitarbeiter mitarbeiter) {
        mitarbeiterRepository.delete(mitarbeiter);
    }

}
