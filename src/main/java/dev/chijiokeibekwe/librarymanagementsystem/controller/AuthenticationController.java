package dev.chijiokeibekwe.librarymanagementsystem.controller;

import dev.chijiokeibekwe.librarymanagementsystem.dto.request.AuthenticationRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.AuthenticationResponse;
import dev.chijiokeibekwe.librarymanagementsystem.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @SecurityRequirements
    @Operation(summary = "Login", description = "Login as a registered user")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(authenticationService.login(request));
    }
}
