package com.projectmt.project_management_tool.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "historique_modif")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @ToString
public class HistoriqueModif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dateModification;

    private String champModifie;

    private String ancienneValeur;

    private String nouvelleValeur;

    @ManyToOne
    @JoinColumn(name = "id_tache", nullable = false)
    private Tache tache;


}
