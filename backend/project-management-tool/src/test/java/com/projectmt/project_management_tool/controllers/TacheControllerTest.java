package com.projectmt.project_management_tool.controllers;

import com.projectmt.project_management_tool.models.Projet;
import com.projectmt.project_management_tool.models.Tache;
import com.projectmt.project_management_tool.models.User;
import com.projectmt.project_management_tool.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TacheControllerTest {

    private TacheController controller;
    private TacheService tacheService;
    private UserService userService;
    private ProjetService projetService;
    private HistoriqueModifService historiqueModifService;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        tacheService = mock(TacheService.class);
        userService = mock(UserService.class);
        projetService = mock(ProjetService.class);
        historiqueModifService = mock(HistoriqueModifService.class);
        emailService = mock(EmailService.class);

        controller = new TacheController();

        ReflectionTestUtils.setField(controller, "tacheService", tacheService);
        ReflectionTestUtils.setField(controller, "userService", userService);
        ReflectionTestUtils.setField(controller, "projetService", projetService);
        ReflectionTestUtils.setField(controller, "historiqueModifService", historiqueModifService);
        ReflectionTestUtils.setField(controller, "emailService", emailService);
    }

    @Test
    void getTachesByProjet_shouldReturnTaches() {
        Long projetId = 1L;
        Tache t1 = new Tache();
        t1.setId(1L);
        when(tacheService.getTachesByProjetId(projetId)).thenReturn(List.of(t1));

        List<Tache> result = controller.getTachesByProjet(projetId);

        assertEquals(1, result.size());
        verify(tacheService).getTachesByProjetId(projetId);
    }

    @Test
    void createTache_shouldCreateTacheWithUserAndProjet() {
        Long projetId = 1L;
        Long userId = 2L;

        Projet projet = new Projet(projetId, "Projet Test", "desc", LocalDate.now());
        User user = new User(userId, "mail@test.com", "pass", "user");

        Tache tache = new Tache();
        tache.setNom("Nouvelle tâche");

        when(projetService.getProjetById(projetId)).thenReturn(projet);
        when(userService.getUserById(userId)).thenReturn(user);
        when(tacheService.saveTache(any())).thenReturn(tache);

        Tache result = controller.createTache(tache, projetId, userId);

        assertEquals("Nouvelle tâche", result.getNom());
        verify(tacheService).saveTache(tache);
    }

    @Test
    void createTache_shouldThrowIfProjetNotFound() {
        when(projetService.getProjetById(anyLong())).thenThrow(new RuntimeException("Projet inconnu"));

        Tache t = new Tache();
        assertThrows(RuntimeException.class, () -> controller.createTache(t, 1L, 2L));
    }

    @Test
    void createTache_shouldThrowIfUserNotFound() {
        Projet projet = new Projet(1L, "P", "desc", LocalDate.now());
        when(projetService.getProjetById(1L)).thenReturn(projet);
        when(userService.getUserById(anyLong())).thenThrow(new RuntimeException("User inconnu"));

        Tache t = new Tache();
        assertThrows(RuntimeException.class, () -> controller.createTache(t, 1L, 2L));
    }

    @Test
    void updateTache_shouldUpdateAndTrackModifications() {
        Tache incoming = new Tache();
        incoming.setId(1L);
        incoming.setNom("Modifiée");

        Tache original = new Tache();
        original.setId(1L);
        original.setNom("Originale");

        when(tacheService.getTacheById(1L)).thenReturn(original);
        when(tacheService.updateTache(incoming)).thenReturn(incoming);

        Tache result = controller.updateTache(incoming);

        assertEquals("Modifiée", result.getNom());
        verify(historiqueModifService).setTacheModif(any(), any());
    }

    @Test
    void updateTache_shouldHandleNoModification() {
        Tache t = new Tache();
        t.setId(1L);
        t.setNom("Tâche");
        t.setTacheDesc("desc");

        when(tacheService.getTacheById(1L)).thenReturn(t);
        when(tacheService.updateTache(t)).thenReturn(t);
        when(historiqueModifService.setTacheModif(any(), any())).thenReturn(List.of());

        Tache result = controller.updateTache(t);

        assertEquals("Tâche", result.getNom());
        verify(historiqueModifService).setTacheModif(any(), any());
    }

    @Test
    void assignTask_shouldAssignUserAndSendEmail() {
        Long tacheId = 1L;
        Long userId = 2L;

        User oldUser = new User(100L, "old@user.com", "pass", "old");
        User newUser = new User(userId, "new@user.com", "pass", "new");

        Projet projet = new Projet(1L, "Projet A", "desc", LocalDate.now());

        Tache tache = new Tache();
        tache.setId(tacheId);
        tache.setNom("Tâche assignée");
        tache.setUser(oldUser);
        tache.setProjet(projet);

        Tache updatedTache = new Tache(tache);
        updatedTache.setUser(newUser);
        updatedTache.setProjet(projet);

        when(tacheService.getTacheById(tacheId)).thenReturn(tache);
        when(userService.getUserById(userId)).thenReturn(newUser);
        when(tacheService.saveTache(any())).thenReturn(updatedTache);

        Tache result = controller.assignTask(tacheId, userId);

        assertEquals("Tâche assignée", result.getNom());
        assertEquals(newUser, result.getUser());
        verify(historiqueModifService).createHisto(any(), eq("Responsable"), eq("old@user.com"), eq("new@user.com"));
        verify(emailService).sendNotification(newUser, "Tâche assignée", "Projet A");
    }

    @Test
    void assignTask_shouldHandleNullProjetGracefully() {
        Long tacheId = 1L;
        Long userId = 2L;

        User oldUser = new User(100L, "old@user.com", "pass", "old");
        User newUser = new User(userId, "new@user.com", "pass", "new");

        Tache tache = new Tache();
        tache.setId(tacheId);
        tache.setNom("Tâche sans projet");
        tache.setUser(oldUser);
        tache.setProjet(null);  // Projet manquant

        when(tacheService.getTacheById(tacheId)).thenReturn(tache);
        when(userService.getUserById(userId)).thenReturn(newUser);
        when(tacheService.saveTache(any())).thenReturn(tache);

        assertThrows(NullPointerException.class, () -> controller.assignTask(tacheId, userId));

        verify(historiqueModifService).createHisto(any(), eq("Responsable"), eq("old@user.com"), eq("new@user.com"));
    }

    @Test
    void assignTask_shouldThrowIfTacheNotFound() {
        when(tacheService.getTacheById(999L)).thenThrow(new RuntimeException("Tâche introuvable"));

        assertThrows(RuntimeException.class, () -> controller.assignTask(999L, 1L));
    }

    @Test
    void assignTask_shouldHandleNullPreviousUser() {
        Long tacheId = 1L;
        Long userId = 2L;

        User newUser = new User(userId, "new@user.com", "pass", "new");

        Tache tache = new Tache();
        tache.setId(tacheId);
        tache.setNom("Tâche");
        tache.setUser(null);
        tache.setProjet(new Projet(1L, "P", "d", LocalDate.now()));

        when(tacheService.getTacheById(tacheId)).thenReturn(tache);
        when(userService.getUserById(userId)).thenReturn(newUser);
        when(tacheService.saveTache(any())).thenReturn(tache);

        assertThrows(NullPointerException.class, () -> controller.assignTask(tacheId, userId));
    }

    @Test
    void assignTask_shouldHandleEmailServiceFailure() {
        Long tacheId = 1L;
        Long userId = 2L;

        User oldUser = new User(1L, "old@x", "pass", "u");
        User newUser = new User(userId, "new@x", "pass", "u");
        Projet p = new Projet(1L, "P", "d", LocalDate.now());

        Tache t = new Tache();
        t.setId(tacheId);
        t.setNom("X");
        t.setUser(oldUser);
        t.setProjet(p);

        when(tacheService.getTacheById(tacheId)).thenReturn(t);
        when(userService.getUserById(userId)).thenReturn(newUser);
        when(tacheService.saveTache(any())).thenReturn(t);
        doThrow(new RuntimeException("Mail fail")).when(emailService).sendNotification(any(), any(), any());

        assertThrows(RuntimeException.class, () -> controller.assignTask(tacheId, userId));
    }
}
