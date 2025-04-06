package com.projectmt.project_management_tool.controllers;

import com.projectmt.project_management_tool.models.User;
import com.projectmt.project_management_tool.services.JwtService;
import com.projectmt.project_management_tool.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
