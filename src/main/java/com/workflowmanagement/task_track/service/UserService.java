package com.workflowmanagement.task_track.service;

import com.workflowmanagement.task_track.Exceptions.InvalidCredentialsException;
import com.workflowmanagement.task_track.Exceptions.UserAlreadyExistsException;
import com.workflowmanagement.task_track.config.JwtUtil;
import com.workflowmanagement.task_track.controller.UserController;
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

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + user.getEmail());
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User does not exist"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials: Password does not match.");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole().name());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUserById(Integer id, User user) {

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
                    return userRepository.save(existingUser);
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
}