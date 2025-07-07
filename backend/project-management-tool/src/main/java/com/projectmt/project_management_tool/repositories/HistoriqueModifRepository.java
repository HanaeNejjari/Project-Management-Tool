package com.projectmt.project_management_tool.repositories;

import com.projectmt.project_management_tool.models.HistoriqueModif;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueModifRepository extends JpaRepository<HistoriqueModif, Long> {
    List<HistoriqueModif> findByTacheId(Long tacheId);
}
