package com.workflowmanagement.task_track.service;

import com.workflowmanagement.task_track.config.JwtUtil;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    /**
     * Registers a new user by hashing their password before saving to the DB.
     */
    public String registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return "User already exists with email: " + user.getEmail();
        }
        String hashedEntry = encoder.encode(user.getPassword());
        user.setPassword(hashedEntry);

        // 3. Save the user entity
        userRepository.save(user);
        return "User registered successfully";
    }

    /**
     * Validates credentials and returns a JWT token if successful.
     */
    public String loginUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Compare the raw password with the hashed password in the database
            if (encoder.matches(password, user.getPassword())) {
                return jwtUtil.generateToken(user.getEmail(), user.getRole().name());//If matches, generate the JWT token
            } else {
                return "Invalid credentials: Password does not match.";
            }
        }

        return "User does not exist with email: " + email;
    }
}