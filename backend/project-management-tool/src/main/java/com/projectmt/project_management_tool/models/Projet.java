package com.projectmt.project_management_tool.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "projet")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @ToString
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    private String projetDesc;

    @Column(nullable = false)
    private LocalDate dateDebut;

}
