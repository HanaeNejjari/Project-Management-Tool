package com.projectmt.project_management_tool.controllers;

import com.projectmt.project_management_tool.models.User;
import com.projectmt.project_management_tool.services.JwtService;
import com.projectmt.project_management_tool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader){
        //On vérifie que l'utilisateur est connecté
        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //On récupére l'email depuis le token
        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        //On verifie que l'utilisateur existe
        User user = userService.getUserByEmail(email).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/exists")
    public boolean isUserExists (@RequestParam String email){
        return userService.isUserExist(email);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        if (userService.isUserExist(user.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        User newUser = userService.createUser(user);

        String token = jwtService.generateToken(newUser);
        return ResponseEntity.ok(token);

    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user){
        if (userService.isUserExist(user.getEmail())){
            Optional<User> optionalUser = userService.getUserByEmail(user.getEmail());
            if (optionalUser.isPresent() && optionalUser.get().getMotDePasse().equals(user.getMotDePasse())){
                String token = jwtService.generateToken(user);
                return ResponseEntity.ok(token);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

}
