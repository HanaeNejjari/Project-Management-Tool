package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.User;
import com.projectmt.project_management_tool.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void testCreateUser(){
        User user = new User(10, "test@mail.fr", "123456", "TestUser");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("test@mail.fr", createdUser.getEmail());
    }

    @Test
    void testIsUserExist(){
        String email = "test@mail.fr";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean result = userService.isUserExist(email);

        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void testGetUserByEmail_UserExists() {
        String email = "test@mail.fr";
        User user = new User(1L, email, "password123", "Test User");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail(email);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    void testGetUserByEmail_UserNotExists() {
        String email = "test@mail.fr";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail(email);

        assertEquals(Optional.empty(), result);
    }

}
