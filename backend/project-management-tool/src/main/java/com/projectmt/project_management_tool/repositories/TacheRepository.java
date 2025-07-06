package com.projectmt.project_management_tool.repositories;

import com.projectmt.project_management_tool.models.Tache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TacheRepository extends JpaRepository<Tache, Long> {
    List<Tache> findByProjetId(Long projetId);
}
