package com.projectmt.project_management_tool.controllers;

import com.projectmt.project_management_tool.models.Projet;
import com.projectmt.project_management_tool.models.RoleUtilisateur;
import com.projectmt.project_management_tool.models.User;
import com.projectmt.project_management_tool.services.JwtService;
import com.projectmt.project_management_tool.services.ProjetService;
import com.projectmt.project_management_tool.services.RoleUtilisateurService;
import com.projectmt.project_management_tool.services.UserService;
import com.projectmt.project_management_tool.utils.RoleConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

import static com.projectmt.project_management_tool.utils.RoleConstants.*;

@RestController
@RequestMapping("/api/projets")
public class ProjetController {
    private final RoleUtilisateurService roleUtilisateurService;
    private final JwtService jwtService;
    private final UserService userService;
    private final ProjetService projetService;

    public ProjetController(RoleUtilisateurService roleUtilisateurService, JwtService jwtService,
                            UserService userService, ProjetService projetService){
        this.roleUtilisateurService = roleUtilisateurService;
        this.jwtService = jwtService;
        this.userService = userService;
        this.projetService = projetService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Projet>> getProjectsByUser(@PathVariable Long userId){
        return ResponseEntity.ok(roleUtilisateurService.getProjetsByUserId(userId));
    }

    @GetMapping("role/{userId}/{projetId}")
    public ResponseEntity<String> getRoleForProject(@PathVariable Long userId,@PathVariable Long projetId){
        return ResponseEntity.ok(roleUtilisateurService.getUserRoleInProjet(userId,projetId));
    }

    @GetMapping("/mesprojets")
    public ResponseEntity<List<Projet>> getProjectsForCurrentUser(@RequestHeader("Authorization") String authHeader){
        //On vérifie que l'utilisateur est connecté
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //On récupére l'email depuis le token
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        //On verifie que l'utilisateur existe
        User user = userService.getUserByEmail(email).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        //On récupère les projets de l'utilisateur
        List<Projet> projets = roleUtilisateurService.getProjetsByUserId(user.getId());
        return ResponseEntity.ok(projets);
    }

    @GetMapping("/{projetId}")
    public ResponseEntity<Projet> getProjectById(@PathVariable Long projetId,
                                                 @RequestHeader("Authorization") String authHeader){
        //On vérifie que l'utilisateur est connecté
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //On récupére l'email depuis le token
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        //On verifie que l'utilisateur existe
        User user = userService.getUserByEmail(email).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        //On récupère le role de l'utilisateur dans le projet pour savoir s'il a accès
        String role = roleUtilisateurService.getUserRoleInProjet(user.getId(), projetId);

        //Si l'utilisateur n'as pas accès, erreur
        if (role == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        //Sinon on retourne le projet
        Projet projet = projetService.getProjetById(projetId);
        return ResponseEntity.ok(projet);
    }

    @PostMapping
    public ResponseEntity<?> createProjet(@RequestBody Projet projet,
                                          @RequestHeader ("Authorization") String authHeader){
        //On vérifie que l'utilisateur est connecté
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //On récupére l'email depuis le token
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        //On verifie que l'utilisateur existe
        User user = userService.getUserByEmail(email).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        //Création du nouveau projet
        Projet newProjet = projetService.saveProjet(projet);

        //Ajout d'un role admin pour le créateur
        RoleUtilisateur role = new RoleUtilisateur();
        role.setUtilisateur(user);
        role.setProjet(newProjet);
        role.setLibelle(RoleConstants.ADMIN);

        roleUtilisateurService.saveRoleUtilisateur(role);

        return ResponseEntity.status(HttpStatus.CREATED).body(newProjet);
    }

    @PutMapping("/{projetId}")
    public ResponseEntity<?> updateProjet(@PathVariable Long projetId, @RequestBody Projet updatedProjet,
                                          @RequestHeader("Authorization") String authHeader){
        //On vérifie que l'utilisateur est connecté
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //On récupére l'email depuis le token
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        //On verifie que l'utilisateur existe
        User user = userService.getUserByEmail(email).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        //On récupère le role de l'utilisateur
        String role = roleUtilisateurService.getUserRoleInProjet(user.getId(), projetId);
        //On vérifie que l'utilisateur a les droits
        if (role == null || !ROLES_UPDATE.contains(role)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'êtes pas autorisé à modifier ce projet");
        }
        //On vérifie que le projet existe
        Projet actualProjet = projetService.getProjetById(projetId);
        if (actualProjet == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projet inexistant");
        }

        //On modifie le projet
        actualProjet.setNom(updatedProjet.getNom());
        actualProjet.setProjetDesc(updatedProjet.getProjetDesc());
        actualProjet.setDateDebut(updatedProjet.getDateDebut());

        Projet projetUpdate = projetService.saveProjet(actualProjet);
        return ResponseEntity.ok(projetUpdate);
    }

    @GetMapping("/{projetId}/users")
    public ResponseEntity<List<RoleUtilisateur>> getUsersInProjet(@PathVariable Long projetId){
        return ResponseEntity.ok(roleUtilisateurService.getUsersRole(projetId));
    }

    @PostMapping("/assignrole")
    public ResponseEntity<?> assignRole(@RequestParam String addEmail, @RequestParam Long projetId,
                                        @RequestParam String role, @RequestHeader("Authorization") String authHeader){
        //On vérifie que l'utilisateur est connecté
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //On récupére l'email du user depuis le token
        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtService.extractEmail(token);
        //On verifie que l'utilisateur existe
        User user = userService.getUserByEmail(userEmail).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        //On récupère le role de l'utilisateur
        String userRole = roleUtilisateurService.getUserRoleInProjet(user.getId(), projetId);
        //On vérifie que l'utilisateur a les droits
        if (!Objects.equals(userRole, ADMIN)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'êtes pas autorisé à ajouter un utilisateur");
        }
        //On vérifie que le projet existe
        Projet projet = projetService.getProjetById(projetId);
        if (projet == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projet inexistant");
        }
        //On verifie que l'utilisateur à ajouter existe
        User addUser = userService.getUserByEmail(addEmail).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        //On vérifie que l'utilisateur a ajouté n'a pas déjà un role dans le projet
        String addUserRole = roleUtilisateurService.getUserRoleInProjet(addUser.getId(), projetId);
        if (addUserRole != null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("L'utilisateur est déjà dans le projet");
        }

        //On vérifie que le role existe
        if (role == null || !ALL_ROLES.contains(role)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role inexistant");
        }

        //On ajoute le role de l'utilisateur dans le projet
        RoleUtilisateur newRole = new RoleUtilisateur();
        newRole.setUtilisateur(addUser);
        newRole.setProjet(projet);
        newRole.setLibelle(role);

        RoleUtilisateur createdRole = roleUtilisateurService.saveRoleUtilisateur(newRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PutMapping("/updaterole")
    public ResponseEntity<?> updateRole(@RequestParam String updateEmail, @RequestParam Long projetId,
                                        @RequestParam String role, @RequestHeader("Authorization") String authHeader){
        //On vérifie que l'utilisateur est connecté
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //On récupére l'email du user depuis le token
        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtService.extractEmail(token);
        //On verifie que l'utilisateur existe
        User user = userService.getUserByEmail(userEmail).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        //On récupère le role de l'utilisateur
        String userRole = roleUtilisateurService.getUserRoleInProjet(user.getId(), projetId);
        //On vérifie que l'utilisateur a les droits
        if (!Objects.equals(userRole, ADMIN)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'êtes pas autorisé à ajouter un utilisateur");
        }
        //On vérifie que le projet existe
        Projet projet = projetService.getProjetById(projetId);
        if (projet == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Projet inexistant");
        }
        //On verifie que l'utilisateur à modifier existe
        User updateUser = userService.getUserByEmail(updateEmail).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        //On vérifie que l'utilisateur est déjà dans projet et on répère son role
        RoleUtilisateur updateUserRole = roleUtilisateurService.getRoleByUserIdAndProjetId(updateUser.getId(), projetId).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "L'utilisateur n'est pas dans le projet"));

        //On vérifie que le role existe
        if (!ALL_ROLES.contains(role)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role inexistant");
        }

        //On modifie le role
        updateUserRole.setLibelle(role);

        RoleUtilisateur updatedRole = roleUtilisateurService.saveRoleUtilisateur(updateUserRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedRole);
    }

    @DeleteMapping("/removerole/{id}")
    public ResponseEntity<?> removeRole(@PathVariable Long id,
                                        @RequestHeader("Authorization") String authHeader){
        //On vérifie que l'utilisateur est connecté
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //On récupére l'email du user depuis le token
        String token = authHeader.replace("Bearer ", "");
        String userEmail = jwtService.extractEmail(token);
        //On verifie que l'utilisateur existe
        User user = userService.getUserByEmail(userEmail).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));

        //On verifie que le role existe
        RoleUtilisateur deleteRole = roleUtilisateurService.getRoleById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role introuvable"));

        //On récupère le role de l'utilisateur
        String userRole = roleUtilisateurService.getUserRoleInProjet(user.getId(), deleteRole.getProjet().getId());
        //On vérifie que l'utilisateur a les droits
        if (!Objects.equals(userRole, ADMIN)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Vous n'êtes pas autorisé à supprimer un utilisateur");
        }

        //On supprime le role
        roleUtilisateurService.removeRole(deleteRole);
        return ResponseEntity.ok("Rôle supprimé avec succés");
    }

}
