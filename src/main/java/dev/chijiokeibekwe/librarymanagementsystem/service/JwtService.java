package dev.chijiokeibekwe.librarymanagementsystem.service;

import org.springframework.security.core.Authentication;

public interface JwtService {

    String generateAccessToken(Authentication authentication);
}
