package com.projectmt.project_management_tool.repositories;

import com.projectmt.project_management_tool.models.RoleUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleUtilisateurRepository extends JpaRepository<RoleUtilisateur, Long> {
    List<RoleUtilisateur> findByUtilisateurId(Long userId);
    Optional<RoleUtilisateur> findByUtilisateurIdAndProjetId(Long userId, Long projetId);
    List<RoleUtilisateur> findByProjetId(Long projetId);
}
