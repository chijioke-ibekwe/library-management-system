package dev.chijiokeibekwe.librarymanagementsystem.controller;

import dev.chijiokeibekwe.librarymanagementsystem.common.ResponseObject;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.UserRegistrationRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.UserResponse;
import dev.chijiokeibekwe.librarymanagementsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static dev.chijiokeibekwe.librarymanagementsystem.enums.ResponseStatus.SUCCESSFUL;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Sign up or register", description = "Sign up as a user")
    @PostMapping
    public ResponseObject<UserResponse> registerUser(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest){
        log.info("Received user registration request for email: {}", userRegistrationRequest.email());

        return new ResponseObject<>(
                SUCCESSFUL,
                "User registered successfully",
                userService.registerUser(userRegistrationRequest)
        );
    }

    @Operation(summary = "Fetch all users", description = "Fetch all users on the platform")
    @GetMapping
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseObject<Page<UserResponse>> getAllUsers(@PageableDefault(
                                                            sort = "id", direction = Sort.Direction.DESC
                                                          ) Pageable pageable){

        return new ResponseObject<>(
                SUCCESSFUL,
                null,
                userService.getAllUsers(pageable)
        );
    }

    @Operation(summary = "Fetch a single user", description = "Fetch a single user on the platform")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('users:read')")
    public ResponseObject<UserResponse> getUser(@PathVariable Long id){

        return new ResponseObject<>(
                SUCCESSFUL,
                null,
                userService.getUser(id)
        );
    }
}
