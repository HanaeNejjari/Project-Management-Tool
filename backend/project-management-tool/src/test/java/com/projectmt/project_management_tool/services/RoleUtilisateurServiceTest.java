package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.Projet;
import com.projectmt.project_management_tool.models.RoleUtilisateur;
import com.projectmt.project_management_tool.repositories.RoleUtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleUtilisateurServiceTest {

    private RoleUtilisateurRepository roleUtilisateurRepository;
    private RoleUtilisateurService roleUtilisateurService;

    @BeforeEach
    void setUp() {
        roleUtilisateurRepository = mock(RoleUtilisateurRepository.class);
        roleUtilisateurService = new RoleUtilisateurService(roleUtilisateurRepository);
    }

    @Test
    void getRoleById_shouldReturnOptionalRole() {
        RoleUtilisateur role = new RoleUtilisateur();
        role.setId(1L);

        when(roleUtilisateurRepository.findById(1L)).thenReturn(Optional.of(role));

        Optional<RoleUtilisateur> result = roleUtilisateurService.getRoleById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void getProjetsByUserId_shouldReturnDistinctProjects() {
        Projet projet1 = new Projet(1L, "P1", "desc", null);
        Projet projet2 = new Projet(2L, "P2", "desc", null);

        RoleUtilisateur r1 = new RoleUtilisateur();
        r1.setProjet(projet1);
        RoleUtilisateur r2 = new RoleUtilisateur();
        r2.setProjet(projet2);
        RoleUtilisateur r3 = new RoleUtilisateur();
        r3.setProjet(projet1); // duplicate

        when(roleUtilisateurRepository.findByUtilisateurId(5L)).thenReturn(List.of(r1, r2, r3));

        List<Projet> projets = roleUtilisateurService.getProjetsByUserId(5L);

        assertEquals(2, projets.size());
    }

    @Test
    void getUserRoleInProjet_shouldReturnCorrectRole() {
        Projet projet = new Projet();
        projet.setId(10L);

        RoleUtilisateur role1 = new RoleUtilisateur();
        role1.setProjet(projet);
        role1.setLibelle("ADMIN");

        when(roleUtilisateurRepository.findByUtilisateurId(2L)).thenReturn(List.of(role1));

        String role = roleUtilisateurService.getUserRoleInProjet(2L, 10L);

        assertEquals("ADMIN", role);
    }

    @Test
    void getUserRoleInProjet_shouldReturnNullIfNotFound() {
        when(roleUtilisateurRepository.findByUtilisateurId(2L)).thenReturn(Collections.emptyList());

        String role = roleUtilisateurService.getUserRoleInProjet(2L, 99L);

        assertNull(role);
    }

    @Test
    void getUsersRole_shouldReturnAllRolesForProject() {
        RoleUtilisateur r1 = new RoleUtilisateur();
        RoleUtilisateur r2 = new RoleUtilisateur();

        when(roleUtilisateurRepository.findByProjetId(10L)).thenReturn(List.of(r1, r2));

        List<RoleUtilisateur> roles = roleUtilisateurService.getUsersRole(10L);

        assertEquals(2, roles.size());
    }

    @Test
    void getRoleByUserIdAndProjetId_shouldReturnRoleIfExists() {
        RoleUtilisateur role = new RoleUtilisateur();
        role.setLibelle("MEMBRE");

        when(roleUtilisateurRepository.findByUtilisateurIdAndProjetId(1L, 2L))
                .thenReturn(Optional.of(role));

        Optional<RoleUtilisateur> result = roleUtilisateurService.getRoleByUserIdAndProjetId(1L, 2L);

        assertTrue(result.isPresent());
        assertEquals("MEMBRE", result.get().getLibelle());
    }

    @Test
    void saveRoleUtilisateur_shouldReturnSavedRole() {
        RoleUtilisateur role = new RoleUtilisateur();
        role.setLibelle("INVITE");

        when(roleUtilisateurRepository.save(role)).thenReturn(role);

        RoleUtilisateur result = roleUtilisateurService.saveRoleUtilisateur(role);

        assertEquals("INVITE", result.getLibelle());
    }

    @Test
    void removeRole_shouldCallDelete() {
        RoleUtilisateur role = new RoleUtilisateur();
        role.setId(99L);

        roleUtilisateurService.removeRole(role);

        verify(roleUtilisateurRepository, times(1)).delete(role);
    }
}
