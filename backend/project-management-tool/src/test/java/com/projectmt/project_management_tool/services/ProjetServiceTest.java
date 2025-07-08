package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.Projet;
import com.projectmt.project_management_tool.repositories.ProjetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjetServiceTest {

    private ProjetRepository projetRepository;
    private ProjetService projetService;

    @BeforeEach
    void setUp() {
        projetRepository = mock(ProjetRepository.class);
        projetService = new ProjetService(projetRepository);
    }

    @Test
    void getProjetById_shouldReturnProjetIfExists() {
        Long id = 1L;
        Projet projet = new Projet(id, "Test Projet", "desc", LocalDate.now());

        when(projetRepository.findById(id)).thenReturn(Optional.of(projet));

        Projet result = projetService.getProjetById(id);

        assertNotNull(result);
        assertEquals("Test Projet", result.getNom());
        verify(projetRepository, times(1)).findById(id);
    }

    @Test
    void getProjetById_shouldThrowExceptionIfNotFound() {
        Long id = 999L;

        when(projetRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            projetService.getProjetById(id);
        });

        assertEquals("Projet inconnu", exception.getMessage());
        verify(projetRepository, times(1)).findById(id);
    }

    @Test
    void saveProjet_shouldCallRepositorySave() {
        Projet projet = new Projet(null, "New Projet", "desc", LocalDate.now());
        Projet saved = new Projet(10L, "New Projet", "desc", LocalDate.now());

        when(projetRepository.save(projet)).thenReturn(saved);

        Projet result = projetService.saveProjet(projet);

        assertEquals(10L, result.getId());
        verify(projetRepository, times(1)).save(projet);
    }
}
