package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.Projet;
import com.projectmt.project_management_tool.models.Tache;
import com.projectmt.project_management_tool.models.User;
import com.projectmt.project_management_tool.repositories.TacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TacheServiceTest {

    private TacheRepository tacheRepository;
    private TacheService tacheService;

    @BeforeEach
    void setUp() {
        tacheRepository = mock(TacheRepository.class);
        tacheService = new TacheService();
        // Injection manuelle via reflection
        org.springframework.test.util.ReflectionTestUtils.setField(tacheService, "tacheRepository", tacheRepository);
    }

    @Test
    void getTacheById_shouldReturnTacheIfExists() {
        Tache tache = new Tache();
        tache.setId(1L);
        when(tacheRepository.findById(1L)).thenReturn(Optional.of(tache));

        Tache result = tacheService.getTacheById(1L);

        assertEquals(1L, result.getId());
        verify(tacheRepository).findById(1L);
    }

    @Test
    void getTacheById_shouldThrowExceptionIfNotFound() {
        when(tacheRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> tacheService.getTacheById(999L));
        assertEquals("Tache inconnu", ex.getMessage());
    }

    @Test
    void getTachesByProjetId_shouldReturnList() {
        Tache t1 = new Tache();
        t1.setId(1L);
        Tache t2 = new Tache();
        t2.setId(2L);

        when(tacheRepository.findByProjetId(10L)).thenReturn(List.of(t1, t2));

        List<Tache> result = tacheService.getTachesByProjetId(10L);

        assertEquals(2, result.size());
        verify(tacheRepository).findByProjetId(10L);
    }

    @Test
    void saveTache_shouldCallRepositorySave() {
        Tache tache = new Tache();
        tache.setNom("Ma tâche");

        when(tacheRepository.save(tache)).thenReturn(tache);

        Tache result = tacheService.saveTache(tache);

        assertEquals("Ma tâche", result.getNom());
        verify(tacheRepository).save(tache);
    }

    @Test
    void updateTache_shouldKeepUserAndProjetAndSave() {
        // Donnée d'origine
        Projet projet = new Projet(1L, "P", "desc", LocalDate.now());
        User user = new User(1L, "user@mail.com", "pass", "user");

        Tache dbTache = new Tache();
        dbTache.setId(1L);
        dbTache.setProjet(projet);
        dbTache.setUser(user);

        Tache inputTache = new Tache();
        inputTache.setId(1L);
        inputTache.setNom("Mise à jour");

        Tache savedTache = new Tache(inputTache);
        savedTache.setProjet(projet);
        savedTache.setUser(user);

        when(tacheRepository.findById(1L)).thenReturn(Optional.of(dbTache));
        when(tacheRepository.save(any())).thenReturn(savedTache);

        Tache result = tacheService.updateTache(inputTache);

        assertEquals("Mise à jour", result.getNom());
        assertEquals(projet, result.getProjet());
        assertEquals(user, result.getUser());
    }
}
