package com.workflowmanagement.task_track.service;

import com.workflowmanagement.task_track.Exceptions.InvalidCredentialsException;
import com.workflowmanagement.task_track.Exceptions.UserAlreadyExistsException;
import com.workflowmanagement.task_track.config.JwtUtil;
import com.workflowmanagement.task_track.dto.AdminActivationDto;
import com.workflowmanagement.task_track.dto.SignupRequestDto;
import com.workflowmanagement.task_track.dto.UserResponseDto;
import com.workflowmanagement.task_track.enums.Status;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.model.UserPrincipal;
import com.workflowmanagement.task_track.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private UserResponseDto mapToResponseDto(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getRole(),
                user.getDepartment(),
                user.getStatus()
        );
    }

    public UserResponseDto registerUser(SignupRequestDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + userDto.getEmail());
        }
        User user=new User();
        user.setUserName(userDto.getUserName());
        user.setEmail(userDto.getEmail());
        user.setDepartment(userDto.getDepartment());
        user.setPassword(encoder.encode(userDto.getPassword()));

        User savedUser= userRepository.save(user);
        return mapToResponseDto(savedUser);
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User does not exist"));
        System.out.println(email);
        System.out.println("Stored Hash in DB: " + user.getPassword());

        if(user.getStatus()!= Status.ACTIVE)
        {
            throw new RuntimeException("Account is INACTIVE. Awaiting Admin approval.");
        }
        if (!encoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials: Password does not match.");
        }


        String roleName=(user.getRole()!=null?user.getRole().name():"NONE");
        return jwtUtil.generateToken(user.getEmail(), roleName);
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().
                stream().map(this::mapToResponseDto)
                .toList();
    }

    public Optional<UserResponseDto> getUserById(Integer id) {
        return userRepository.findById(id).map(this::mapToResponseDto);
    }

    public Optional<UserResponseDto> updateUserById(Integer id, User user) {

        return userRepository.findById(id).map(
                existingUser-> {
                    existingUser.setUserName(user.getUserName());
                    existingUser.setRole(user.getRole());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setStatus(user.getStatus());
                    existingUser.setDepartment(user.getDepartment());
                    if(user.getPassword()!=null && !user.getPassword().isEmpty())
                    {
                            existingUser.setPassword(encoder.encode(user.getPassword()) );
                    }
                    User updatedUser = userRepository.save(existingUser);
                    return mapToResponseDto(updatedUser);
                }
        );
    }

    public boolean deleteUserById(Integer id) {
        if(userRepository.existsById(id))
        {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<UserResponseDto> activateUser(Integer id, AdminActivationDto activationDto) {
        return userRepository.findById(id).map(user -> {
            user.setRole(activationDto.getRole());
            user.setStatus(activationDto.getStatus());
            User updatedUser=userRepository.save(user);

            return mapToResponseDto(updatedUser);

        });
    }
}