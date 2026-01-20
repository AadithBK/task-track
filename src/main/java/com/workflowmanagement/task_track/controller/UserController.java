package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.LoginRequest;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.service.UserService;
import com.workflowmanagement.task_track.util.CustomStatus;

import com.workflowmanagement.task_track.util.CustomJSONResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController  {


    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomJSONResponse> signup(@Valid @RequestBody User user) {
        User registeredUser = userService.registerUser(user);
//        return CustomJSONResponse.customJSONResponse(CustomStatus.OK, "User Created", null );
    return new ResponseEntity<>( new CustomJSONResponse<>("User Created", null,CustomStatus.OK, null ),HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", produces = {"application/json"})

    public ResponseEntity<CustomJSONResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // Now Spring only checks for email and password!
        String token = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
//        return CustomJSONResponse.customJSONResponseWithData(CustomStatus.OK, "User Created", null,  token );
        return new ResponseEntity( new CustomJSONResponse<>("", null,CustomStatus.OK, token ),HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<CustomJSONResponse> getAllUsers()
    {
        List<User> users=userService.getAllUsers();
        if(users.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        CustomJSONResponse<List<User>> res=  new CustomJSONResponse<>("", null, CustomStatus.OK, users);
      return new ResponseEntity<>(res ,HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<CustomJSONResponse<User>> getUserById(@PathVariable Integer id)
    {
        return userService.getUserById(id).
                map(user->new ResponseEntity<>( new CustomJSONResponse<>(null, null, CustomStatus.OK, user),HttpStatus.OK))
                .orElse(new ResponseEntity<>( new CustomJSONResponse<>("User Not Found", null, CustomStatus.OK, null),HttpStatus.NOT_FOUND));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<CustomJSONResponse<User>> updateUserById(@Valid @PathVariable Integer id, @RequestBody User user)
    {
        return userService.updateUserById(id,user)
                .map(updatedUser->new ResponseEntity<>( new CustomJSONResponse<>(null, null, CustomStatus.OK, updatedUser),HttpStatus.OK))
                .orElse(new ResponseEntity<>( new CustomJSONResponse<>(null, "User Not Found", CustomStatus.OK, null),HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id)
    {
        if(userService.deleteUserById(id))
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}