package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

public class EmailServiceTest {

    private EmailService emailService;
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        mailSender = mock(JavaMailSender.class);
        emailService = new EmailService();
        org.springframework.test.util.ReflectionTestUtils.setField(emailService, "mailSender", mailSender);
    }

    @Test
    void sendNotification_shouldSendEmail() {
        // Arrange
        User user = new User();
        user.setEmail("test@email.com");
        user.setNomUtilisateur("Testeur");

        String tache = "Créer un module";
        String projet = "Project MT";

        // Act
        emailService.sendNotification(user, tache, projet);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
