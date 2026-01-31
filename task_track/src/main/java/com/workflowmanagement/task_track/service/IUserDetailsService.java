package com.workflowmanagement.task_track.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IUserDetailsService {

    /**
     * Loads a user by email for Spring Security authentication.
     *
     * @param email user's email
     * @return UserDetails object (wrapped in UserPrincipal)
     * @throws UsernameNotFoundException if user is not found
     */
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
}
