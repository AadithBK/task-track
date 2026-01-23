package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.LoginRequest;
import com.workflowmanagement.task_track.dto.SignupRequestDto;
import com.workflowmanagement.task_track.dto.UserResponseDto;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.service.implementation.UserService;

import com.workflowmanagement.task_track.util.CustomJSONResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController  {


    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomJSONResponse> signup(@RequestBody SignupRequestDto user) {
        UserResponseDto registeredUser = userService.registerUser(user);
//        return CustomJSONResponse.customJSONResponse(CustomStatus.OK, "User Created", null );
    return new ResponseEntity<>(CustomJSONResponse.ok("User Created", null),HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", produces = {"application/json"})

    public ResponseEntity<CustomJSONResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // Now Spring only checks for email and password!
        String token = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
//        return CustomJSONResponse.customJSONResponseWithData(CustomStatus.OK, "User Created", null,  token );
        return new ResponseEntity(CustomJSONResponse.ok(null, token),HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<CustomJSONResponse> getAllUsers()
    {
        List<UserResponseDto> users=userService.getAllUsers();
        if(users.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        CustomJSONResponse<List<UserResponseDto>> res= CustomJSONResponse.ok(null, users);
      return new ResponseEntity<>(res ,HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<CustomJSONResponse<Optional<UserResponseDto>>> getUserById(@PathVariable Integer id) {
        Optional<UserResponseDto> user = userService.getUserById(id);
        return ResponseEntity.ok(CustomJSONResponse.ok("User retrieved successfully", user));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<CustomJSONResponse<UserResponseDto>> updateUserById(@Valid @PathVariable Integer id, @RequestBody User user)
    {
        return userService.updateUserById(id,user)
                .map(updatedUser->new ResponseEntity<>(CustomJSONResponse.ok(null, updatedUser),HttpStatus.OK))
                .orElse(new ResponseEntity<>(CustomJSONResponse.err("User Not Found"),HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id)
    {
        if(userService.deleteUserById(id))
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}