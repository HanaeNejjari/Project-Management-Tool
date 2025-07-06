package com.projectmt.project_management_tool.repositories;

import com.projectmt.project_management_tool.models.Projet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetRepository extends JpaRepository<Projet, Long> {
}
