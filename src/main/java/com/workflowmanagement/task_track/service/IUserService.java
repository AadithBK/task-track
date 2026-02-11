
package com.workflowmanagement.task_track.service;

import com.workflowmanagement.task_track.dto.user.UserActivationDto;
import com.workflowmanagement.task_track.dto.user.SignupRequestDto;
import com.workflowmanagement.task_track.dto.user.UserResponseDto;
import com.workflowmanagement.task_track.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    /**
     * Registers a new user.
     *
     * @param userDto signup payload
     * @return created user's response dto
     * @throws com.workflowmanagement.task_track.exceptions.UserAlreadyExistsException if email already exists
     */
    UserResponseDto registerUser(SignupRequestDto userDto);

    /**
     * Authenticates a user and returns a JWT.
     *
     * @param email    user email
     * @param password raw password
     * @return JWT token
     * @throws com.workflowmanagement.task_track.exceptions.InvalidCredentialsException on bad credentials
     * @throws RuntimeException if account is not ACTIVE
     */
    String loginUser(String email, String password);

    /**
     * @return all users mapped to UserResponseDto
     */
    List<UserResponseDto> getAllUsers();

    /**
     * Gets a user by id as a response dto.
     *
     * @param id userId
     * @return optional UserResponseDto
     */
    Optional<UserResponseDto> getUserById(Integer id);

    /**
     * Gets a user entity by id.
     * Note: This exposes the entity for internal service usage (e.g., TaskService).
     *
     * @param id userId
     * @return optional User entity
     */
    Optional<User> getUserObjById(Integer id);

    /**
     * Updates an existing user by id with fields from the provided User entity.
     * Password will be re-encoded if present and non-empty.
     *
     * @param id   userId
     * @param user partial/complete User entity with updates
     * @return optional UserResponseDto
     */
    Optional<UserResponseDto> updateUserById(Integer id, User user);

    /**
     * Deletes a user by id.
     *
     * @param id userId
     * @return true if deleted, false if not found
     */
    boolean deleteUserById(Integer id);

    /**
     * Activates a user and/or assigns a role based on admin input.
     *
     * @param id            userId
     * @param activationDto role & status payload
     * @return optional UserResponseDto
     */
    Optional<UserResponseDto> activateUser(Integer id, UserActivationDto activationDto);
}

