package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.User;
import com.projectmt.project_management_tool.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
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
    void getAllUsers_shouldReturnListOfUsers() {
        List<User> mockUsers = List.of(
                new User(1L, "test1@email.com", "password1", "test1"),
                new User(2L, "test2@email.com", "password2", "test2")
        );

        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("test1", result.get(0).getNomUtilisateur());
        assertEquals("test2@email.com", result.get(1).getEmail());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testCreateUser(){
        User user = new User(1L, "test@email.com", "password", "test");

        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("test@email.com", createdUser.getEmail());
    }

    @Test
    void testIsUserExist(){
        String email = "test@email.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean result = userService.isUserExist(email);

        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void testGetUserByEmail_UserExists() {
        String email = "test@email.com";
        User user = new User(1L, email, "password", "test");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByEmail(email);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    void testGetUserByEmail_UserNotExists() {
        String email = "test@email.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail(email);

        assertEquals(Optional.empty(), result);
    }

}
