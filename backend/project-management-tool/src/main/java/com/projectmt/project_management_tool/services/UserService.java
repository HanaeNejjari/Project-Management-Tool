package com.projectmt.project_management_tool.services;

import com.projectmt.project_management_tool.models.User;
import com.projectmt.project_management_tool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers(){
//        return userRepository.findAll();
        List<User> users = userRepository.findAll();
        users.forEach(System.out::println);
        return users;
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }
}
