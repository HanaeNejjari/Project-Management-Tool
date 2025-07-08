package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.HistoriqueModif;
import com.projectmt.project_management_tool.models.Tache;
import com.projectmt.project_management_tool.repositories.HistoriqueModifRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class HistoriqueModifServiceTest {

    private HistoriqueModifService historiqueModifService;
    private HistoriqueModifRepository historiqueModifRepository;

    @BeforeEach
    void setUp() {
        historiqueModifRepository = mock(HistoriqueModifRepository.class);
        historiqueModifService = new HistoriqueModifService();
        ReflectionTestUtils.setField(historiqueModifService, "historiqueModifRepository", historiqueModifRepository);
    }

    @Test
    void testGetHistoByTacheId_returnsExpectedList() {
        Long tacheId = 1L;
        HistoriqueModif modif = new HistoriqueModif();
        modif.setId(1L);

        when(historiqueModifRepository.findByTacheId(tacheId)).thenReturn(List.of(modif));

        List<HistoriqueModif> result = historiqueModifService.getHistoByTacheId(tacheId);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testCreateHisto_returnsSavedObject() {
        Tache tache = new Tache();
        tache.setId(1L);

        HistoriqueModif expected = new HistoriqueModif();
        expected.setId(10L);

        when(historiqueModifRepository.save(Mockito.any(HistoriqueModif.class)))
                .thenReturn(expected);

        HistoriqueModif result = historiqueModifService.createHisto(
                tache, "Nom", "Ancien", "Nouveau"
        );

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(historiqueModifRepository, times(1)).save(any());
    }

    @Test
    void testSetTacheModif_detectsModifiedFields() {
        Tache prev = new Tache();
        prev.setNom("Ancien nom");
        prev.setTacheDesc("Desc 1");
        prev.setDateEcheance(LocalDate.of(2024, 7, 1));
        prev.setDateFin(LocalDate.of(2024, 7, 10));
        prev.setPriorite("Basse");
        prev.setStatut("A faire");

        Tache updated = new Tache();
        updated.setNom("Nouveau nom");
        updated.setTacheDesc("Desc 2");
        updated.setDateEcheance(LocalDate.of(2024, 8, 1));
        updated.setDateFin(LocalDate.of(2024, 8, 10));
        updated.setPriorite("Haute");
        updated.setStatut("Fait");

        when(historiqueModifRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<HistoriqueModif> histos = historiqueModifService.setTacheModif(prev, updated);

        assertEquals(6, histos.size());
        verify(historiqueModifRepository, times(6)).save(any());
    }

    @Test
    void testSetTacheModif_shouldReturnEmptyListWhenNoChange() {
        Tache t1 = new Tache();
        t1.setNom("Nom");
        t1.setTacheDesc("Description");
        t1.setDateEcheance(LocalDate.of(2024, 7, 10));
        t1.setDateFin(LocalDate.of(2024, 7, 15));
        t1.setPriorite("Haute");
        t1.setStatut("En cours");

        Tache t2 = new Tache(t1);

        List<HistoriqueModif> histos = historiqueModifService.setTacheModif(t1, t2);

        assertTrue(histos.isEmpty());
        verify(historiqueModifRepository, never()).save(any());
    }

    @Test
    void testSetTacheModif_onlyNomChanged() {
        Tache oldT = new Tache();
        oldT.setNom("Old");

        Tache newT = new Tache();
        newT.setNom("New");

        when(historiqueModifRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        List<HistoriqueModif> result = historiqueModifService.setTacheModif(oldT, newT);

        assertEquals(1, result.size());
        assertEquals("Nom", result.get(0).getChampModifie());
    }

    @Test
    void testSetTacheModif_nullFieldsHandledGracefully() {
        Tache prev = new Tache();
        prev.setNom(null);
        prev.setTacheDesc(null);
        prev.setDateEcheance(null);
        prev.setDateFin(null);
        prev.setPriorite(null);
        prev.setStatut(null);

        Tache updated = new Tache();
        updated.setNom("Nom");
        updated.setTacheDesc("Desc");
        updated.setDateEcheance(LocalDate.of(2024, 9, 1));
        updated.setDateFin(LocalDate.of(2024, 9, 10));
        updated.setPriorite("Moyenne");
        updated.setStatut("En cours");

        when(historiqueModifRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<HistoriqueModif> histos = historiqueModifService.setTacheModif(prev, updated);

        assertEquals(6, histos.size());
        verify(historiqueModifRepository, times(6)).save(any());
    }
}
