package com.workflowmanagement.task_track;

import com.workflowmanagement.task_track.config.JwtUtil;
import com.workflowmanagement.task_track.dto.user.SignupRequestDto;
import com.workflowmanagement.task_track.dto.user.UserResponseDto;
import com.workflowmanagement.task_track.enums.Status;
import com.workflowmanagement.task_track.exceptions.AwaitingApprovalException;
import com.workflowmanagement.task_track.exceptions.InvalidCredentialsException;
import com.workflowmanagement.task_track.exceptions.UserAlreadyExistsException;
import com.workflowmanagement.task_track.mapper.UserMapper;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.repository.UserRepository;
import com.workflowmanagement.task_track.service.implementation.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private SignupRequestDto signupRequestDto;
    private UserResponseDto userResponseDto;
    private final BCryptPasswordEncoder realEncoder = new BCryptPasswordEncoder(12);

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setEmail("test@example.com");
        testUser.setPassword(realEncoder.encode("password123"));
        testUser.setStatus(Status.ACTIVE);

        signupRequestDto = new SignupRequestDto();
        signupRequestDto.setEmail("test@example.com");
        signupRequestDto.setUserName("tester");
        signupRequestDto.setPassword("password123");

        userResponseDto = new UserResponseDto();
        userResponseDto.setEmail("test@example.com");
    }

    // --- Register User Tests ---

    @Test
    void registerUser_Success() {
        // 1. Mock the repository to return empty (user doesn't exist yet)
        when(userRepository.findByEmail(signupRequestDto.getEmail())).thenReturn(Optional.empty());

        // 2. Mock the save to return our testUser (with the ID and hashed pass)
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // 3. IMPORTANT: Mock the mapper to handle the specific testUser
        // we are returning from the save method
        when(userMapper.userToUserResponseDto(testUser)).thenReturn(userResponseDto);

        // Act
        UserResponseDto result = userService.registerUser(signupRequestDto);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(userResponseDto.getEmail(), result.getEmail());

        // Verify that save was called exactly once
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ThrowsException_IfUserExists() {
        when(userRepository.findByEmail(signupRequestDto.getEmail())).thenReturn(Optional.of(testUser));

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(signupRequestDto));
        verify(userRepository, never()).save(any(User.class));
    }

    // --- Login User Tests ---

    @Test
    void loginUser_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("mocked-jwt-token");

        String token = userService.loginUser("test@example.com", "password123");

        assertEquals("mocked-jwt-token", token);
    }

    @Test
    void loginUser_ThrowsException_IfUserInactive() {
        testUser.setStatus(Status.INACTIVE);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        assertThrows(AwaitingApprovalException.class, () -> userService.loginUser("test@example.com", "password123"));
    }

    @Test
    void loginUser_ThrowsException_InvalidPassword() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        assertThrows(InvalidCredentialsException.class, () -> userService.loginUser("test@example.com", "wrong-pass"));
    }

    // --- Delete User Tests ---

    @Test
    void deleteUserById_ReturnsTrue_WhenUserExists() {
        when(userRepository.existsById(1)).thenReturn(true);

        boolean deleted = userService.deleteUserById(1);

        assertTrue(deleted);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteUserById_ReturnsFalse_WhenUserDoesNotExist() {
        when(userRepository.existsById(1)).thenReturn(false);

        boolean deleted = userService.deleteUserById(1);

        assertFalse(deleted);
        verify(userRepository, never()).deleteById(1);
    }
}