package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.AdminActivationDto;
import com.workflowmanagement.task_track.dto.LoginRequest;
import com.workflowmanagement.task_track.dto.SignupRequestDto;
import com.workflowmanagement.task_track.dto.UserResponseDto;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.service.UserService;
import com.workflowmanagement.task_track.util.CustomStatus;

import com.workflowmanagement.task_track.util.CustomJSONResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController  {


    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CustomJSONResponse<UserResponseDto>> signup(@Valid @RequestBody SignupRequestDto signupDto) {
        UserResponseDto responseDto = userService.registerUser(signupDto);
//        return CustomJSONResponse.customJSONResponse(CustomStatus.OK, "User Created", null );
    return new ResponseEntity<>( new CustomJSONResponse<>("Signup successful! Please wait for Admin activation.", null,CustomStatus.OK, responseDto ),HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", produces = {"application/json"})
    public ResponseEntity<CustomJSONResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // Now Spring only checks for email and password!
        String token = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
//        return CustomJSONResponse.customJSONResponseWithData(CustomStatus.OK, "User Created", null,  token );
        return new ResponseEntity( new CustomJSONResponse<>("", null,CustomStatus.OK, token ),HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CustomJSONResponse<List<UserResponseDto>>> getAllUsers()
    {
        List<UserResponseDto> users=userService.getAllUsers();
        if(users.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        CustomJSONResponse<List<UserResponseDto>> res=  new CustomJSONResponse<>("", null, CustomStatus.OK, users);
      return new ResponseEntity<>(res ,HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CustomJSONResponse<UserResponseDto>> getUserById(@PathVariable Integer id)
    {
        return userService.getUserById(id).
                map(userDto->new ResponseEntity<>( new CustomJSONResponse<UserResponseDto>(null, null, CustomStatus.OK, userDto),HttpStatus.OK))
                .orElse(new ResponseEntity<>( new CustomJSONResponse<>("User Not Found", null, CustomStatus.OK, null),HttpStatus.NOT_FOUND));
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomJSONResponse<UserResponseDto>> updateUserById(@Valid @PathVariable Integer id, @RequestBody User user)
    {
        return userService.updateUserById(id,user)
                .map(updatedUser->new ResponseEntity<>( new CustomJSONResponse<>("User Updated Successfully", null, CustomStatus.OK, updatedUser),HttpStatus.OK))
                .orElse(new ResponseEntity<>( new CustomJSONResponse<>(null, "User Not Found", CustomStatus.OK, null),HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id)
    {
        if(userService.deleteUserById(id))
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PutMapping("/activate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<CustomJSONResponse<UserResponseDto>> activeUser(@PathVariable Integer id, @RequestBody AdminActivationDto activationDto)
    {
        return userService.activateUser(id,activationDto)
                .map(updatedUser->new ResponseEntity<>(
                        new CustomJSONResponse<>(
                                "User Activated Successfully",null,CustomStatus.OK,updatedUser),HttpStatus.OK))
                .orElse(new ResponseEntity<>(new CustomJSONResponse<>("User not found",null,CustomStatus.ERR,null),HttpStatus.NOT_FOUND));


    }
}