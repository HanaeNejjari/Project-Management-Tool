package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendNotification(User destinataire, String nomTache, String nomProjet){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinataire.getEmail());
        message.setFrom("nejjarihanae2003@icloud.com");
        message.setSubject("Une nouvelle tâche vous a été assigné");
        message.setText("Bonjour " + destinataire.getNomUtilisateur() + ",\n\nLa tâche \"" + nomTache +
                "\" du projet \"" + nomProjet +  "\", viens de vous être assignée.\n\nMerci !");

        mailSender.send(message);
    }
}
