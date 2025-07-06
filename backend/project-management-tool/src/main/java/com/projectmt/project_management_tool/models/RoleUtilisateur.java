package com.projectmt.project_management_tool.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_utilisateur")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @ToString
public class RoleUtilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String libelle;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur", nullable = false)
    private User utilisateur;

    @ManyToOne
    @JoinColumn(name = "id_projet", nullable = false)
    private Projet projet;
}
