package com.workflowmanagement.task_track.Exceptions;

import com.workflowmanagement.task_track.enums.CustomStatus;
import com.workflowmanagement.task_track.util.CustomJSONResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)

    public ResponseEntity<CustomJSONResponse> handleUserExists(UserAlreadyExistsException ex)
    {
//        return CustomJSONResponse.customJSONResponse(CustomStatus.ERR, null, ex.getMessage());
    return new ResponseEntity<>(new CustomJSONResponse<>(null, ex.getMessage(), CustomStatus.ERR, null), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(IllegalArgumentException.class)

    public ResponseEntity<CustomJSONResponse> handleIllegalArgument(IllegalArgumentException ex) {
        // This will catch the message we wrote inside the @JsonCreator (e.g., "Invalid Role:...")

        return new ResponseEntity<>(new CustomJSONResponse<>(null, ex.getMessage(), CustomStatus.ERR, null), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InvalidCredentialsException.class)

    public ResponseEntity<CustomJSONResponse> handlleInvalidCredentials(InvalidCredentialsException ex)
    {
        return new ResponseEntity<>(new CustomJSONResponse<>(null, ex.getMessage(), CustomStatus.ERR, null), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ResponseEntity<CustomJSONResponse>  handleValidationExceptions(MethodArgumentNotValidException ex) {

        return new ResponseEntity<>(new CustomJSONResponse<>(null, ex.getMessage(), CustomStatus.ERR, null), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public  ResponseEntity<CustomJSONResponse> handleEntityNotFound(EntityNotFoundException ex) {

        return new ResponseEntity<>(new CustomJSONResponse<>(null, ex.getMessage(), CustomStatus.ERR, null), HttpStatus.NOT_FOUND);
    }



}