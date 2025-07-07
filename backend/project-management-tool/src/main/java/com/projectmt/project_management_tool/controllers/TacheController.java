package com.projectmt.project_management_tool.controllers;

import com.projectmt.project_management_tool.models.Projet;
import com.projectmt.project_management_tool.models.Tache;
import com.projectmt.project_management_tool.models.User;
import com.projectmt.project_management_tool.services.*;
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

    @Autowired
    private HistoriqueModifService historiqueModifService;

    @Autowired
    private EmailService emailService;


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
        //On met à jour la tache
        Tache prevTache = new Tache(tacheService.getTacheById(tache.getId()));
        Tache updatedTache = tacheService.updateTache(tache);
        //On ajoute l'historique de modfication
        historiqueModifService.setTacheModif(prevTache, updatedTache);
        return updatedTache;
    }

    @PostMapping("/assignTask")
    public Tache assignTask(@RequestParam Long tacheId, @RequestParam Long userId){
        //On récupère la tache
        Tache tache = tacheService.getTacheById(tacheId);
        //On récupére les user
        User prevUser = tache.getUser();
        User newUser = userService.getUserById(userId);

        //On modifie le user dans la tache
        tache.setUser(newUser);

        //On sauvegarde la tache
        Tache updatedTache = tacheService.saveTache(tache);
        //On ajoute la modif à l'historique
        historiqueModifService.createHisto(updatedTache, "Responsable", prevUser.getEmail(), newUser.getEmail());

        //On envoie un mail à l'utilisateur assigné
        emailService.sendNotification(newUser, updatedTache.getNom(), updatedTache.getProjet().getNom());

        return updatedTache;
    }
}
