package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.Tache;
import com.projectmt.project_management_tool.repositories.TacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TacheService {

    @Autowired
    private TacheRepository tacheRepository;

    public Tache getTacheById(Long tacheId){
        return tacheRepository.findById(tacheId).orElseThrow(() -> new RuntimeException("Tache inconnu"));
    }

    public List<Tache> getTachesByProjetId(Long projetId){
        return tacheRepository.findByProjetId(projetId);
    }

    public Tache saveTache(Tache tache){
        return tacheRepository.save(tache);
    }

    public Tache updateTache(Tache tache){
        //On récupére le projet et le user associé à la tache
        Tache currentTache = getTacheById(tache.getId());
        tache.setProjet(currentTache.getProjet());
        tache.setUser(currentTache.getUser());
        return tacheRepository.save(tache);
    }
}
