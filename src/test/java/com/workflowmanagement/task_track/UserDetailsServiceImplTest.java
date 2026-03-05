package com.workflowmanagement.task_track;

import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.model.UserPrincipal;
import com.workflowmanagement.task_track.repository.UserRepository;
import com.workflowmanagement.task_track.service.implementation.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private User testUser;
    private final String TEST_EMAIL = "auth@example.com";

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setEmail(TEST_EMAIL);
        testUser.setPassword("hashedPassword");
        // Set other necessary fields if UserPrincipal requires them (like roles)
    }

    @Test
    void loadUserByUsername_Success() {
        // Arrange
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(testUser));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(TEST_EMAIL);

        // Assert
        assertNotNull(userDetails);
        assertTrue(userDetails instanceof UserPrincipal);
        assertEquals(TEST_EMAIL, userDetails.getUsername());

        verify(userRepository, times(1)).findByEmail(TEST_EMAIL);
    }

    @Test
    void loadUserByUsername_ThrowsException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent@example.com");
        });

        assertTrue(exception.getMessage().contains("User not found with email"));
        verify(userRepository, times(1)).findByEmail(anyString());
    }
}