package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.Projet;
import com.projectmt.project_management_tool.models.RoleUtilisateur;
import com.projectmt.project_management_tool.repositories.RoleUtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleUtilisateurService {

    private final RoleUtilisateurRepository roleUtilisateurRepository;

    public RoleUtilisateurService(RoleUtilisateurRepository roleUtilisateurRepository) {
        this.roleUtilisateurRepository = roleUtilisateurRepository;
    }

    public Optional<RoleUtilisateur> getRoleById(Long id){
        return roleUtilisateurRepository.findById(id);
    }

    public List<Projet> getProjetsByUserId(Long userId){
        return roleUtilisateurRepository.findByUtilisateurId(userId).stream()
                .map(RoleUtilisateur::getProjet)
                .distinct()
                .collect(Collectors.toList());
    }

    public String getUserRoleInProjet(Long userId, Long projetId){
        return roleUtilisateurRepository.findByUtilisateurId(userId).stream()
                .filter(r -> r.getProjet().getId().equals(projetId))
                .map(RoleUtilisateur::getLibelle)
                .findFirst()
                .orElse(null);
    }

    public List<RoleUtilisateur> getUsersRole(Long projetId){
        return roleUtilisateurRepository.findByProjetId(projetId);
    }

    public Optional<RoleUtilisateur> getRoleByUserIdAndProjetId(Long userId, Long projetId){
        return roleUtilisateurRepository.findByUtilisateurIdAndProjetId(userId, projetId);
    }

    public RoleUtilisateur saveRoleUtilisateur(RoleUtilisateur roleUtilisateur){
        return roleUtilisateurRepository.save(roleUtilisateur);
    }

    public void removeRole(RoleUtilisateur roleUtilisateur){
        roleUtilisateurRepository.delete(roleUtilisateur);
    }
}
