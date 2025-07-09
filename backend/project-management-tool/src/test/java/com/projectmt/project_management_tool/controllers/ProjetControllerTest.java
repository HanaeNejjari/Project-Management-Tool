package com.projectmt.project_management_tool.controllers;

import com.projectmt.project_management_tool.models.Projet;
import com.projectmt.project_management_tool.models.RoleUtilisateur;
import com.projectmt.project_management_tool.models.User;
import com.projectmt.project_management_tool.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.projectmt.project_management_tool.utils.RoleConstants.ADMIN;
import static com.projectmt.project_management_tool.utils.RoleConstants.ALL_ROLES;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.projectmt.project_management_tool.utils.RoleConstants.*;

public class ProjetControllerTest {

    private ProjetController projetController;
    private RoleUtilisateurService roleUtilisateurService;
    private JwtService jwtService;
    private UserService userService;
    private ProjetService projetService;

    @BeforeEach
    void setUp() {
        roleUtilisateurService = mock(RoleUtilisateurService.class);
        jwtService = mock(JwtService.class);
        userService = mock(UserService.class);
        projetService = mock(ProjetService.class);
        projetController = new ProjetController(roleUtilisateurService, jwtService, userService, projetService);
    }

    @Test
    void getProjectsByUser_shouldReturnListOfProjets() { /* ton test existant */ }

    @Test
    void getProjectById_shouldReturnProjetIfAuthorized() { /* ton test */ }

    @Test
    void getProjectById_shouldReturnUnauthorizedWithoutAuth() { /* ton test */ }

    @Test
    void getProjectById_shouldReturnNotFoundWhenUserMissing() { /* ton test */ }

    @Test
    void getProjectById_shouldReturnForbiddenWhenNoRole() { /* ton test */ }

    @Test
    void createProjet_shouldCreateProjetAndAssignAdminRole() { /* ton test */ }

    @Test
    void createProjet_shouldReturnUnauthorizedWithoutToken() { /* ton test */ }

    @Test
    void createProjet_shouldReturnNotFoundWhenUserMissing() { /* ton test */ }

    @Test
    void assignRole_shouldAddUserToProjectIfAdmin() { /* ton test */ }

    @Test
    void assignRole_shouldReturnUnauthorizedWithoutToken() { /* ton test */ }

    @Test
    void assignRole_shouldReturnForbiddenWhenNotAdmin() { /* ton test */ }

    @Test
    void assignRole_shouldReturnNotFoundWhenProjectMissing() { /* ton test */ }

    @Test
    void assignRole_shouldReturnForbiddenWhenUserAlreadyInProject() { /* ton test */ }

    @Test
    void assignRole_shouldReturnNotFoundWhenRoleInvalid() { /* ton test */ }

    // --- Nouveaux tests ajoutés pour couvrir les branches manquantes ---

    @Test
    void getProjectsForCurrentUser_shouldReturnUnauthorizedIfNoToken() {
        var resp = projetController.getProjectsForCurrentUser(null);
        assertEquals(401, resp.getStatusCodeValue());
    }

    @Test
    void getProjectsForCurrentUser_shouldReturnNotFoundWhenUserMissing() {
        String token = "t";
        when(jwtService.extractEmail(token)).thenReturn("x@y");
        when(userService.getUserByEmail("x@y")).thenReturn(Optional.empty());
        var resp = projetController.getProjectsForCurrentUser("Bearer " + token);
        assertEquals(404, resp.getStatusCodeValue());
    }

    @Test
    void getProjectsForCurrentUser_shouldReturnList() {
        String token = "tok";
        String email = "e@x";
        User u = new User(1L, email, "pass", "u");
        Projet p = new Projet(5L, "Nom", "desc", LocalDate.now());
        when(jwtService.extractEmail(token)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(u));
        when(roleUtilisateurService.getProjetsByUserId(1L)).thenReturn(List.of(p));
        var resp = projetController.getProjectsForCurrentUser("Bearer " + token);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals(1, resp.getBody().size());
    }

    @Test
    void updateProjet_shouldReturnUnauthorizedIfNoToken() {
        var resp = projetController.updateProjet(1L, new Projet(), null);
        assertEquals(401, resp.getStatusCodeValue());
    }

    @Test
    void updateProjet_shouldReturnForbiddenIfNotAdmin() {
        String token = "t";
        String email = "e@x";
        User u = new User(2L, email, "pass", "u");
        when(jwtService.extractEmail(token)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(u));
        when(roleUtilisateurService.getUserRoleInProjet(2L, 3L)).thenReturn("MEMBRE");
        var resp = projetController.updateProjet(3L, new Projet(), "Bearer " + token);
        assertEquals(403, resp.getStatusCodeValue());
    }

    @Test
    void updateProjet_shouldReturnNotFoundIfProjectNull() {
        String token = "t"; String email = "e@x";
        User u = new User(2L, email, "p", ""); Projet updated = new Projet();
        when(jwtService.extractEmail(token)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(u));
        when(roleUtilisateurService.getUserRoleInProjet(2L, 4L)).thenReturn(ADMIN);
        when(projetService.getProjetById(4L)).thenReturn(null);
        var resp = projetController.updateProjet(4L, updated, "Bearer " + token);
        assertEquals(404, resp.getStatusCodeValue());
    }

    @Test
    void updateProjet_shouldUpdateSuccessfully() {
        String token="t"; String email="e@x";
        User u=new User(2L,email,"p",""); Projet existing=new Projet(6L,"A","d",LocalDate.now());
        Projet updated=new Projet(); updated.setNom("B"); updated.setProjetDesc("E"); updated.setDateDebut(LocalDate.now());
        when(jwtService.extractEmail(token)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(u));
        when(roleUtilisateurService.getUserRoleInProjet(2L,6L)).thenReturn(ADMIN);
        when(projetService.getProjetById(6L)).thenReturn(existing);
        when(projetService.saveProjet(existing)).thenReturn(existing);
        var resp=projetController.updateProjet(6L,updated,"Bearer "+token);
        assertEquals(200, resp.getStatusCodeValue());
        Projet projet = (Projet) resp.getBody();
        assertEquals("B", projet.getNom());
    }

    @Test
    void getUsersInProjet_shouldReturnEmptyList() {
        when(roleUtilisateurService.getUsersRole(10L)).thenReturn(List.of());
        var resp = projetController.getUsersInProjet(10L);
        assertEquals(200, resp.getStatusCodeValue());
        assertTrue(resp.getBody().isEmpty());
    }

    @Test
    void updateRole_shouldReturnUnauthorizedWithoutToken() {
        var resp = projetController.updateRole("a", 1L, ADMIN, null);
        assertEquals(401, resp.getStatusCodeValue());
    }

    @Test
    void updateRole_shouldReturnForbiddenIfNotAdmin() {
        String token="t"; String email="e@x"; User u=new User(2L,email,"p","");
        when(jwtService.extractEmail(token)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(u));
        when(roleUtilisateurService.getUserRoleInProjet(2L,7L)).thenReturn("MEMBRE");
        var resp=projetController.updateRole("b@x",7L,ADMIN,"Bearer "+token);
        assertEquals(403, resp.getStatusCodeValue());
    }

    @Test
    void updateRole_shouldReturnNotFoundIfProjectNull() {
        String token="t"; String email="e@x"; User u=new User(2L,email,"p","");
        when(jwtService.extractEmail(token)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(u));
        when(roleUtilisateurService.getUserRoleInProjet(2L,8L)).thenReturn(ADMIN);
        when(projetService.getProjetById(8L)).thenReturn(null);
        var resp=projetController.updateRole("b@x",8L,ADMIN,"Bearer "+token);
        assertEquals(404, resp.getStatusCodeValue());
    }

    @Test
    void updateRole_shouldReturnNotFoundIfUserNotInProjet() {
        String token="t"; String email="e@x"; User u=new User(2L,email,"p","");
        when(jwtService.extractEmail(token)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(u));
        when(roleUtilisateurService.getUserRoleInProjet(2L,9L)).thenReturn(ADMIN);
        when(projetService.getProjetById(9L)).thenReturn(new Projet(9L,"X","D",LocalDate.now()));
        when(userService.getUserByEmail("c@x")).thenReturn(Optional.of(new User(3L,"c@x","p","")));
        when(roleUtilisateurService.getRoleByUserIdAndProjetId(3L,9L)).thenReturn(Optional.empty());
        var resp=projetController.updateRole("c@x",9L,ADMIN,"Bearer "+token);
        assertEquals(404, resp.getStatusCodeValue());
    }

    @Test
    void updateRole_shouldReturnNotFoundIfRoleInvalid() {
        String token="t"; String email="e@x"; User u=new User(2L,email,"p","");
        Projet pr=new Projet(10L,"X","D",LocalDate.now());
        when(jwtService.extractEmail(token)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(u));
        when(roleUtilisateurService.getUserRoleInProjet(2L,10L)).thenReturn(ADMIN);
        when(projetService.getProjetById(10L)).thenReturn(pr);
        User t=new User(3L,"c@x","p","");
        when(userService.getUserByEmail("c@x")).thenReturn(Optional.of(t));
        when(roleUtilisateurService.getRoleByUserIdAndProjetId(3L,10L))
                .thenReturn(Optional.of(new RoleUtilisateur(7L, "LECTEUR", t, pr)));
        var resp=projetController.updateRole("c@x",10L,"UNKNOWN","Bearer "+token);
        assertEquals(404, resp.getStatusCodeValue());
    }

    @Test
    void updateRole_shouldSucceed() {
        String token="t"; String email="e@x"; User admin=new User(2L,email,"p","");
        Projet pr=new Projet(11L,"X","D",LocalDate.now());
        RoleUtilisateur initial=new RoleUtilisateur(8L,"LECTEUR", new User(3L,"c@x","p",""), pr);
        when(jwtService.extractEmail(token)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(admin));
        when(roleUtilisateurService.getUserRoleInProjet(2L,11L)).thenReturn(ADMIN);
        when(projetService.getProjetById(11L)).thenReturn(pr);
        when(userService.getUserByEmail("c@x")).thenReturn(Optional.of(initial.getUtilisateur()));
        when(roleUtilisateurService.getRoleByUserIdAndProjetId(3L,11L))
                .thenReturn(Optional.of(initial));
        when(roleUtilisateurService.saveRoleUtilisateur(initial)).thenAnswer(invocation -> invocation.getArgument(0));
        var resp=projetController.updateRole("c@x",11L,ADMIN,"Bearer "+token);
        assertEquals(201, resp.getStatusCodeValue());
        assertEquals(ADMIN, ((RoleUtilisateur)resp.getBody()).getLibelle());
    }

    @Test
    void assignRole_shouldReturnNotFoundWhenSecondUserMissing() {
        String token = "tok";
        String email = "admin@x.com";
        User admin = new User(1L, email, "pass", "Admin");

        when(jwtService.extractEmail(token)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(admin));
        when(roleUtilisateurService.getUserRoleInProjet(1L, 5L)).thenReturn(ADMIN);
        when(projetService.getProjetById(5L)).thenReturn(new Projet(5L, "X", "Y", LocalDate.now()));
        when(userService.getUserByEmail("unknown@x.com")).thenReturn(Optional.empty());

        var resp = projetController.assignRole("unknown@x.com", 5L, ADMIN, "Bearer " + token);
        assertEquals(404, resp.getStatusCodeValue());
    }

    @Test
    void updateProjet_shouldReturnNotFoundWhenUserMissing() {
        String token = "tok";
        when(jwtService.extractEmail(token)).thenReturn("missing@x.com");
        when(userService.getUserByEmail("missing@x.com")).thenReturn(Optional.empty());

        var resp = projetController.updateProjet(1L, new Projet(), "Bearer " + token);
        assertEquals(404, resp.getStatusCodeValue());
    }

    @Test
    void updateRole_shouldReturnNotFoundIfSecondUserMissing() {
        String token = "tok";
        String email = "admin@x.com";
        User admin = new User(1L, email, "pass", "Admin");

        Projet projet = new Projet(12L, "Z", "desc", LocalDate.now());

        when(jwtService.extractEmail(token)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(admin));
        when(roleUtilisateurService.getUserRoleInProjet(1L, 12L)).thenReturn(ADMIN);
        when(projetService.getProjetById(12L)).thenReturn(projet);
        when(userService.getUserByEmail("missing@x.com")).thenReturn(Optional.empty());

        var resp = projetController.updateRole("missing@x.com", 12L, ADMIN, "Bearer " + token);
        assertEquals(404, resp.getStatusCodeValue());
    }

   @Test
   void assignRole_shouldReturnNotFoundWhenRoleIsNull() {
       String token = "tok";
       String email = "admin@x.com";
       User admin = new User(1L, email, "pass", "Admin");
       User target = new User(2L, "target@x.com", "pass", "Target");
       Projet projet = new Projet(15L, "Z", "desc", LocalDate.now());

       when(jwtService.extractEmail(token)).thenReturn(email);
       when(userService.getUserByEmail(email)).thenReturn(Optional.of(admin));
       when(userService.getUserByEmail("target@x.com")).thenReturn(Optional.of(target));
       when(roleUtilisateurService.getUserRoleInProjet(1L, 15L)).thenReturn(ADMIN);
       when(roleUtilisateurService.getUserRoleInProjet(2L, 15L)).thenReturn(null);
       when(projetService.getProjetById(15L)).thenReturn(projet);

       assertDoesNotThrow(() -> {
           var resp = projetController.assignRole("target@x.com", 15L, null, "Bearer " + token);
           assertEquals(404, resp.getStatusCodeValue());
       });
   }

    @Test
    void getProjectById_shouldReturnUnauthorizedWhenHeaderIsEmptyString() {
        var resp = projetController.getProjectById(1L, "");
        assertEquals(401, resp.getStatusCodeValue());
    }

    @Test
    void getRoleForProject_shouldReturnRoleIfExists() {
        when(roleUtilisateurService.getUserRoleInProjet(1L, 1L)).thenReturn("LECTEUR");
        var resp = projetController.getRoleForProject(1L, 1L);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals("LECTEUR", resp.getBody());
    }

    @Test
    void assignRole_shouldAssignRoleToUserIfAllValid() {
        String token = "tok";
        String adminEmail = "admin@x.com";
        User admin = new User(1L, adminEmail, "p", "");
        User target = new User(2L, "target@x.com", "p", "");
        Projet projet = new Projet(55L, "N", "D", LocalDate.now());

        when(jwtService.extractEmail(token)).thenReturn(adminEmail);
        when(userService.getUserByEmail(adminEmail)).thenReturn(Optional.of(admin));
        when(roleUtilisateurService.getUserRoleInProjet(1L, 55L)).thenReturn(ADMIN);
        when(userService.getUserByEmail("target@x.com")).thenReturn(Optional.of(target));
        when(projetService.getProjetById(55L)).thenReturn(projet);
        when(roleUtilisateurService.getUserRoleInProjet(2L, 55L)).thenReturn(null);
        when(roleUtilisateurService.saveRoleUtilisateur(any())).thenAnswer(inv -> inv.getArgument(0));

        var resp = projetController.assignRole("target@x.com", 55L, MEMBRE, "Bearer " + token);
       assertEquals(201, resp.getStatusCodeValue());
        assertEquals("MEMBRE", ((RoleUtilisateur) resp.getBody()).getLibelle());
    }

    @Test
    void createProjet_shouldSucceedWithAdminRole() {
        String token = "tok";
        String email = "creator@x.com";
        User user = new User(1L, email, "p", "");
        Projet newProjet = new Projet(null, "Nouveau", "Desc", LocalDate.now());

        when(jwtService.extractEmail(token)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(projetService.saveProjet(newProjet)).thenReturn(newProjet);
        when(roleUtilisateurService.saveRoleUtilisateur(any())).thenAnswer(inv -> inv.getArgument(0));

        var resp = projetController.createProjet(newProjet, "Bearer " + token);
        assertEquals(201, resp.getStatusCodeValue());
        assertEquals("Nouveau", ((Projet) resp.getBody()).getNom());
    }

    @Test
    void getProjectById_shouldReturnProjectIfUserHasRole() {
        String token = "tok";
        String email = "member@x.com";
        User user = new User(1L, email, "p", "");
        Projet projet = new Projet(77L, "P", "Desc", LocalDate.now());

        when(jwtService.extractEmail(token)).thenReturn(email);
        when(userService.getUserByEmail(email)).thenReturn(Optional.of(user));
        when(roleUtilisateurService.getUserRoleInProjet(1L, 77L)).thenReturn("MEMBRE");
        when(projetService.getProjetById(77L)).thenReturn(projet);

        var resp = projetController.getProjectById(77L, "Bearer " + token);
        assertEquals(200, resp.getStatusCodeValue());
        assertEquals("P", ((Projet) resp.getBody()).getNom());
    }

}

