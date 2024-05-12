package dev.chijiokeibekwe.librarymanagementsystem.dto.response;

public record AuthenticationResponse (
        String accessToken,

        String tokenType,

        Integer expiresIn
)
{
    //
}
