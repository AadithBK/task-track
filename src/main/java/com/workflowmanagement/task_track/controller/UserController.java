package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.LoginRequest;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.service.UserService;
import jakarta.validation.Valid;
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

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@Valid @RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        // Now Spring only checks for email and password!
        String token = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers()
    {
        List<User> users=userService.getAllUsers();
        if(users.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
      return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id)
    {
        return userService.getUserById(id).
                map(user->new ResponseEntity<>(user,HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<User> updateUserById(@Valid @PathVariable Integer id,@RequestBody User user)
    {
        return userService.updateUserById(id,user)
                .map(updatedUser->new ResponseEntity<>(updatedUser,HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("deleteUser/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id)
    {
        if(userService.deleteUserById(id))
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}