package com.example.pingstack.exceptions;

import com.example.pingstack.ErrorMessage.ErrorMessage;
import com.example.pingstack.exceptions.auth.UserAlreadyExistException;
import com.example.pingstack.exceptions.notfound.ResourceNotFoundException;
import com.example.pingstack.exceptions.unauthorized.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalTime;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> handleUserAlreadyException(UserAlreadyExistException ex){
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.CONTINUE.value(),
                ex.getMessage(),
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException (ResourceNotFoundException ex){
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.CONTINUE.value(),
                ex.getMessage(),
                LocalTime.now()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDeniedException( AccessDeniedException accessDeniedException){
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.CONTINUE.value(),
                accessDeniedException.getMessage(),
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGeneralException(Exception ex){
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.CONTINUE.value(),
                ex.getMessage(),
                LocalTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }


}
