package com.projectmt.project_management_tool.controllers;

import com.projectmt.project_management_tool.models.Projet;
import com.projectmt.project_management_tool.models.Tache;
import com.projectmt.project_management_tool.models.User;
import com.projectmt.project_management_tool.services.ProjetService;
import com.projectmt.project_management_tool.services.TacheService;
import com.projectmt.project_management_tool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/taches")
public class TacheController {

    @Autowired
    private TacheService tacheService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjetService projetService;


    @GetMapping("/projet/{projetId}")
    public List<Tache> getTachesByProjet(@PathVariable Long projetId){
        return tacheService.getTachesByProjetId(projetId);
    }

    @PostMapping
    public Tache createTache(@RequestBody Tache tache,
                             @RequestParam Long projetId,
                             @RequestParam Long userId){
        //On récupère le projet
        Projet projet = projetService.getProjetById(projetId);
        //On récupére le user
        User user = userService.getUserById(userId);

        //On ajoute le user et le projet dans la tache
        tache.setUser(user);
        tache.setProjet(projet);

        //On sauvegarde
        return tacheService.saveTache(tache);
    }

    @PutMapping
    public Tache updateTache(@RequestBody Tache tache){
        return tacheService.updateTache(tache);
    }

    @PostMapping("/assignTask")
    public Tache assignTask(@RequestParam Long tacheId, @RequestParam Long userId){
        //On récupère la tache
        Tache tache = tacheService.getTacheById(tacheId);
        //On récupére le user
        User user = userService.getUserById(userId);

        //On modifie le user dans la tache
        tache.setUser(user);

        //On sauvegarde
        return tacheService.saveTache(tache);
    }
}
