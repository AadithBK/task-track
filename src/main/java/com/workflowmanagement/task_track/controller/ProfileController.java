package com.workflowmanagement.task_track.controller;


import com.workflowmanagement.task_track.model.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @GetMapping("/me")
    public Map<String, String> getMyProfile() {
        // Get the email we stored in the SecurityContext during the JwtFilter stage
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = (UserPrincipal) auth.getPrincipal();
        String email = principal.user().getEmail();

        return Map.of(
                "message", "Welcome to your protected profile!",
                "userEmail", email,
                "status", "Authenticated via JWT"
        );
    }
}