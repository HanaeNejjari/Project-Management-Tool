package com.projectmt.project_management_tool.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectmt.project_management_tool.models.User;
import com.projectmt.project_management_tool.services.JwtService;
import com.projectmt.project_management_tool.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(UserController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(new User(), new User()));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void isUserExists_shouldReturnTrueOrFalse() throws Exception {
        when(userService.isUserExist("test@mail.fr")).thenReturn(true);

        mockMvc.perform(get("/api/users/exists?email=test@mail.fr"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService).isUserExist("test@mail.fr");
    }

    @Test
    void registerUser_shouldReturnTokenIfNewUser() throws Exception {
        User user = new User();
        user.setEmail("new@mail.fr");
        user.setMotDePasse("password");

        when(userService.isUserExist(user.getEmail())).thenReturn(false);
        when(userService.createUser(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("mocked-token");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("mocked-token"));
    }

    @Test
    void registerUser_shouldReturnBadRequestIfUserExists() throws Exception {
        User user = new User();
        user.setEmail("existing@mail.fr");

        when(userService.isUserExist(user.getEmail())).thenReturn(true);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User already exists"));
    }

    @Test
    void loginUser_shouldReturnTokenIfCredentialsMatch() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setMotDePasse("password");

        when(userService.isUserExist(user.getEmail())).thenReturn(true);
        when(userService.getUserByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"motDePasse\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("jwt-token"));
    }

    @Test
    void loginUser_shouldReturnUnauthorizedIfInvalidCredentials() throws Exception {
        User user = new User();
        user.setEmail("user@mail.fr");
        user.setMotDePasse("wrongpassword");

        User existingUser = new User();
        existingUser.setEmail("user@mail.fr");
        existingUser.setMotDePasse("correctpassword");

        when(userService.isUserExist(user.getEmail())).thenReturn(true);
        when(userService.getUserByEmail(user.getEmail())).thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }
}
