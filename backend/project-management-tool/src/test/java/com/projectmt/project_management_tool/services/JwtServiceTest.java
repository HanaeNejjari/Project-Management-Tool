package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        String secretKey = "my-secret-key-for-testing-jwt-service";
        long expiration = 3600000;
        String base64Secret = Base64.getEncoder().encodeToString(secretKey.getBytes());

        ReflectionTestUtils.setField(jwtService, "secretKey", base64Secret);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", expiration);
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        User user = new User(1L, "test@email.com", "password", "test");

        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractEmail_shouldReturnCorrectEmail() {
        User user = new User(1L, "test@email.com", "password", "test");

        String token = jwtService.generateToken(user);
        String email = jwtService.extractEmail(token);

        assertEquals("test@email.com", email);
    }

    @Test
    void isTokenExpired_shouldReturnFalseForValidToken() {
        User user = new User(1L, "test@email.com", "password", "test");

        String token = jwtService.generateToken(user);

        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        User user = new User(1L, "test@email.com", "password", "test");

        String token = jwtService.generateToken(user);

        assertTrue(jwtService.validateToken(token, "test@email.com"));
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidEmail() {
        User user = new User(1L, "test@email.com", "password", "test");

        String token = jwtService.generateToken(user);

        assertFalse(jwtService.validateToken(token, "wrong@email.com"));
    }
    

}
