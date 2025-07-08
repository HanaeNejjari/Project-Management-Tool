package com.projectmt.project_management_tool.controllers;

import com.projectmt.project_management_tool.models.HistoriqueModif;
import com.projectmt.project_management_tool.services.HistoriqueModifService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@WebMvcTest(HistoriqueModifController.class)
public class HistoriqueModifControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HistoriqueModifService historiqueModifService;

    @Test
    void testGetHistoByTache() throws Exception {
        Long tacheId = 1L;

        // Données simulées
        HistoriqueModif modif1 = new HistoriqueModif();
        modif1.setId(10L);
        modif1.setDateModification(LocalDate.of(2024, 5, 1));
        modif1.setChampModifie("titre");
        modif1.setAncienneValeur("Ancien titre");
        modif1.setNouvelleValeur("Nouveau titre");
        modif1.setTache(null);

        HistoriqueModif modif2 = new HistoriqueModif();
        modif2.setId(20L);
        modif2.setDateModification(LocalDate.of(2024, 5, 2));
        modif2.setChampModifie("description");
        modif2.setAncienneValeur("Ancienne desc");
        modif2.setNouvelleValeur("Nouvelle desc");
        modif2.setTache(null);

        List<HistoriqueModif> mockList = Arrays.asList(modif1, modif2);

        // Simulation du service
        when(historiqueModifService.getHistoByTacheId(tacheId)).thenReturn(mockList);

        // Exécution de la requête et vérification JSON
        mockMvc.perform(get("/api/histo/tache/{tacheId}", tacheId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(10)))
                .andExpect(jsonPath("$[0].champModifie", is("titre")))
                .andExpect(jsonPath("$[0].ancienneValeur", is("Ancien titre")))
                .andExpect(jsonPath("$[0].nouvelleValeur", is("Nouveau titre")))
                .andExpect(jsonPath("$[1].id", is(20)))
                .andExpect(jsonPath("$[1].champModifie", is("description")))
                .andExpect(jsonPath("$[1].ancienneValeur", is("Ancienne desc")))
                .andExpect(jsonPath("$[1].nouvelleValeur", is("Nouvelle desc")));
    }
}
