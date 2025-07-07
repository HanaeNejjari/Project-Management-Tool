package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.HistoriqueModif;
import com.projectmt.project_management_tool.models.Tache;
import com.projectmt.project_management_tool.repositories.HistoriqueModifRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class HistoriqueModifService {

    @Autowired
    private HistoriqueModifRepository historiqueModifRepository;

    public List<HistoriqueModif> getHistoByTacheId(Long tacheId){
        return historiqueModifRepository.findByTacheId(tacheId);
    }

    public HistoriqueModif createHisto(Tache tache, String champ, String prevValue, String newValue){
        HistoriqueModif newHisto = new HistoriqueModif();
        newHisto.setDateModification(LocalDate.now());
        newHisto.setChampModifie(champ);
        newHisto.setAncienneValeur(prevValue);
        newHisto.setNouvelleValeur(newValue);
        newHisto.setTache(tache);
        return historiqueModifRepository.save(newHisto);
    }

    public List<HistoriqueModif> setTacheModif(Tache prevTache, Tache newTache){
        List<HistoriqueModif> histoList = new ArrayList<>();
        if (!Objects.equals(prevTache.getNom(), newTache.getNom())){
            histoList.add(createHisto(newTache, "Nom", prevTache.getNom(), newTache.getNom()));
        }
        if (!Objects.equals(prevTache.getTacheDesc(), newTache.getTacheDesc())){
            histoList.add(createHisto(newTache, "Description", prevTache.getTacheDesc(), newTache.getTacheDesc()));
        }
        if (!Objects.equals(prevTache.getDateEcheance(), newTache.getDateEcheance())){
            histoList.add(createHisto(newTache, "Date d'échéance", prevTache.getDateEcheance().toString(), newTache.getDateEcheance().toString()));
        }
        if (!Objects.equals(prevTache.getPriorite(), newTache.getPriorite())){
            histoList.add(createHisto(newTache, "Priorite", prevTache.getPriorite(), newTache.getPriorite()));
        }
        if (!Objects.equals(prevTache.getDateFin(), newTache.getDateFin())){
            histoList.add(createHisto(newTache, "Date de fin", prevTache.getDateFin().toString(), newTache.getDateFin().toString()));
        }
        if (!Objects.equals(prevTache.getStatut(), newTache.getStatut())){
            histoList.add(createHisto(newTache, "Statut", prevTache.getStatut(), newTache.getStatut()));
        }
        return histoList;
    }

}
