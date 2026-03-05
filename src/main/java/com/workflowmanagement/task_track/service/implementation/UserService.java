package com.workflowmanagement.task_track.service.implementation;

import com.workflowmanagement.task_track.exceptions.AwaitingApprovalException;
import com.workflowmanagement.task_track.exceptions.InvalidCredentialsException;
import com.workflowmanagement.task_track.exceptions.UserAlreadyExistsException;
import com.workflowmanagement.task_track.config.JwtUtil;
import com.workflowmanagement.task_track.dto.user.UserActivationDto;
import com.workflowmanagement.task_track.dto.user.SignupRequestDto;
import com.workflowmanagement.task_track.dto.user.UserResponseDto;
import com.workflowmanagement.task_track.enums.Status;
import com.workflowmanagement.task_track.mapper.UserMapper;
import com.workflowmanagement.task_track.model.User;
import com.workflowmanagement.task_track.repository.UserRepository;
import com.workflowmanagement.task_track.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    @Autowired
    private final  UserRepository userRepository;

    @Autowired
    private final JwtUtil jwtUtil;

    @Autowired
    private final UserMapper userMapper;


    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

//    private UserResponseDto mapToResponseDto(User user) {
//        return new UserResponseDto(
//                user.getUserId(),
//                user.getUserName(),
//                user.getEmail(),
//                user.getRole(),
//                user.getDepartment(),
//                user.getStatus()
//        );
//    }

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
        return userMapper.userToUserResponseDto(savedUser);
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User does not exist"));
        if(user.getStatus()!= Status.ACTIVE)
            throw new AwaitingApprovalException("Account is INACTIVE. Awaiting Admin approval.");
        if (!encoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials: Password does not match.");
        }


        String roleName=(user.getRole()!=null?user.getRole().name():"NONE");
        return jwtUtil.generateToken(user.getEmail(), roleName);
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> users= userRepository.findAll();
        return userMapper.toDtoList(users);


    }

    public Optional<UserResponseDto> getUserById(Integer id) {
        return Optional.ofNullable(userRepository.findById(id)
                .map(userMapper::userToUserResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found")));
    }
    public Optional<User> getUserObjById(Integer id) {
        return userRepository.findById(id);
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
                    return userMapper.userToUserResponseDto(updatedUser);
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

    @Transactional
    public Optional<UserResponseDto> activateUser(Integer id, UserActivationDto activationDto) {
        return userRepository.findById(id).map(user -> {
            user.setRole(activationDto.getRole());
            user.setStatus(activationDto.getStatus());
            User updatedUser=userRepository.save(user);

            return userMapper.userToUserResponseDto(updatedUser);

        });
    }
}