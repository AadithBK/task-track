package com.workflowmanagement.task_track.repository;

import com.workflowmanagement.task_track.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);//Spring Data JPA generates and executes: SELECT * FROM users WHERE email = 'the_email_you_provided';
//    User findByUsername(String username);


}