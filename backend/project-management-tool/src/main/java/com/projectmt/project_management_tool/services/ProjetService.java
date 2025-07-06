package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.Projet;
import com.projectmt.project_management_tool.repositories.ProjetRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjetService {
    private final ProjetRepository projetRepository;

    public ProjetService(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    public Projet getProjetById(Long id){
        return projetRepository.findById(id).orElseThrow(() -> new RuntimeException("Projet inconnu"));
    }

    public Projet saveProjet(Projet projet){
        return projetRepository.save(projet);
    }
}
