//package com.workflowmanagement.task_track.Exceptions;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.workflowmanagement.task_track.util.CustomJSONResponse;
//import com.workflowmanagement.task_track.util.CustomStatus;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//
//@Component
//public class CustomSecurityAccessDeniedHandler implements AccessDeniedHandler {
//@Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response,
//                       AccessDeniedException accessDeniedException) throws IOException {
//
//        // Use your existing CustomJSONResponse class
//        CustomJSONResponse<Object> customResponse = new CustomJSONResponse<>(
//                null,
//                "Permission Denied: You do not have the required role to access this resource.",
//                CustomStatus.ERR,
//                null
//        );
//
//        // Setting the response metadata
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//
//        // Converting the Java Object to JSON string and writing it to the body
//        response.getWriter().write(objectMapper.writeValueAsString(customResponse));
//    }
//}