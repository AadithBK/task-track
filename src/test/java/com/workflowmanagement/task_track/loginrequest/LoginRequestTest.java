package com.workflowmanagement.task_track.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsValid_thenNoViolations() {
        LoginRequest req = new LoginRequest();
        req.setEmail("user@example.com");
        req.setPassword("StrongPassword!23");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);
        assertTrue(violations.isEmpty(), "Expected no constraint violations for a valid LoginRequest");
    }

    @Test
    void whenEmailBlank_thenViolationForNotBlank() {
        LoginRequest req = new LoginRequest();
        req.setEmail("   "); // blank
        req.setPassword("any");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream().anyMatch(v ->
                        v.getPropertyPath().toString().equals("email")
                                && v.getMessage().equals("Email is required")
                ),
                "Expected NotBlank violation message for email"
        );
    }

    @Test
    void whenEmailNull_thenViolationForNotBlank() {
        LoginRequest req = new LoginRequest();
        req.setEmail(null);
        req.setPassword("any");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream().anyMatch(v ->
                        v.getPropertyPath().toString().equals("email")
                                && v.getMessage().equals("Email is required")
                ),
                "Expected NotBlank violation message for null email"
        );
    }

    @Test
    void whenEmailInvalidFormat_thenViolationForEmail() {
        LoginRequest req = new LoginRequest();
        req.setEmail("not-an-email");
        req.setPassword("any");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream().anyMatch(v ->
                        v.getPropertyPath().toString().equals("email")
                                && v.getMessage().equals("Enter a valid EmailId")
                ),
                "Expected Email format violation message"
        );
    }

    @Test
    void whenPasswordBlank_thenViolationForNotBlank() {
        LoginRequest req = new LoginRequest();
        req.setEmail("user@example.com");
        req.setPassword("   ");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream().anyMatch(v ->
                        v.getPropertyPath().toString().equals("password")
                                && v.getMessage().equals("Password is required")
                ),
                "Expected NotBlank violation message for password"
        );
    }

    @Test
    void whenPasswordNull_thenViolationForNotBlank() {
        LoginRequest req = new LoginRequest();
        req.setEmail("user@example.com");
        req.setPassword(null);

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty());
        assertTrue(
                violations.stream().anyMatch(v ->
                        v.getPropertyPath().toString().equals("password")
                                && v.getMessage().equals("Password is required")
                ),
                "Expected NotBlank violation message for null password"
        );
    }

    @Test
    void lombokGeneratedMethods_basicSanity() {
        LoginRequest a = new LoginRequest();
        a.setEmail("user@example.com");
        a.setPassword("p1");

        LoginRequest b = new LoginRequest();
        b.setEmail("user@example.com");
        b.setPassword("p1");

        // @Data generates getters, setters, equals, hashCode, toString
        assertEquals("user@example.com", a.getEmail());
        assertEquals("p1", a.getPassword());

        // equals/hashCode should consider both fields
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        // toString should include class name and fields
        String s = a.toString();
        assertTrue(s.contains("LoginRequest"));
        assertTrue(s.contains("email=user@example.com"));
    }
}


