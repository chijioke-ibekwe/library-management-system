package dev.chijiokeibekwe.librarymanagementsystem.exception;

import dev.chijiokeibekwe.librarymanagementsystem.common.ResponseObject;
import dev.chijiokeibekwe.librarymanagementsystem.enums.ResponseStatus;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ResponseObject<?>> handleAuthenticationException(AuthenticationException e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response =  new ResponseObject<>(
                ResponseStatus.FAILED,
                "Token is missing or invalid",
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ResponseObject<?>> handleAccessDeniedException(AccessDeniedException e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response =  new ResponseObject<>(
                ResponseStatus.FAILED,
                "You are not authorized to perform this action",
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ResponseObject<?>> handleBadCredentialsException(BadCredentialsException e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response =  new ResponseObject<>(
                ResponseStatus.FAILED,
                "Invalid username or password",
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ResponseObject<?>> handleEntityNotFoundException(EntityNotFoundException e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response =  new ResponseObject<>(
                ResponseStatus.FAILED,
                e.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(value = EntityExistsException.class)
    public ResponseEntity<ResponseObject<?>> handleEntityExistsException(EntityExistsException e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response =  new ResponseObject<>(
                ResponseStatus.FAILED,
                e.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ResponseObject<?>> handleConstraintViolationException(ConstraintViolationException e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response =  new ResponseObject<>(
                ResponseStatus.FAILED,
                e.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject<?>> handleInvalidMethodArgumentException(MethodArgumentNotValidException e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response =  new ResponseObject<>(
                ResponseStatus.FAILED,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseObject<?>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response =  new ResponseObject<>(
                ResponseStatus.FAILED,
                e.getMessage(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseObject<?>> handleGenericException(Exception e) {

        log.error(e.getMessage(), e);

        ResponseObject<?> response =  new ResponseObject<>(
                ResponseStatus.FAILED,
                "An error occurred while processing your request. Please try again",
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
