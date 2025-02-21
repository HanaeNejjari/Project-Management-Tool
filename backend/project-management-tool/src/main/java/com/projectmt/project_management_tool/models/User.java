package com.projectmt.project_management_tool.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "utilisateur")
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor @ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String motdepasse;

    @Column(nullable = false)
    private String nomutilisateur;

}
