
package com.workflowmanagement.task_track.controller;

import com.workflowmanagement.task_track.dto.LoginRequest;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.service.implementation.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUserId(1);
        sampleUser.setUserName("Vignesh");
        sampleUser.setEmail("vicky@example.com");
        sampleUser.setPassword("pwd123");
    }

    // ---------- POST /signup ----------
    @Test
    void signup_returns201_andBody() {
        when(userService.registerUser(any(User.class))).thenReturn(sampleUser);

        ResponseEntity<User> resp = userController.signup(sampleUser);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getUserId()).isEqualTo(1);
        assertThat(resp.getBody().getEmail()).isEqualTo("vicky@example.com");

        verify(userService).registerUser(sampleUser);
    }

    // ---------- POST /login ----------
    @Test
    void login_returns200_withToken() {
        LoginRequest req = new LoginRequest();
        req.setEmail("vicky@example.com");
        req.setPassword("pwd123");

        when(userService.loginUser("vicky@example.com", "pwd123"))
                .thenReturn("jwt-token-abc");

        ResponseEntity<String> resp = userController.login(req);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isEqualTo("jwt-token-abc");

        verify(userService).loginUser("vicky@example.com", "pwd123");
    }

    // ---------- GET /getAllUsers ----------
    @Test
    void getAllUsers_whenNonEmpty_returns200_andList() {
        User u2 = new User();
        u2.setUserId(2);
        u2.setUserName("Hema");
        u2.setEmail("hema@example.com");

        when(userService.getAllUsers()).thenReturn(List.of(sampleUser, u2));

        ResponseEntity<List<User>> resp = userController.getAllUsers();

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody()).hasSize(2);
        assertThat(resp.getBody().get(0).getUserId()).isEqualTo(1);
        assertThat(resp.getBody().get(1).getUserId()).isEqualTo(2);

        verify(userService).getAllUsers();
    }

    @Test
    void getAllUsers_whenEmpty_returns204() {
        when(userService.getAllUsers()).thenReturn(List.of());

        ResponseEntity<List<User>> resp = userController.getAllUsers();

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(resp.getBody()).isNull(); // 204 has no body

        verify(userService).getAllUsers();
    }

    // ---------- GET /getUserById/{id} ----------
    @Test
    void getUserById_found_returns200_andBody() {
        when(userService.getUserById(1)).thenReturn(Optional.of(sampleUser));

        ResponseEntity<?> resp = userController.getUserById(1);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isInstanceOf(User.class);
        assertThat(((User) resp.getBody()).getUserId()).isEqualTo(1);

        verify(userService).getUserById(1);
    }

    @Test
    void getUserById_notFound_returns404() {
        when(userService.getUserById(99)).thenReturn(Optional.empty());

        ResponseEntity<?> resp = userController.getUserById(99);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resp.getBody()).isNull();

        verify(userService).getUserById(99);
    }

    // ---------- PUT /updateUser/{id} ----------
    @Test
    void updateUserById_found_returns200_andUpdatedBody() {
        User patch = new User();
        patch.setUserName("Vicky Updated");
        patch.setEmail("vicky@example.com");

        User updated = new User();
        updated.setUserId(1);
        updated.setUserName("Vicky Updated");
        updated.setEmail("vicky@example.com");

        when(userService.updateUserById(eq(1), any(User.class)))
                .thenReturn(Optional.of(updated));

        ResponseEntity<User> resp = userController.updateUserById(1, patch);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getUserId()).isEqualTo(1);
        assertThat(resp.getBody().getUserName()).isEqualTo("Vicky Updated");

        verify(userService).updateUserById(1, patch);
    }

    @Test
    void updateUserById_notFound_returns404() {
        when(userService.updateUserById(eq(999), any(User.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<User> resp = userController.updateUserById(999, new User());

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resp.getBody()).isNull();

        verify(userService).updateUserById(eq(999), any(User.class));
    }

    // ---------- DELETE /deleteUser/{id} ----------
    @Test
    void deleteUserById_success_returns200() {
        when(userService.deleteUserById(10)).thenReturn(true);

        ResponseEntity<Void> resp = userController.deleteUserById(10);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).deleteUserById(10);
    }

    @Test
    void deleteUserById_notFound_returns404() {
        when(userService.deleteUserById(11)).thenReturn(false);

        ResponseEntity<Void> resp = userController.deleteUserById(11);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService).deleteUserById(11);
    }
}
