package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {

        if (user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty()) {
            return new ResponseEntity<>("Validation Error: Email and Password cannot be empty!", HttpStatus.BAD_REQUEST);
        }

        String result = userService.registerUser(user);


        if (result.equals("User registered successfully")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT); // 409 if user exists
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {

        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty() ||
                loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            return new ResponseEntity<>("Validation Error: Credentials required!", HttpStatus.BAD_REQUEST);
        }
        String response = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

        if (response.equals("Invalid credentials") || response.equals("User does not exist")) {
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}