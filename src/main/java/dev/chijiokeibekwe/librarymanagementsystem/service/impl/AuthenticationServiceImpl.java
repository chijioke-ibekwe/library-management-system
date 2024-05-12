package dev.chijiokeibekwe.librarymanagementsystem.service.impl;

import dev.chijiokeibekwe.librarymanagementsystem.config.properties.RsaKeyProperties;
import dev.chijiokeibekwe.librarymanagementsystem.dto.request.AuthenticationRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.AuthenticationResponse;
import dev.chijiokeibekwe.librarymanagementsystem.service.AuthenticationService;
import dev.chijiokeibekwe.librarymanagementsystem.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RsaKeyProperties rsaKeyProperties;

    @Override
    @Transactional
    public AuthenticationResponse login(AuthenticationRequest request) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        ));

        return new AuthenticationResponse(
                jwtService.generateAccessToken(authentication),
                "bearer",
                rsaKeyProperties.expirationInSeconds()
        );
    }
}
