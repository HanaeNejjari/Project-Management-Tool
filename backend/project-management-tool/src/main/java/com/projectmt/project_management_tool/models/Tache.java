package com.projectmt.project_management_tool.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tache")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @ToString
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String tacheDesc;

    private LocalDate dateEcheance;

    private String priorite;

    private LocalDate dateFin;

    private String statut;

    @ManyToOne
    @JoinColumn(name = "id_projet")
    private Projet projet;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private User user;

}
