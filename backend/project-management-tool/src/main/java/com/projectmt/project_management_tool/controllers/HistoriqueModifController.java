package com.projectmt.project_management_tool.controllers;

import com.projectmt.project_management_tool.models.HistoriqueModif;
import com.projectmt.project_management_tool.services.HistoriqueModifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/histo")
public class HistoriqueModifController {

    @Autowired
    private HistoriqueModifService historiqueModifService;

    @GetMapping("/tache/{tacheId}")
    public List<HistoriqueModif> getHistoByTache(@PathVariable Long tacheId){
        return historiqueModifService.getHistoByTacheId(tacheId);
    }

}
