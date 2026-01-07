package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty()) {
            return "Validation Error: Email and Password cannot be empty!";
        }
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty() ||
                loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            return "Validation Error: Credentials required!";
        }
        return userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
    }



}